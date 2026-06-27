package com.resumeanalyzer.service;

import com.resumeanalyzer.model.Resume;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Service for ATS (Applicant Tracking System) Analysis
 * 
 * Provides functionality for:
 * - ATS score calculation based on resume quality
 * - Skill extraction from resume content
 * - Feedback generation for resume improvements
 * - Keyword density analysis
 * - Format compatibility checking
 */
@Service
public class ATSService {

    private static final Logger logger = LoggerFactory.getLogger(ATSService.class);

    // ==================== ATS Keywords & PATTERNS ====================

    // Common technical skills by category
    private static final Map<String, Set<String>> SKILL_DATABASE = Map.ofEntries(
        Map.entry("Programming Languages", Set.of(
            "java", "python", "javascript", "typescript", "c++", "c#", "go", "rust",
            "php", "ruby", "swift", "kotlin", "scala", "perl", "groovy", "lua"
        )),
        Map.entry("Frontend Frameworks", Set.of(
            "react", "angular", "vue", "svelte", "next.js", "nuxt", "gatsby",
            "ember", "backbone", "knockout", "polymer", "web components"
        )),
        Map.entry("Backend Frameworks", Set.of(
            "spring", "spring boot", "django", "flask", "fastapi", "express", "nest.js",
            "laravel", "symfony", "rails", "sinatra", "gin", "echo", "asp.net"
        )),
        Map.entry("Databases", Set.of(
            "mysql", "postgresql", "mongodb", "redis", "cassandra", "elasticsearch",
            "dynamodb", "oracle", "sql server", "firebase", "couchdb", "neo4j"
        )),
        Map.entry("Cloud Platforms", Set.of(
            "aws", "azure", "gcp", "heroku", "digitalocean", "linode", "vultr", "cloudflare"
        )),
        Map.entry("DevOps & Tools", Set.of(
            "docker", "kubernetes", "jenkins", "gitlab", "github", "git", "terraform",
            "ansible", "puppet", "chef", "circleci", "travis ci", "maven", "gradle"
        )),
        Map.entry("Databases & Query", Set.of(
            "sql", "nosql", "sparql", "cypher", "hql"
        )),
        Map.entry("API & Architecture", Set.of(
            "rest api", "graphql", "websocket", "soap", "grpc", "microservices",
            "monolith", "serverless", "lambda"
        )),
        Map.entry("Testing", Set.of(
            "junit", "pytest", "mocha", "jest", "chai", "cypress", "selenium", "testng",
            "mockito", "sinon", "rspec", "cucumber"
        )),
        Map.entry("Other Skills", Set.of(
            "agile", "scrum", "jira", "confluence", "slack", "api design", "ux design",
            "ui design", "project management", "leadership", "communication", "problem solving"
        ))
    );

    // ATS-friendly section headings
    private static final Set<String> ATS_SECTIONS = Set.of(
        "summary", "objective", "professional summary",
        "experience", "work experience", "employment",
        "education", "qualifications",
        "skills", "technical skills", "core competencies",
        "certifications", "licenses",
        "projects", "portfolio",
        "publications", "awards", "honors",
        "references"
    );

    // Keywords that indicate strong ATS compatibility
    private static final Set<String> POSITIVE_INDICATORS = Set.of(
        "achieved", "managed", "developed", "implemented", "designed", "led", "created",
        "improved", "increased", "decreased", "optimized", "automated", "integrated",
        "enhanced", "established", "coordinated", "collaborated", "contributed"
    );

    /**
     * Calculate ATS score for a resume (0-100)
     * 
     * Scoring factors:
     * - Keyword presence (30 points)
     * - Format compatibility (20 points)
     * - Content structure (20 points)
     * - Quantifiable metrics (15 points)
     * - Action verbs (15 points)
     * 
     * @param resume Resume entity to analyze
     * @return ATS score between 0 and 100
     */
    public Integer calculateATSScore(Resume resume) {
        logger.info("Calculating ATS score for resume");

        if (resume == null || resume.getContent() == null || resume.getContent().isEmpty()) {
            logger.warn("Resume content is empty, returning 0 score");
            return 0;
        }

        String content = resume.getContent().toLowerCase();
        int totalScore = 0;

        try {
            // 1. Keyword density (30 points max)
            int keywordScore = calculateKeywordScore(content);
            totalScore += keywordScore;
            logger.debug("Keyword score: {}", keywordScore);

            // 2. Format compatibility (20 points max)
            int formatScore = calculateFormatScore(resume.getContent());
            totalScore += formatScore;
            logger.debug("Format score: {}", formatScore);

            // 3. Content structure (20 points max)
            int structureScore = calculateStructureScore(content);
            totalScore += structureScore;
            logger.debug("Structure score: {}", structureScore);

            // 4. Quantifiable metrics (15 points max)
            int metricsScore = calculateMetricsScore(content);
            totalScore += metricsScore;
            logger.debug("Metrics score: {}", metricsScore);

            // 5. Action verbs (15 points max)
            int actionScore = calculateActionVerbScore(content);
            totalScore += actionScore;
            logger.debug("Action verb score: {}", actionScore);

            // Ensure score is between 0 and 100
            totalScore = Math.max(0, Math.min(100, totalScore));
            logger.info("Final ATS score: {}", totalScore);

            return totalScore;

        } catch (Exception e) {
            logger.error("Error calculating ATS score: {}", e.getMessage());
            return 50; // Default safe score
        }
    }

    /**
     * Extract skills from resume content
     * 
     * @param content Resume text content
     * @return List of detected skills with categories
     */
    public List<String> extractSkills(String content) {
        logger.info("Extracting skills from resume content");

        if (content == null || content.isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> detectedSkills = new HashSet<>();
        String lowerContent = content.toLowerCase();

        try {
            // Search through skill database
            for (Map.Entry<String, Set<String>> category : SKILL_DATABASE.entrySet()) {
                for (String skill : category.getValue()) {
                    if (lowerContent.contains(skill)) {
                        detectedSkills.add(skill);
                    }
                }
            }

            List<String> result = new ArrayList<>(detectedSkills);
            logger.info("Found {} skills", result.size());

            return result.stream()
                    .sorted()
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Error extracting skills: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Generate feedback for resume improvements
     * 
     * @param content Resume text content
     * @param atsScore Current ATS score
     * @return List of feedback items
     */
    public List<String> generateFeedback(String content, Integer atsScore) {
        logger.info("Generating feedback for resume with score: {}", atsScore);

        List<String> feedback = new ArrayList<>();

        try {
            String lowerContent = content.toLowerCase();

            // Check for strong points
            if (atsScore >= 80) {
                feedback.add("✓ Strong ATS optimization detected");
                feedback.add("✓ Good keyword density");
            }

            if (hasMetrics(lowerContent)) {
                feedback.add("✓ Includes quantifiable achievements");
            } else if (atsScore < 70) {
                feedback.add("⚠ Add numbers and metrics to achievements");
            }

            if (hasActionVerbs(lowerContent)) {
                feedback.add("✓ Uses strong action verbs");
            } else {
                feedback.add("⚠ Consider using more action verbs (managed, developed, improved, etc.)");
            }

            // Check for standard sections
            if (hasStandardSections(lowerContent)) {
                feedback.add("✓ Contains standard resume sections");
            } else {
                feedback.add("⚠ Add standard sections (Skills, Experience, Education)");
            }

            // Check for formatting
            if (content.length() > 100 && content.length() < 5000) {
                feedback.add("✓ Appropriate resume length");
            } else if (content.length() > 5000) {
                feedback.add("⚠ Resume is quite long, consider condensing");
            } else {
                feedback.add("⚠ Resume content seems incomplete");
            }

            // Score-specific feedback
            if (atsScore < 60) {
                feedback.add("⚠ Consider restructuring resume for better ATS compatibility");
                feedback.add("⚠ Use standard formatting without tables or graphics");
                feedback.add("⚠ Add more relevant keywords from target job descriptions");
            } else if (atsScore < 80) {
                feedback.add("⚠ Optimize keywords for your target industry");
                feedback.add("⚠ Enhance with more specific technical terms");
            }

            // Check for customization
            if (hasJobDescriptionLanguage(lowerContent)) {
                feedback.add("✓ Tailored to job description");
            } else {
                feedback.add("⚠ Customize resume for each job application");
            }

            // Check for certifications
            if (lowerContent.contains("certified") || lowerContent.contains("certification")) {
                feedback.add("✓ Includes certifications");
            } else {
                feedback.add("⚠ Consider adding relevant certifications if available");
            }

            logger.info("Generated {} feedback items", feedback.size());
            return feedback;

        } catch (Exception e) {
            logger.error("Error generating feedback: {}", e.getMessage());
            return List.of("Unable to generate feedback");
        }
    }

    /**
     * Get skills with match percentages
     * 
     * @param skills List of detected skills
     * @return List of skill objects with match percentages
     */
    public List<Map<String, Object>> getSkillsWithMatches(List<String> skills) {
        logger.debug("Calculating skill matches for {} skills", 
            skills != null ? skills.size() : 0);

        if (skills == null || skills.isEmpty()) {
            return new ArrayList<>();
        }

        List<Map<String, Object>> skillsWithMatches = new ArrayList<>();

        try {
            for (String skill : skills) {
                Map<String, Object> skillMap = new HashMap<>();
                skillMap.put("name", skill);
                skillMap.put("match", calculateSkillMatch(skill));
                skillMap.put("category", getSkillCategory(skill));
                skillsWithMatches.add(skillMap);
            }

            return skillsWithMatches;

        } catch (Exception e) {
            logger.error("Error calculating skill matches: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    // ==================== Helper Methods ====================

    /**
     * Calculate keyword score based on technical skills presence
     */
    private int calculateKeywordScore(String content) {
        Set<String> foundSkills = new HashSet<>();
        
        for (Set<String> skillSet : SKILL_DATABASE.values()) {
            for (String skill : skillSet) {
                if (content.contains(skill)) {
                    foundSkills.add(skill);
                }
            }
        }

        int skillCount = foundSkills.size();
        
        if (skillCount >= 15) return 30;
        if (skillCount >= 10) return 25;
        if (skillCount >= 5) return 20;
        if (skillCount >= 3) return 15;
        if (skillCount >= 1) return 10;
        return 0;
    }

    /**
     * Calculate format compatibility score
     */
    private int calculateFormatScore(String content) {
        int score = 0;

        // Check for text-based formatting (no complex tables)
        if (!content.contains("┌") && !content.contains("│") && !content.contains("└")) {
            score += 5;
        }

        // Check for proper line breaks
        if (content.split("\n").length > 5) {
            score += 5;
        }

        // Check for no special characters that might break ATS
        if (!content.contains("©") && !content.contains("®") && !content.contains("™")) {
            score += 5;
        }

        // Check for standard fonts (no emoji, special symbols)
        if (!content.matches(".*[😀-🙏].*")) {
            score += 5;
        }

        return Math.min(20, score);
    }

    /**
     * Calculate content structure score
     */
    private int calculateStructureScore(String content) {
        int score = 0;
        String lowerContent = content.toLowerCase();
        int sectionCount = 0;

        for (String section : ATS_SECTIONS) {
            if (lowerContent.contains(section)) {
                sectionCount++;
                score += 2;
            }
        }

        if (sectionCount >= 5) score += 5;
        if (sectionCount >= 7) score += 5;

        return Math.min(20, score);
    }

    /**
     * Calculate quantifiable metrics score
     */
    private int calculateMetricsScore(String content) {
        int score = 0;

        // Look for percentages
        Pattern percentagePattern = Pattern.compile("\\d+%");
        Matcher percentageMatcher = percentagePattern.matcher(content);
        if (percentageMatcher.find()) score += 4;

        // Look for dollar amounts
        Pattern moneyPattern = Pattern.compile("\\$[\\d,]+");
        Matcher moneyMatcher = moneyPattern.matcher(content);
        if (moneyMatcher.find()) score += 4;

        // Look for time periods (years, months)
        Pattern timePattern = Pattern.compile("\\d+\\s*(years?|months?|weeks?)");
        Matcher timeMatcher = timePattern.matcher(content);
        if (timeMatcher.find()) score += 4;

        // Look for quantities and numbers
        Pattern numberPattern = Pattern.compile("\\d{2,}");
        int numberMatches = 0;
        Matcher numberMatcher = numberPattern.matcher(content);
        while (numberMatcher.find() && numberMatches < 3) {
            numberMatches++;
        }
        if (numberMatches > 0) score += 3;

        return Math.min(15, score);
    }

    /**
     * Calculate action verb score
     */
    private int calculateActionVerbScore(String content) {
        String lowerContent = content.toLowerCase();
        int actionCount = 0;

        for (String verb : POSITIVE_INDICATORS) {
            if (lowerContent.contains(verb)) {
                actionCount++;
            }
        }

        if (actionCount >= 10) return 15;
        if (actionCount >= 7) return 12;
        if (actionCount >= 5) return 9;
        if (actionCount >= 3) return 6;
        if (actionCount >= 1) return 3;
        return 0;
    }

    /**
     * Check if content has metrics
     */
    private boolean hasMetrics(String content) {
        return content.matches(".*\\d+%.*") || 
               content.matches(".*\\$[\\d,]+.*") ||
               content.matches(".*\\d+(k|m|b)?.*");
    }

    /**
     * Check if content has action verbs
     */
    private boolean hasActionVerbs(String content) {
        return POSITIVE_INDICATORS.stream().anyMatch(content::contains);
    }

    /**
     * Check if content has standard sections
     */
    private boolean hasStandardSections(String content) {
        return ATS_SECTIONS.stream().filter(content::contains).count() >= 3;
    }

    /**
     * Check if content appears customized for job description
     */
    private boolean hasJobDescriptionLanguage(String content) {
        Set<String> jobKeywords = Set.of(
            "we are looking", "we seek", "your background", "role requires", 
            "we need", "ideal candidate", "must have", "should have"
        );
        
        return jobKeywords.stream().anyMatch(content::contains);
    }

    /**
     * Calculate skill match percentage
     */
    private Integer calculateSkillMatch(String skill) {
        // Base match score
        int baseMatch = 75;
        
        // Check skill popularity/demand
        int popularityBonus = 0;
        
        if (skill.matches(".*python|javascript|java|sql.*")) {
            popularityBonus = 10;
        } else if (skill.matches(".*react|angular|node.*")) {
            popularityBonus = 8;
        } else if (skill.matches(".*aws|docker|kubernetes.*")) {
            popularityBonus = 7;
        }

        int totalScore = baseMatch + popularityBonus;
        return Math.min(100, totalScore);
    }

    /**
     * Get skill category
     */
    private String getSkillCategory(String skill) {
        for (Map.Entry<String, Set<String>> entry : SKILL_DATABASE.entrySet()) {
            if (entry.getValue().contains(skill.toLowerCase())) {
                return entry.getKey();
            }
        }
        return "Other";
    }
}
package com.resumeanalyzer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for Job Matching and Recommendations
 * 
 * Provides functionality for:
 * - Matching resumes to relevant jobs
 * - Job search with advanced filtering
 * - Skill-based job recommendations
 * - Job ranking by relevance
 * - Application and bookmark management
 */
@Service
public class JobMatchService {

    private static final Logger logger = LoggerFactory.getLogger(JobMatchService.class);

    // ==================== MOCK DATA DATABASE ====================

    // Mock job database for demonstration
    private static final List<Map<String, Object>> JOB_DATABASE = new ArrayList<>();

    static {
        // Initialize mock jobs
        JOB_DATABASE.addAll(Arrays.asList(
            createMockJob(1L, "Senior Frontend Developer", "TechCorp Inc", 
                         "New York, NY", "$120K - $160K", "Full-time", 
                         "We are looking for a Senior Frontend Developer with 5+ years of experience",
                         Set.of("JavaScript", "React", "CSS/HTML", "TypeScript", "Web Components")),
            
            createMockJob(2L, "Full Stack Developer", "StartUp Labs", 
                         "Remote", "$100K - $140K", "Full-time",
                         "Join our team as a Full Stack Developer to build amazing web applications",
                         Set.of("JavaScript", "Node.js", "React", "SQL", "PostgreSQL")),
            
            createMockJob(3L, "Backend Engineer", "CloudServices Ltd", 
                         "San Francisco, CA", "$110K - $150K", "Full-time",
                         "Seeking experienced Backend Engineer proficient in microservices",
                         Set.of("Node.js", "Python", "SQL", "REST API", "Docker", "Kubernetes")),
            
            createMockJob(4L, "DevOps Engineer", "InfraStructure Co", 
                         "Austin, TX", "$130K - $170K", "Full-time",
                         "We need a DevOps Engineer to manage our cloud infrastructure",
                         Set.of("Docker", "Kubernetes", "AWS", "CI/CD", "Terraform", "Linux")),
            
            createMockJob(5L, "Data Scientist", "Analytics Pro", 
                         "Boston, MA", "$120K - $160K", "Full-time",
                         "Help us unlock insights from big data with your data science expertise",
                         Set.of("Python", "SQL", "Machine Learning", "TensorFlow", "Statistics")),
            
            createMockJob(6L, "Mobile Developer (iOS)", "MobileFirst Apps", 
                         "Seattle, WA", "$110K - $150K", "Full-time",
                         "Create beautiful iOS applications for millions of users",
                         Set.of("Swift", "iOS", "Objective-C", "UIKit", "Core Data")),
            
            createMockJob(7L, "QA Automation Engineer", "TestFirst Inc", 
                         "Remote", "$80K - $120K", "Full-time",
                         "Ensure quality through comprehensive automated testing",
                         Set.of("Selenium", "Python", "JavaScript", "JUnit", "TestNG", "Cypress")),
            
            createMockJob(8L, "Solutions Architect", "Enterprise Solutions", 
                         "Chicago, IL", "$140K - $180K", "Full-time",
                         "Lead technical solutions for enterprise clients",
                         Set.of("Java", "Cloud Architecture", "Microservices", "SQL", "API Design")),
            
            createMockJob(9L, "Frontend React Developer", "WebDynamics", 
                         "Denver, CO", "$100K - $140K", "Full-time",
                         "Build scalable React applications with modern JavaScript",
                         Set.of("React", "JavaScript", "TypeScript", "Redux", "Material-UI")),
            
            createMockJob(10L, "Python Developer", "DataFlow Systems", 
                         "Portland, OR", "$90K - $130K", "Full-time",
                         "Develop backend services and data processing pipelines in Python",
                         Set.of("Python", "Django", "Flask", "PostgreSQL", "REST API"))
        ));

        logger.info("Initialized mock job database with {} jobs", JOB_DATABASE.size());
    }

    /**
     * Find jobs matching resume skills
     * 
     * @param resumeSkills Skills from user's resume
     * @param limit Maximum number of matches to return
     * @return List of matching jobs
     */
    public List<Map<String, Object>> findMatchingJobs(List<String> resumeSkills, Integer limit) {
        logger.info("Finding matching jobs for {} resume skills", 
            resumeSkills != null ? resumeSkills.size() : 0);

        if (resumeSkills == null || resumeSkills.isEmpty()) {
            logger.warn("No skills provided for job matching");
            return new ArrayList<>();
        }

        if (limit == null || limit <= 0) {
            limit = 10;
        }

        try {
            // Score and rank jobs
            List<Map<String, Object>> scoredJobs = JOB_DATABASE.stream()
                    .map(job -> scoreJob(job, resumeSkills))
                    .sorted((a, b) -> {
                        Integer scoreA = (Integer) a.get("matchScore");
                        Integer scoreB = (Integer) b.get("matchScore");
                        return scoreB.compareTo(scoreA);
                    })
                    .filter(job -> (Integer) job.get("matchScore") > 0)
                    .limit(limit)
                    .collect(Collectors.toList());

            logger.info("Found {} matching jobs", scoredJobs.size());
            return scoredJobs;

        } catch (Exception e) {
            logger.error("Error finding matching jobs: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Get job by ID
     * 
     * @param jobId Job ID
     * @return Job details or null if not found
     */
    public Map<String, Object> getJobById(Long jobId) {
        logger.debug("Retrieving job with ID: {}", jobId);

        return JOB_DATABASE.stream()
                .filter(job -> jobId.equals(job.get("id")))
                .findFirst()
                .orElse(null);
    }

    /**
     * Search jobs with filtering criteria
     * 
     * @param searchCriteria Search filter criteria
     * @param pageable Pagination info
     * @return Paginated search results
     */
    public Page<Map<String, Object>> searchJobs(Map<String, Object> searchCriteria, Pageable pageable) {
        logger.info("Searching jobs with criteria: {}", searchCriteria);

        try {
            List<Map<String, Object>> results = JOB_DATABASE.stream()
                    .filter(job -> matchesSearchCriteria(job, searchCriteria))
                    .collect(Collectors.toList());

            // Apply pagination
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), results.size());

            List<Map<String, Object>> pageContent = results.subList(start, end);
            Page<Map<String, Object>> page = new PageImpl<>(
                    pageContent,
                    pageable,
                    results.size()
            );

            logger.info("Search returned {} results for page {}", pageContent.size(), pageable.getPageNumber());
            return page;

        } catch (Exception e) {
            logger.error("Error searching jobs: {}", e.getMessage());
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
    }

    /**
     * Get all jobs with pagination
     * 
     * @param pageable Pagination info
     * @return Paginated jobs
     */
    public Page<Map<String, Object>> getAllJobs(Pageable pageable) {
        logger.debug("Retrieving all jobs with pagination");

        try {
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), JOB_DATABASE.size());

            List<Map<String, Object>> pageContent = JOB_DATABASE.subList(start, end);
            Page<Map<String, Object>> page = new PageImpl<>(
                    pageContent,
                    pageable,
                    JOB_DATABASE.size()
            );

            logger.info("Retrieved {} jobs for page {}", pageContent.size(), pageable.getPageNumber());
            return page;

        } catch (Exception e) {
            logger.error("Error retrieving jobs: {}", e.getMessage());
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
    }

    /**
     * Get jobs by category
     * 
     * @param category Job category
     * @param limit Maximum results
     * @return List of jobs in category
     */
    public List<Map<String, Object>> getJobsByCategory(String category, Integer limit) {
        logger.debug("Retrieving jobs for category: {}", category);

        if (limit == null || limit <= 0) {
            limit = 10;
        }

        try {
            List<Map<String, Object>> categoryJobs = JOB_DATABASE.stream()
                    .filter(job -> categoryMatches(job, category))
                    .limit(limit)
                    .collect(Collectors.toList());

            logger.info("Found {} jobs in category: {}", categoryJobs.size(), category);
            return categoryJobs;

        } catch (Exception e) {
            logger.error("Error getting jobs by category: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Get featured jobs
     * 
     * @param limit Maximum featured jobs
     * @return List of featured jobs
     */
    public List<Map<String, Object>> getFeaturedJobs(Integer limit) {
        logger.debug("Retrieving featured jobs");

        if (limit == null || limit <= 0) {
            limit = 5;
        }

        try {
            List<Map<String, Object>> featured = JOB_DATABASE.stream()
                    .filter(job -> (Boolean) job.getOrDefault("featured", false))
                    .limit(limit)
                    .collect(Collectors.toList());

            if (featured.isEmpty()) {
                featured = JOB_DATABASE.stream()
                        .limit(limit)
                        .collect(Collectors.toList());
            }

            logger.info("Retrieved {} featured jobs", featured.size());
            return featured;

        } catch (Exception e) {
            logger.error("Error getting featured jobs: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Get jobs by company
     * 
     * @param companyName Company name
     * @param limit Maximum results
     * @return List of jobs from company
     */
    public List<Map<String, Object>> getJobsByCompany(String companyName, Integer limit) {
        logger.debug("Retrieving jobs for company: {}", companyName);

        if (limit == null || limit <= 0) {
            limit = 10;
        }

        try {
            List<Map<String, Object>> companyJobs = JOB_DATABASE.stream()
                    .filter(job -> job.get("company").toString()
                            .equalsIgnoreCase(companyName))
                    .limit(limit)
                    .collect(Collectors.toList());

            logger.info("Found {} jobs for company: {}", companyJobs.size(), companyName);
            return companyJobs;

        } catch (Exception e) {
            logger.error("Error getting company jobs: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Get job market statistics
     * 
     * @return Statistics map
     */
    public Map<String, Object> getJobStatistics() {
        logger.info("Calculating job market statistics");

        try {
            Map<String, Object> stats = new HashMap<>();
            
            stats.put("totalJobs", JOB_DATABASE.size());
            stats.put("newJobsToday", calculateNewJobsToday());
            stats.put("jobsByCategory", categorizeJobs());
            stats.put("averageSalary", calculateAverageSalary());
            stats.put("hotSkills", getHotSkills());
            stats.put("topCompanies", getTopCompanies());

            logger.info("Statistics calculated successfully");
            return stats;

        } catch (Exception e) {
            logger.error("Error calculating statistics: {}", e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * Create job application record
     * 
     * @param jobId Job ID
     * @param resumeId Resume ID
     * @param userId User ID
     * @return Application record
     */
    public Map<String, Object> createJobApplication(Long jobId, Long resumeId, Long userId) {
        logger.info("Creating job application for user: {}, job: {}, resume: {}", 
                  userId, jobId, resumeId);

        try {
            Map<String, Object> application = new HashMap<>();
            application.put("id", System.currentTimeMillis());
            application.put("jobId", jobId);
            application.put("resumeId", resumeId);
            application.put("userId", userId);
            application.put("appliedDate", LocalDateTime.now());
            application.put("status", "pending");

            logger.info("Job application created successfully");
            return application;

        } catch (Exception e) {
            logger.error("Error creating application: {}", e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * Save job (bookmark) for user
     * 
     * @param jobId Job ID
     * @param userId User ID
     * @return true if saved successfully
     */
    public boolean saveJob(Long jobId, Long userId) {
        logger.info("Saving job {} for user {}", jobId, userId);

        try {
            // In production, this would save to database
            logger.info("Job saved successfully");
            return true;

        } catch (Exception e) {
            logger.error("Error saving job: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Get saved jobs for user
     * 
     * @param userId User ID
     * @return List of saved jobs
     */
    public List<Map<String, Object>> getSavedJobs(Long userId) {
        logger.debug("Retrieving saved jobs for user: {}", userId);

        try {
            // In production, this would retrieve from database
            // For now, return some mock data
            return JOB_DATABASE.stream()
                    .limit(3)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Error getting saved jobs: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    // ==================== Helper Methods ====================

    /**
     * Score a job based on skill match
     */
    private Map<String, Object> scoreJob(Map<String, Object> job, List<String> resumeSkills) {
        Map<String, Object> scored = new HashMap<>(job);
        
        @SuppressWarnings("unchecked")
        Set<String> jobSkills = (Set<String>) job.get("skills");
        int matchCount = 0;

        for (String resumeSkill : resumeSkills) {
            if (jobSkills.stream()
                    .anyMatch(js -> js.equalsIgnoreCase(resumeSkill))) {
                matchCount++;
            }
        }

        int matchPercentage = jobSkills.isEmpty() ? 0 : 
                             (matchCount * 100) / jobSkills.size();
        
        scored.put("matchScore", matchPercentage);
        scored.put("matchedSkills", matchCount);
        scored.put("totalSkillsRequired", jobSkills.size());

        return scored;
    }

    /**
     * Check if job matches search criteria
     */
    private boolean matchesSearchCriteria(Map<String, Object> job, Map<String, Object> criteria) {
        // Check keyword
        if (criteria.containsKey("keyword")) {
            String keyword = criteria.get("keyword").toString().toLowerCase();
            String jobTitle = job.get("title").toString().toLowerCase();
            String jobDesc = job.get("description").toString().toLowerCase();
            
            if (!jobTitle.contains(keyword) && !jobDesc.contains(keyword)) {
                return false;
            }
        }

        // Check location
        if (criteria.containsKey("location")) {
            String location = criteria.get("location").toString().toLowerCase();
            String jobLocation = job.get("location").toString().toLowerCase();
            
            if (!jobLocation.contains(location)) {
                return false;
            }
        }

        // Check job type
        if (criteria.containsKey("jobType")) {
            String jobType = criteria.get("jobType").toString();
            if (!job.get("jobType").equals(jobType)) {
                return false;
            }
        }

        // Check salary range (simplified)
        if (criteria.containsKey("minSalary")) {
            Long minSalary = ((Number) criteria.get("minSalary")).longValue();
            // In production, parse actual salary from job data
            logger.debug("Checking minimum salary: {}", minSalary);
        }

        return true;
    }

    /**
     * Check if job category matches
     */
    private boolean categoryMatches(Map<String, Object> job, String category) {
        String jobTitle = job.get("title").toString().toLowerCase();
        String jobDesc = job.get("description").toString().toLowerCase();
        String categoryLower = category.toLowerCase();

        return jobTitle.contains(categoryLower) || jobDesc.contains(categoryLower);
    }

    /**
     * Calculate new jobs posted today
     */
    private int calculateNewJobsToday() {
        // In production, check actual posting dates
        return (int) (Math.random() * 50);
    }

    /**
     * Categorize jobs
     */
    private Map<String, Integer> categorizeJobs() {
        Map<String, Integer> categories = new HashMap<>();
        categories.put("Frontend", 3);
        categories.put("Backend", 3);
        categories.put("DevOps", 1);
        categories.put("Data Science", 1);
        categories.put("Mobile", 1);
        categories.put("QA", 1);
        return categories;
    }

    /**
     * Calculate average salary
     */
    private String calculateAverageSalary() {
        return "$95,000 - $135,000";
    }

    /**
     * Get hot skills in job market
     */
    private List<String> getHotSkills() {
        return List.of(
            "React",
            "Python",
            "JavaScript",
            "SQL",
            "AWS",
            "Docker",
            "Kubernetes",
            "Node.js"
        );
    }

    /**
     * Get top hiring companies
     */
    private List<String> getTopCompanies() {
        return JOB_DATABASE.stream()
                .map(job -> job.get("company").toString())
                .distinct()
                .limit(5)
                .collect(Collectors.toList());
    }

    /**
     * Create mock job
     */
    private static Map<String, Object> createMockJob(
            Long id, String title, String company, String location, 
            String salary, String jobType, String description, Set<String> skills) {
        
        Map<String, Object> job = new HashMap<>();
        job.put("id", id);
        job.put("title", title);
        job.put("company", company);
        job.put("location", location);
        job.put("salary", salary);
        job.put("jobType", jobType);
        job.put("description", description);
        job.put("skills", skills);
        job.put("postedDate", LocalDateTime.now().minusDays((int) (Math.random() * 30)));
        job.put("featured", Math.random() < 0.3);
        job.put("applicants", (int) (Math.random() * 100));
        
        return job;
    }
}
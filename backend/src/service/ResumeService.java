package com.resumeanalyzer.service;

import com.resumeanalyzer.model.Resume;
import com.resumeanalyzer.repository.ResumeRepository;
import org.apache.pdfbox.pdfdocument.PDFDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdfpage.PDFPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Service for Resume Management
 * 
 * Handles all resume-related operations:
 * - File upload and storage
 * - Resume parsing from various formats
 * - Resume retrieval and management
 * - File system operations
 */
@Service
public class ResumeService {

    private static final Logger logger = LoggerFactory.getLogger(ResumeService.class);

    @Autowired
    private ResumeRepository resumeRepository;

    @Value("${resume.upload.dir:uploads/resumes}")
    private String uploadDirectory;

    @Value("${resume.max.size:5242880}")
    private long maxFileSize;

    /**
     * Upload and parse a resume file
     * 
     * @param file Multipart resume file
     * @param userId User ID associated with resume
     * @return Resume entity with parsed content
     * @throws IOException If file processing fails
     */
    public Resume uploadAndAnalyze(MultipartFile file, Long userId) throws IOException {
        logger.info("Starting resume upload for user: {}", userId);

        try {
            // Validate file
            if (file.isEmpty()) {
                throw new IllegalArgumentException("File cannot be empty");
            }

            if (file.getSize() > maxFileSize) {
                throw new IllegalArgumentException("File size exceeds maximum limit of " + maxFileSize + " bytes");
            }

            // Create Resume entity
            Resume resume = new Resume();
            resume.setUserId(userId);
            resume.setFileName(sanitizeFileName(file.getOriginalFilename()));
            resume.setFileSize(file.getSize());
            resume.setContentType(file.getContentType());

            // Parse file content
            String content = parseFile(file);
            resume.setContent(content);

            // Save file to disk
            String filePath = saveFileToDisk(file, userId);
            resume.setFilePath(filePath);

            // Set timestamps
            resume.setCreatedAt(LocalDateTime.now());
            resume.setUpdatedAt(LocalDateTime.now());

            logger.info("Resume successfully parsed and prepared for saving");
            return resume;

        } catch (IOException e) {
            logger.error("Error processing resume file: {}", e.getMessage());
            throw new IOException("Failed to process resume file: " + e.getMessage());
        }
    }

    /**
     * Save resume to database
     * 
     * @param resume Resume entity to save
     * @return Saved resume with ID
     */
    public Resume saveResume(Resume resume) {
        logger.info("Saving resume to database for user: {}", resume.getUserId());
        
        try {
            Resume saved = resumeRepository.save(resume);
            logger.info("Resume saved successfully with ID: {}", saved.getId());
            return saved;
        } catch (Exception e) {
            logger.error("Error saving resume to database: {}", e.getMessage());
            throw new RuntimeException("Failed to save resume: " + e.getMessage());
        }
    }

    /**
     * Get resume by ID
     * 
     * @param id Resume ID
     * @return Optional containing resume if found
     */
    public Optional<Resume> getResumeById(Long id) {
        logger.debug("Retrieving resume with ID: {}", id);
        
        try {
            return resumeRepository.findById(id);
        } catch (Exception e) {
            logger.error("Error retrieving resume: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Get all resumes for a user
     * 
     * @param userId User ID
     * @return List of user's resumes
     */
    public List<Resume> getUserResumes(Long userId) {
        logger.debug("Retrieving all resumes for user: {}", userId);
        
        try {
            List<Resume> resumes = resumeRepository.findByUserIdOrderByCreatedAtDesc(userId);
            logger.info("Found {} resumes for user: {}", resumes.size(), userId);
            return resumes;
        } catch (Exception e) {
            logger.error("Error retrieving user resumes: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Delete resume by ID
     * 
     * @param id Resume ID
     * @return true if deletion successful
     */
    public boolean deleteResume(Long id) {
        logger.info("Deleting resume with ID: {}", id);
        
        try {
            Optional<Resume> resume = resumeRepository.findById(id);
            
            if (resume.isEmpty()) {
                logger.warn("Resume not found for deletion: {}", id);
                return false;
            }

            // Delete file from disk
            if (resume.get().getFilePath() != null) {
                deleteFileFromDisk(resume.get().getFilePath());
            }

            // Delete from database
            resumeRepository.deleteById(id);
            logger.info("Resume deleted successfully: {}", id);
            return true;

        } catch (Exception e) {
            logger.error("Error deleting resume: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Update resume analysis data
     * 
     * @param resumeId Resume ID to update
     * @param atsScore ATS score
     * @param skills List of detected skills
     * @param feedback List of feedback items
     */
    public void updateResumeAnalysis(Long resumeId, Integer atsScore, 
                                     List<String> skills, List<String> feedback) {
        logger.info("Updating analysis for resume: {}", resumeId);
        
        try {
            Optional<Resume> resume = resumeRepository.findById(resumeId);
            
            if (resume.isEmpty()) {
                logger.warn("Resume not found for update: {}", resumeId);
                return;
            }

            Resume updatedResume = resume.get();
            updatedResume.setAtsScore(atsScore);
            updatedResume.setSkills(skills);
            updatedResume.setFeedback(feedback);
            updatedResume.setUpdatedAt(LocalDateTime.now());

            resumeRepository.save(updatedResume);
            logger.info("Resume analysis updated successfully");

        } catch (Exception e) {
            logger.error("Error updating resume analysis: {}", e.getMessage());
        }
    }

    /**
     * Check if resume exists
     * 
     * @param id Resume ID
     * @return true if resume exists
     */
    public boolean resumeExists(Long id) {
        return resumeRepository.existsById(id);
    }

    /**
     * Get total resume count for a user
     * 
     * @param userId User ID
     * @return Number of resumes
     */
    public long getUserResumeCount(Long userId) {
        return resumeRepository.countByUserId(userId);
    }

    // ==================== Helper Methods ====================

    /**
     * Parse file based on content type
     */
    private String parseFile(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        
        if (contentType == null) {
            throw new IllegalArgumentException("Unable to determine file type");
        }

        logger.debug("Parsing file with content type: {}", contentType);

        if (contentType.equals("application/pdf")) {
            return parsePDF(file);
        } else if (contentType.contains("word") || 
                   contentType.equals("application/msword") ||
                   contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
            return parseDOCX(file);
        } else if (contentType.equals("text/plain")) {
            return parseTXT(file);
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + contentType);
        }
    }

    /**
     * Parse PDF file
     */
    private String parsePDF(MultipartFile file) throws IOException {
        logger.debug("Parsing PDF file: {}", file.getOriginalFilename());
        
        try (InputStream inputStream = file.getInputStream();
             PDFParser pdfParser = new PDFParser(inputStream);
             PDFDocument pdfDocument = pdfParser.getPDDocument()) {

            PDFTextStripper textStripper = new PDFTextStripper();
            String text = textStripper.getText(pdfDocument);
            
            logger.debug("PDF parsing completed, extracted {} characters", text.length());
            return text.trim();

        } catch (Exception e) {
            logger.error("Error parsing PDF: {}", e.getMessage());
            throw new IOException("Failed to parse PDF: " + e.getMessage());
        }
    }

    /**
     * Parse DOCX file
     */
    private String parseDOCX(MultipartFile file) throws IOException {
        logger.debug("Parsing DOCX file: {}", file.getOriginalFilename());
        
        try (InputStream inputStream = file.getInputStream();
             XWPFDocument document = new XWPFDocument(inputStream)) {

            StringBuilder text = new StringBuilder();
            
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                text.append(paragraph.getText()).append("\n");
            }

            logger.debug("DOCX parsing completed, extracted {} characters", text.length());
            return text.toString().trim();

        } catch (Exception e) {
            logger.error("Error parsing DOCX: {}", e.getMessage());
            throw new IOException("Failed to parse DOCX: " + e.getMessage());
        }
    }

    /**
     * Parse TXT file
     */
    private String parseTXT(MultipartFile file) throws IOException {
        logger.debug("Parsing TXT file: {}", file.getOriginalFilename());
        
        try (InputStream inputStream = file.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            StringBuilder text = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                text.append(line).append("\n");
            }

            logger.debug("TXT parsing completed, extracted {} characters", text.length());
            return text.toString().trim();

        } catch (Exception e) {
            logger.error("Error parsing TXT: {}", e.getMessage());
            throw new IOException("Failed to parse TXT: " + e.getMessage());
        }
    }

    /**
     * Save file to disk
     */
    private String saveFileToDisk(MultipartFile file, Long userId) throws IOException {
        try {
            // Create directory if not exists
            Path uploadPath = Paths.get(uploadDirectory, userId.toString());
            Files.createDirectories(uploadPath);

            // Generate unique filename
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);

            // Save file
            Files.write(filePath, file.getBytes());
            
            logger.info("File saved to disk: {}", filePath);
            return filePath.toString();

        } catch (IOException e) {
            logger.error("Error saving file to disk: {}", e.getMessage());
            throw new IOException("Failed to save file: " + e.getMessage());
        }
    }

    /**
     * Delete file from disk
     */
    private void deleteFileFromDisk(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                Files.delete(path);
                logger.info("File deleted from disk: {}", filePath);
            }
        } catch (IOException e) {
            logger.warn("Error deleting file from disk: {}", e.getMessage());
        }
    }

    /**
     * Sanitize filename to prevent security issues
     */
    private String sanitizeFileName(String filename) {
        if (filename == null) {
            return "resume_" + System.currentTimeMillis();
        }
        return filename.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    /**
     * Get file content as string
     */
    public String getFileContent(String filePath) throws IOException {
        try {
            if (filePath == null || !Files.exists(Paths.get(filePath))) {
                throw new FileNotFoundException("File not found: " + filePath);
            }
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            logger.error("Error reading file: {}", e.getMessage());
            throw e;
        }
    }
}
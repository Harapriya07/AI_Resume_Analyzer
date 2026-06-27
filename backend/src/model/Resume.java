package com.resumeanalyzer.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Resume Entity Model
 * 
 * Represents a user's resume with all related information
 * including file details, content, and analysis results
 */
@Entity
@Table(name = "resumes")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "ats_score")
    private Integer atsScore;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "resume_skills", joinColumns = @JoinColumn(name = "resume_id"))
    @Column(name = "skill")
    private List<String> skills;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "resume_feedback", joinColumns = @JoinColumn(name = "resume_id"))
    @Column(name = "feedback", columnDefinition = "TEXT")
    private List<String> feedback;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ==================== Constructors ====================

    public Resume() {
    }

    public Resume(Long userId, String fileName) {
        this.userId = userId;
        this.fileName = fileName;
    }

    public Resume(Long userId, String fileName, String content) {
        this.userId = userId;
        this.fileName = fileName;
        this.content = content;
    }

    // ==================== Getters and Setters ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getAtsScore() {
        return atsScore;
    }

    public void setAtsScore(Integer atsScore) {
        this.atsScore = atsScore;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getFeedback() {
        return feedback;
    }

    public void setFeedback(List<String> feedback) {
        this.feedback = feedback;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // ==================== Utility Methods ====================

    /**
     * Get resume rating based on ATS score
     */
    public String getRating() {
        if (atsScore == null) return "Unknown";
        if (atsScore >= 80) return "Excellent";
        if (atsScore >= 60) return "Good";
        return "Needs Improvement";
    }

    /**
     * Get resume score percentage
     */
    public Integer getScorePercentage() {
        return atsScore;
    }

    /**
     * Check if resume has been analyzed
     */
    public boolean isAnalyzed() {
        return atsScore != null && !skills.isEmpty();
    }

    /**
     * Get skill count
     */
    public int getSkillCount() {
        return skills != null ? skills.size() : 0;
    }

    /**
     * Get feedback count
     */
    public int getFeedbackCount() {
        return feedback != null ? feedback.size() : 0;
    }

    /**
     * Get file size in MB
     */
    public double getFileSizeInMB() {
        if (fileSize == null) return 0;
        return fileSize / (1024.0 * 1024.0);
    }

    // ==================== toString and equals ====================

    @Override
    public String toString() {
        return "Resume{" +
                "id=" + id +
                ", userId=" + userId +
                ", fileName='" + fileName + '\'' +
                ", atsScore=" + atsScore +
                ", skillCount=" + getSkillCount() +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;

        if (id != null ? !id.equals(resume.id) : resume.id != null) return false;
        if (userId != null ? !userId.equals(resume.userId) : resume.userId != null) return false;
        return fileName != null ? fileName.equals(resume.fileName) : resume.fileName == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        return result;
    }
}
package com.resumeanalyzer.repository;

import com.resumeanalyzer.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Resume Entity
 * 
 * Provides database access methods for Resume operations
 * Extends JpaRepository for CRUD operations
 */
@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {

    /**
     * Find all resumes for a specific user, ordered by creation date
     * 
     * @param userId User ID
     * @return List of resumes ordered by creation date (newest first)
     */
    List<Resume> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * Find all resumes for a user, ordered by ATS score (highest first)
     * 
     * @param userId User ID
     * @return List of resumes ordered by ATS score
     */
    List<Resume> findByUserIdOrderByAtsScoreDesc(Long userId);

    /**
     * Count total resumes for a user
     * 
     * @param userId User ID
     * @return Number of resumes
     */
    long countByUserId(Long userId);

    /**
     * Find resumes by user within a date range
     * 
     * @param userId User ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of resumes created in date range
     */
    List<Resume> findByUserIdAndCreatedAtBetween(
            Long userId, 
            LocalDateTime startDate, 
            LocalDateTime endDate
    );

    /**
     * Find resumes by ATS score range
     * 
     * @param minScore Minimum ATS score
     * @param maxScore Maximum ATS score
     * @return List of resumes with scores in range
     */
    List<Resume> findByAtsScoreBetween(Integer minScore, Integer maxScore);

    /**
     * Find resumes by file name (search)
     * 
     * @param fileName File name to search
     * @return List of resumes matching file name
     */
    List<Resume> findByFileNameContainingIgnoreCase(String fileName);

    /**
     * Custom query to find high-scoring resumes
     * 
     * @return List of resumes with ATS score >= 80
     */
    @Query("SELECT r FROM Resume r WHERE r.atsScore >= 80 ORDER BY r.atsScore DESC")
    List<Resume> findHighScoringResumes();

    /**
     * Custom query to find resumes with specific skills
     * 
     * @param skill Skill to search for
     * @return List of resumes containing the skill
     */
    @Query("SELECT r FROM Resume r WHERE LOWER(r.content) LIKE LOWER(CONCAT('%', :skill, '%'))")
    List<Resume> findBySkill(@Param("skill") String skill);

    /**
     * Check if resume exists for user
     * 
     * @param userId User ID
     * @param fileName File name
     * @return true if resume exists
     */
    boolean existsByUserIdAndFileName(Long userId, String fileName);

    /**
     * Find most recent resume for user
     * 
     * @param userId User ID
     * @return Optional containing most recent resume
     */
    Optional<Resume> findFirstByUserIdOrderByCreatedAtDesc(Long userId);
}
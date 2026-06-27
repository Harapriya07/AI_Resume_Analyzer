package com.resumeanalyzer.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * User Entity Model
 * 
 * Represents a user account in the AI Resume Analyzer system
 * Includes user profile, authentication details, preferences,
 * and relationships with resume and application data
 */
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email"),
    @UniqueConstraint(columnNames = "username")
})
public class User {

    // ==================== PRIMARY KEY ====================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ==================== AUTHENTICATION FIELDS ====================

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    // ==================== PERSONAL INFORMATION ====================

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "profile_picture_url", length = 500)
    private String profilePictureUrl;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    // ==================== PROFESSIONAL INFORMATION ====================

    @Column(name = "job_title", length = 100)
    private String jobTitle;

    @Column(name = "company", length = 100)
    private String company;

    @Column(name = "industry", length = 100)
    private String industry;

    @Column(name = "location", length = 200)
    private String location;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    // ==================== ACCOUNT STATUS ====================

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "verification_token", length = 255)
    private String verificationToken;

    @Column(name = "verification_token_expiry")
    private LocalDateTime verificationTokenExpiry;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", length = 20)
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", length = 20)
    private UserRole userRole = UserRole.USER;

    // ==================== ACCOUNT SECURITY ====================

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "last_password_change")
    private LocalDateTime lastPasswordChange;

    @Column(name = "password_reset_token", length = 255)
    private String passwordResetToken;

    @Column(name = "password_reset_token_expiry")
    private LocalDateTime passwordResetTokenExpiry;

    @Column(name = "failed_login_attempts")
    private Integer failedLoginAttempts = 0;

    @Column(name = "account_locked_until")
    private LocalDateTime accountLockedUntil;

    @Column(name = "two_factor_enabled")
    private Boolean twoFactorEnabled = false;

    @Column(name = "two_factor_secret", length = 255)
    private String twoFactorSecret;

    // ==================== USER PREFERENCES ====================

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_theme", length = 20)
    private Theme preferredTheme = Theme.LIGHT;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_language", length = 20)
    private Language preferredLanguage = Language.ENGLISH;

    @Column(name = "email_notifications")
    private Boolean emailNotifications = true;

    @Column(name = "push_notifications")
    private Boolean pushNotifications = true;

    @Column(name = "marketing_emails")
    private Boolean marketingEmails = false;

    @Column(name = "newsletter_subscribed")
    private Boolean newsletterSubscribed = false;

    // ==================== JOB PREFERENCES ====================

    @Column(name = "preferred_job_types", length = 500)
    private String preferredJobTypes; // Comma-separated

    @Column(name = "preferred_locations", length = 500)
    private String preferredLocations; // Comma-separated

    @Column(name = "preferred_salary_min")
    private Long preferredSalaryMin;

    @Column(name = "preferred_salary_max")
    private Long preferredSalaryMax;

    @Column(name = "willing_to_relocate")
    private Boolean willingToRelocate = false;

    @Column(name = "willing_remote")
    private Boolean willingRemote = true;

    // ==================== SUBSCRIPTION & BILLING ====================

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_plan", length = 20)
    private SubscriptionPlan subscriptionPlan = SubscriptionPlan.FREE;

    @Column(name = "subscription_start_date")
    private LocalDateTime subscriptionStartDate;

    @Column(name = "subscription_end_date")
    private LocalDateTime subscriptionEndDate;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "billing_email", length = 100)
    private String billingEmail;

    // ==================== STATISTICS ====================

    @Column(name = "total_resumes_analyzed")
    private Integer totalResumesAnalyzed = 0;

    @Column(name = "total_jobs_viewed")
    private Integer totalJobsViewed = 0;

    @Column(name = "total_applications")
    private Integer totalApplications = 0;

    @Column(name = "average_ats_score")
    private Double averageAtsScore = 0.0;

    // ==================== TIMESTAMPS ====================

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // ==================== RELATIONSHIPS ====================

    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Resume> resumes = new HashSet<>();

    // ==================== ENUMS ====================

    public enum AccountStatus {
        ACTIVE, INACTIVE, SUSPENDED, DELETED
    }

    public enum UserRole {
        USER, ADMIN, PREMIUM_USER, MODERATOR
    }

    public enum Theme {
        LIGHT, DARK, AUTO
    }

    public enum Language {
        ENGLISH, SPANISH, FRENCH, GERMAN, CHINESE, JAPANESE
    }

    public enum SubscriptionPlan {
        FREE, BASIC, PROFESSIONAL, ENTERPRISE
    }

    // ==================== CONSTRUCTORS ====================

    public User() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public User(String email, String username, String firstName, String lastName) {
        this();
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String email, String username, String password, String firstName, String lastName) {
        this(email, username, firstName, lastName);
        this.password = password;
    }

    // ==================== GETTERS AND SETTERS ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.lastPasswordChange = LocalDateTime.now();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean verified) {
        isVerified = verified;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public LocalDateTime getVerificationTokenExpiry() {
        return verificationTokenExpiry;
    }

    public void setVerificationTokenExpiry(LocalDateTime verificationTokenExpiry) {
        this.verificationTokenExpiry = verificationTokenExpiry;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public LocalDateTime getLastPasswordChange() {
        return lastPasswordChange;
    }

    public void setLastPasswordChange(LocalDateTime lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public LocalDateTime getPasswordResetTokenExpiry() {
        return passwordResetTokenExpiry;
    }

    public void setPasswordResetTokenExpiry(LocalDateTime passwordResetTokenExpiry) {
        this.passwordResetTokenExpiry = passwordResetTokenExpiry;
    }

    public Integer getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(Integer failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public LocalDateTime getAccountLockedUntil() {
        return accountLockedUntil;
    }

    public void setAccountLockedUntil(LocalDateTime accountLockedUntil) {
        this.accountLockedUntil = accountLockedUntil;
    }

    public Boolean getTwoFactorEnabled() {
        return twoFactorEnabled;
    }

    public void setTwoFactorEnabled(Boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }

    public String getTwoFactorSecret() {
        return twoFactorSecret;
    }

    public void setTwoFactorSecret(String twoFactorSecret) {
        this.twoFactorSecret = twoFactorSecret;
    }

    public Theme getPreferredTheme() {
        return preferredTheme;
    }

    public void setPreferredTheme(Theme preferredTheme) {
        this.preferredTheme = preferredTheme;
    }

    public Language getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(Language preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public Boolean getEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(Boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public Boolean getPushNotifications() {
        return pushNotifications;
    }

    public void setPushNotifications(Boolean pushNotifications) {
        this.pushNotifications = pushNotifications;
    }

    public Boolean getMarketingEmails() {
        return marketingEmails;
    }

    public void setMarketingEmails(Boolean marketingEmails) {
        this.marketingEmails = marketingEmails;
    }

    public Boolean getNewsletterSubscribed() {
        return newsletterSubscribed;
    }

    public void setNewsletterSubscribed(Boolean newsletterSubscribed) {
        this.newsletterSubscribed = newsletterSubscribed;
    }

    public String getPreferredJobTypes() {
        return preferredJobTypes;
    }

    public void setPreferredJobTypes(String preferredJobTypes) {
        this.preferredJobTypes = preferredJobTypes;
    }

    public String getPreferredLocations() {
        return preferredLocations;
    }

    public void setPreferredLocations(String preferredLocations) {
        this.preferredLocations = preferredLocations;
    }

    public Long getPreferredSalaryMin() {
        return preferredSalaryMin;
    }

    public void setPreferredSalaryMin(Long preferredSalaryMin) {
        this.preferredSalaryMin = preferredSalaryMin;
    }

    public Long getPreferredSalaryMax() {
        return preferredSalaryMax;
    }

    public void setPreferredSalaryMax(Long preferredSalaryMax) {
        this.preferredSalaryMax = preferredSalaryMax;
    }

    public Boolean getWillingToRelocate() {
        return willingToRelocate;
    }

    public void setWillingToRelocate(Boolean willingToRelocate) {
        this.willingToRelocate = willingToRelocate;
    }

    public Boolean getWillingRemote() {
        return willingRemote;
    }

    public void setWillingRemote(Boolean willingRemote) {
        this.willingRemote = willingRemote;
    }

    public SubscriptionPlan getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    public LocalDateTime getSubscriptionStartDate() {
        return subscriptionStartDate;
    }

    public void setSubscriptionStartDate(LocalDateTime subscriptionStartDate) {
        this.subscriptionStartDate = subscriptionStartDate;
    }

    public LocalDateTime getSubscriptionEndDate() {
        return subscriptionEndDate;
    }

    public void setSubscriptionEndDate(LocalDateTime subscriptionEndDate) {
        this.subscriptionEndDate = subscriptionEndDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getBillingEmail() {
        return billingEmail;
    }

    public void setBillingEmail(String billingEmail) {
        this.billingEmail = billingEmail;
    }

    public Integer getTotalResumesAnalyzed() {
        return totalResumesAnalyzed;
    }

    public void setTotalResumesAnalyzed(Integer totalResumesAnalyzed) {
        this.totalResumesAnalyzed = totalResumesAnalyzed;
    }

    public Integer getTotalJobsViewed() {
        return totalJobsViewed;
    }

    public void setTotalJobsViewed(Integer totalJobsViewed) {
        this.totalJobsViewed = totalJobsViewed;
    }

    public Integer getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(Integer totalApplications) {
        this.totalApplications = totalApplications;
    }

    public Double getAverageAtsScore() {
        return averageAtsScore;
    }

    public void setAverageAtsScore(Double averageAtsScore) {
        this.averageAtsScore = averageAtsScore;
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

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Set<Resume> getResumes() {
        return resumes;
    }

    public void setResumes(Set<Resume> resumes) {
        this.resumes = resumes;
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Get full name of user
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Check if account is active and not suspended
     */
    public boolean isAccountActive() {
        return isActive && accountStatus == AccountStatus.ACTIVE;
    }

    /**
     * Check if account is verified
     */
    public boolean isAccountVerified() {
        return isVerified && isActive;
    }

    /**
     * Check if account is locked due to failed login attempts
     */
    public boolean isAccountLocked() {
        if (accountLockedUntil == null) {
            return false;
        }
        return LocalDateTime.now().isBefore(accountLockedUntil);
    }

    /**
     * Check if subscription is active
     */
    public boolean isSubscriptionActive() {
        if (subscriptionPlan == SubscriptionPlan.FREE) {
            return true;
        }
        if (subscriptionEndDate == null) {
            return false;
        }
        return LocalDateTime.now().isBefore(subscriptionEndDate);
    }

    /**
     * Check if subscription is expired
     */
    public boolean isSubscriptionExpired() {
        if (subscriptionPlan == SubscriptionPlan.FREE) {
            return false;
        }
        if (subscriptionEndDate == null) {
            return true;
        }
        return LocalDateTime.now().isAfter(subscriptionEndDate);
    }

    /**
     * Check if password reset token is valid
     */
    public boolean isPasswordResetTokenValid() {
        if (passwordResetToken == null || passwordResetTokenExpiry == null) {
            return false;
        }
        return LocalDateTime.now().isBefore(passwordResetTokenExpiry);
    }

    /**
     * Check if verification token is valid
     */
    public boolean isVerificationTokenValid() {
        if (verificationToken == null || verificationTokenExpiry == null) {
            return false;
        }
        return LocalDateTime.now().isBefore(verificationTokenExpiry);
    }

    /**
     * Check if two-factor authentication is enabled and configured
     */
    public boolean isTwoFactorConfigured() {
        return twoFactorEnabled && twoFactorSecret != null && !twoFactorSecret.isEmpty();
    }

    /**
     * Check if user is premium
     */
    public boolean isPremium() {
        return subscriptionPlan != SubscriptionPlan.FREE && isSubscriptionActive();
    }

    /**
     * Increment failed login attempts
     */
    public void incrementFailedLoginAttempts() {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= 5) {
            this.accountLockedUntil = LocalDateTime.now().plusHours(1);
        }
    }

    /**
     * Reset failed login attempts
     */
    public void resetFailedLoginAttempts() {
        this.failedLoginAttempts = 0;
        this.accountLockedUntil = null;
        this.lastLogin = LocalDateTime.now();
    }

    /**
     * Record resume analysis
     */
    public void recordResumeAnalysis() {
        this.totalResumesAnalyzed++;
    }

    /**
     * Record job view
     */
    public void recordJobView() {
        this.totalJobsViewed++;
    }

    /**
     * Record job application
     */
    public void recordJobApplication() {
        this.totalApplications++;
    }

    /**
     * Update average ATS score
     */
    public void updateAverageAtsScore(Double newScore) {
        if (totalResumesAnalyzed == 0) {
            this.averageAtsScore = newScore;
        } else {
            this.averageAtsScore = (this.averageAtsScore * (totalResumesAnalyzed - 1) + newScore) / totalResumesAnalyzed;
        }
    }

    /**
     * Get account age in days
     */
    public long getAccountAgeDays() {
        return java.time.temporal.ChronoUnit.DAYS.between(createdAt, LocalDateTime.now());
    }

    /**
     * Check if user is new (created within last 7 days)
     */
    public boolean isNewUser() {
        return getAccountAgeDays() <= 7;
    }

    // ==================== toString and equals ====================

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", accountStatus=" + accountStatus +
                ", subscriptionPlan=" + subscriptionPlan +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        return username != null ? username.equals(user.username) : user.username == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }
}
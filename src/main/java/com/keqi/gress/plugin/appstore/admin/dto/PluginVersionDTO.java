package com.keqi.gress.plugin.appstore.admin.dto;

import java.time.LocalDateTime;

/**
 * Plugin Version DTO
 * 
 * @author Gress Team
 */
public class PluginVersionDTO {
    
    /**
     * Version ID
     */
    private Long id;
    
    /**
     * Plugin ID
     */
    private String pluginId;
    
    /**
     * Version Number
     */
    private String version;
    
    /**
     * Version Description/Changelog
     */
    private String description;
    
    /**
     * Release Notes
     */
    private String releaseNotes;
    
    /**
     * File Path
     */
    private String filePath;
    
    /**
     * File Size (bytes)
     */
    private Long fileSize;
    
    /**
     * File Hash
     */
    private String fileHash;
    
    /**
     * Status (PENDING/APPROVED/REJECTED/CURRENT/ARCHIVED)
     */
    private String status;
    
    /**
     * Is Current Version
     */
    private Boolean isCurrent;
    
    /**
     * Reviewer ID
     */
    private String reviewerId;
    
    /**
     * Reviewer Name
     */
    private String reviewerName;
    
    /**
     * Review Time
     */
    private LocalDateTime reviewTime;
    
    /**
     * Review Comment
     */
    private String reviewComment;
    
    /**
     * Create Time
     */
    private LocalDateTime createTime;
    
    /**
     * Update Time
     */
    private LocalDateTime updateTime;
    
    public PluginVersionDTO() {
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getPluginId() {
        return pluginId;
    }
    
    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getReleaseNotes() {
        return releaseNotes;
    }
    
    public void setReleaseNotes(String releaseNotes) {
        this.releaseNotes = releaseNotes;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
    public String getFileHash() {
        return fileHash;
    }
    
    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Boolean getIsCurrent() {
        return isCurrent;
    }
    
    public void setIsCurrent(Boolean isCurrent) {
        this.isCurrent = isCurrent;
    }
    
    public String getReviewerId() {
        return reviewerId;
    }
    
    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }
    
    public String getReviewerName() {
        return reviewerName;
    }
    
    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }
    
    public LocalDateTime getReviewTime() {
        return reviewTime;
    }
    
    public void setReviewTime(LocalDateTime reviewTime) {
        this.reviewTime = reviewTime;
    }
    
    public String getReviewComment() {
        return reviewComment;
    }
    
    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    public static class Builder {
        private Long id;
        private String pluginId;
        private String version;
        private String description;
        private String releaseNotes;
        private String filePath;
        private Long fileSize;
        private String fileHash;
        private String status;
        private Boolean isCurrent;
        private String reviewerId;
        private String reviewerName;
        private LocalDateTime reviewTime;
        private String reviewComment;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        
        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        
        public Builder pluginId(String pluginId) {
            this.pluginId = pluginId;
            return this;
        }
        
        public Builder version(String version) {
            this.version = version;
            return this;
        }
        
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        
        public Builder releaseNotes(String releaseNotes) {
            this.releaseNotes = releaseNotes;
            return this;
        }
        
        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }
        
        public Builder fileSize(Long fileSize) {
            this.fileSize = fileSize;
            return this;
        }
        
        public Builder fileHash(String fileHash) {
            this.fileHash = fileHash;
            return this;
        }
        
        public Builder status(String status) {
            this.status = status;
            return this;
        }
        
        public Builder isCurrent(Boolean isCurrent) {
            this.isCurrent = isCurrent;
            return this;
        }
        
        public Builder reviewerId(String reviewerId) {
            this.reviewerId = reviewerId;
            return this;
        }
        
        public Builder reviewerName(String reviewerName) {
            this.reviewerName = reviewerName;
            return this;
        }
        
        public Builder reviewTime(LocalDateTime reviewTime) {
            this.reviewTime = reviewTime;
            return this;
        }
        
        public Builder reviewComment(String reviewComment) {
            this.reviewComment = reviewComment;
            return this;
        }
        
        public Builder createTime(LocalDateTime createTime) {
            this.createTime = createTime;
            return this;
        }
        
        public Builder updateTime(LocalDateTime updateTime) {
            this.updateTime = updateTime;
            return this;
        }
        
        public PluginVersionDTO build() {
            PluginVersionDTO dto = new PluginVersionDTO();
            dto.id = this.id;
            dto.pluginId = this.pluginId;
            dto.version = this.version;
            dto.description = this.description;
            dto.releaseNotes = this.releaseNotes;
            dto.filePath = this.filePath;
            dto.fileSize = this.fileSize;
            dto.fileHash = this.fileHash;
            dto.status = this.status;
            dto.isCurrent = this.isCurrent;
            dto.reviewerId = this.reviewerId;
            dto.reviewerName = this.reviewerName;
            dto.reviewTime = this.reviewTime;
            dto.reviewComment = this.reviewComment;
            dto.createTime = this.createTime;
            dto.updateTime = this.updateTime;
            return dto;
        }
    }
}

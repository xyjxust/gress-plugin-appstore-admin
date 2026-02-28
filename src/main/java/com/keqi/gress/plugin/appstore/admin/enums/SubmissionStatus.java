package com.keqi.gress.plugin.appstore.admin.enums;

/**
 * Plugin Submission Status Enumeration
 */
public enum SubmissionStatus {
    /**
     * Pending review
     */
    PENDING,
    
    /**
     * Approved and listed
     */
    APPROVED,
    
    /**
     * Rejected
     */
    REJECTED,
    
    /**
     * Delisted (removed from store)
     */
    DELISTED
}

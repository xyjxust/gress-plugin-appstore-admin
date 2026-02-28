package com.keqi.gress.plugin.appstore.admin.dto;

/**
 * Rollback Version Request
 * 
 * @author Gress Team
 */
public class RollbackVersionRequest {
    
    /**
     * Operator ID
     */
    private String operatorId;
    
    /**
     * Operator Name
     */
    private String operatorName;
    
    /**
     * Rollback Reason
     */
    private String reason;
    
    /**
     * Notify Users
     */
    private Boolean notifyUsers;
    
    public RollbackVersionRequest() {
    }
    
    public RollbackVersionRequest(String operatorId, String operatorName, String reason, Boolean notifyUsers) {
        this.operatorId = operatorId;
        this.operatorName = operatorName;
        this.reason = reason;
        this.notifyUsers = notifyUsers;
    }
    
    public String getOperatorId() {
        return operatorId;
    }
    
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
    
    public String getOperatorName() {
        return operatorName;
    }
    
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public Boolean getNotifyUsers() {
        return notifyUsers;
    }
    
    public void setNotifyUsers(Boolean notifyUsers) {
        this.notifyUsers = notifyUsers;
    }
}

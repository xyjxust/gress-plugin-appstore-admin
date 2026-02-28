package com.keqi.gress.plugin.appstore.admin.dto;

/**
 * Set Current Version Request
 * 
 * @author Gress Team
 */
public class SetCurrentVersionRequest {
    
    /**
     * Operator ID
     */
    private String operatorId;
    
    /**
     * Operator Name
     */
    private String operatorName;
    
    /**
     * Comment
     */
    private String comment;
    
    public SetCurrentVersionRequest() {
    }
    
    public SetCurrentVersionRequest(String operatorId, String operatorName, String comment) {
        this.operatorId = operatorId;
        this.operatorName = operatorName;
        this.comment = comment;
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
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
}

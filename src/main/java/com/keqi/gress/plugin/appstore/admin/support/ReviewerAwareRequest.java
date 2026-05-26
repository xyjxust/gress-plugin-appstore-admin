package com.keqi.gress.plugin.appstore.admin.support;

public interface ReviewerAwareRequest {

    String getReviewerId();

    void setReviewerId(String reviewerId);

    String getReviewerName();

    void setReviewerName(String reviewerName);
}

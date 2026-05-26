package com.keqi.gress.plugin.appstore.admin.support;

import cn.hutool.core.util.StrUtil;

public final class RequestActorContextBinder {

    private RequestActorContextBinder() {
    }

    public static <T extends OperatorAwareRequest> T bindOperator(T request) {
        if (request == null) {
            return null;
        }
        if (StrUtil.isBlank(request.getOperatorId())) {
            request.setOperatorId(OperatorContextHelper.getOperatorId());
        }
        if (StrUtil.isBlank(request.getOperatorName())) {
            request.setOperatorName(OperatorContextHelper.getOperatorName());
        }
        return request;
    }

    public static <T extends ReviewerAwareRequest> T bindReviewer(T request) {
        if (request == null) {
            return null;
        }
        if (StrUtil.isBlank(request.getReviewerId())) {
            request.setReviewerId(OperatorContextHelper.getOperatorId());
        }
        if (StrUtil.isBlank(request.getReviewerName())) {
            request.setReviewerName(OperatorContextHelper.getOperatorName());
        }
        return request;
    }

    public static <T extends HandlerAwareRequest> T bindHandler(T request) {
        if (request == null) {
            return null;
        }
        if (StrUtil.isBlank(request.getHandlerId())) {
            request.setHandlerId(OperatorContextHelper.getOperatorId());
        }
        if (StrUtil.isBlank(request.getHandlerName())) {
            request.setHandlerName(OperatorContextHelper.getOperatorName());
        }
        return request;
    }
}

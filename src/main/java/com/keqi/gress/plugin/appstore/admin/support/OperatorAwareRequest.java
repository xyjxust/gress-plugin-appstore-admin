package com.keqi.gress.plugin.appstore.admin.support;

public interface OperatorAwareRequest {

    String getOperatorId();

    void setOperatorId(String operatorId);

    String getOperatorName();

    void setOperatorName(String operatorName);
}

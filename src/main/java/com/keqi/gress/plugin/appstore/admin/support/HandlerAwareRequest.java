package com.keqi.gress.plugin.appstore.admin.support;

public interface HandlerAwareRequest {

    String getHandlerId();

    void setHandlerId(String handlerId);

    String getHandlerName();

    void setHandlerName(String handlerName);
}

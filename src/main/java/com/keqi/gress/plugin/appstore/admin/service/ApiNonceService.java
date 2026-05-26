package com.keqi.gress.plugin.appstore.admin.service;

import com.keqi.gress.plugin.api.service.PluginLambdaDataSource;
import com.keqi.gress.plugin.appstore.admin.entity.AppStoreApiNonce;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class ApiNonceService {

    @Autowired
    private PluginLambdaDataSource dataSource;

    /**
     * 尝试写入 nonce（若重复则失败，表示重放）
     */
    public boolean tryConsume(String keyId, String nonce, LocalDateTime expireAt) {
        if (keyId == null || keyId.isBlank() || nonce == null || nonce.isBlank()) return false;
        try {
            AppStoreApiNonce entity = AppStoreApiNonce.builder()
                .keyId(keyId.trim())
                .nonce(nonce.trim())
                .expireAt(expireAt)
                .build();
            entity.setCreateTime(LocalDateTime.now());
            dataSource.insert(entity);
            return true;
        } catch (Exception e) {
            // 唯一索引冲突/DB异常都视作失败（重放或不可用）
            return false;
        }
    }

    public void cleanupExpired() {
        // 该数据表主要依赖 TTL（expire_at）做逻辑过期，清理属于可选优化。
    }
}

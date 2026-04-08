package com.keqi.gress.plugin.appstore.admin.controller;

import com.keqi.gress.common.model.Result;
import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.plugin.appstore.admin.dto.ActivateSigningKeyRequest;
import com.keqi.gress.plugin.appstore.admin.dto.GenerateSigningKeyRequest;
import com.keqi.gress.plugin.appstore.admin.dto.SigningKeyDTO;
import com.keqi.gress.plugin.appstore.admin.service.AppStoreSigningKeyService;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Signing key management for appstore-admin UI.
 */
@Service
@RestController
@RequestMapping("/signing-keys")
public class SigningKeyManagementController {

    private static final Log log = LogFactory.get(SigningKeyManagementController.class);

    @Inject(source = Inject.BeanSource.PLUGIN)
    private AppStoreSigningKeyService signingKeyService;

    @GetMapping
    public Result<List<SigningKeyDTO>> list() {

            return Result.success(signingKeyService.listSigningKeys());

    }

    /**
     * Download public key PEM string for a keyId.
     */
    @GetMapping("/{keyId}/public-key-pem")
    public Result<String> getPublicKeyPem(@PathVariable String keyId) {

            String pem = signingKeyService.getPublicKeyPemOrNull(keyId);
            if (pem == null || pem.isBlank()) {
                return Result.error("未找到公钥 PEM: keyId=" + keyId);
            }
            return Result.success(pem);

    }

    @PostMapping("/generate")
    public Result<SigningKeyDTO> generate(@RequestBody GenerateSigningKeyRequest request) {

            // TODO: operator from security context
            String operatorName = "admin";
            return signingKeyService.generateSigningKey(request, operatorName);

    }

    @PostMapping("/{keyId}/activate")
    public Result<Void> activate(
            @PathVariable String keyId,
            @RequestBody(required = false) ActivateSigningKeyRequest request) {

            // TODO: operator from security context
            String operatorName = "admin";
            return signingKeyService.activateKey(keyId, request, operatorName);

    }
}


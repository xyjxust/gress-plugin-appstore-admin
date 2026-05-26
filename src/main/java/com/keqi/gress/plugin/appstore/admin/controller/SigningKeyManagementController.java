package com.keqi.gress.plugin.appstore.admin.controller;

import com.keqi.gress.common.model.Result;
import com.keqi.gress.plugin.api.ui.annotation.PluginAction;
import com.keqi.gress.plugin.api.ui.annotation.PluginMenu;
import com.keqi.gress.plugin.appstore.admin.dto.ActivateSigningKeyRequest;
import com.keqi.gress.plugin.appstore.admin.dto.GenerateSigningKeyRequest;
import com.keqi.gress.plugin.appstore.admin.dto.SigningKeyDTO;
import com.keqi.gress.plugin.appstore.admin.service.AppStoreSigningKeyService;
import com.keqi.gress.plugin.appstore.admin.support.OperatorContextHelper;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Signing key management for as-admin UI.
 */
@Service
@RestController
@RequestMapping("/signing-keys")
@PluginMenu(id = "signing-keys", name = "签名密钥管理", managementEnabled = true)
public class SigningKeyManagementController {

    private static final Log log = LogFactory.get(SigningKeyManagementController.class);

    @Autowired
    private AppStoreSigningKeyService signingKeyService;

    @GetMapping
    @PluginAction(id = "refresh", name = "刷新")
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
    @PluginAction(id = "generate", name = "生成密钥")
    public Result<SigningKeyDTO> generate(@RequestBody GenerateSigningKeyRequest request) {
            return signingKeyService.generateSigningKey(request, OperatorContextHelper.getOperatorName());

    }

    @PostMapping("/{keyId}/activate")
    @PluginAction(id = "activate", name = "激活密钥", managementEnabled = true, actionCode = "MANAGE")
    public Result<Void> activate(
            @PathVariable String keyId,
            @RequestBody(required = false) ActivateSigningKeyRequest request) {
            return signingKeyService.activateKey(keyId, request, OperatorContextHelper.getOperatorName());

    }
}

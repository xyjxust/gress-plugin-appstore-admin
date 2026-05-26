package com.keqi.gress.plugin.appstore.admin.controller;

import com.keqi.gress.common.model.Result;
import com.keqi.gress.plugin.appstore.admin.dto.TrustedRootDTO;
import com.keqi.gress.plugin.appstore.admin.service.AppStoreSigningKeyService;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AppStore signing API for clients (stage B).
 */
@Service
@RestController
@RequestMapping("/anon/signing")
public class AppStoreSigningApiController {

    private static final Log log = LogFactory.get(AppStoreSigningApiController.class);

    @Autowired
    private AppStoreSigningKeyService signingKeyService;

    @GetMapping("/trusted-roots")
    public Result<List<TrustedRootDTO>> getTrustedRoots() {
            return Result.success(signingKeyService.getTrustedRoots());
    }
}

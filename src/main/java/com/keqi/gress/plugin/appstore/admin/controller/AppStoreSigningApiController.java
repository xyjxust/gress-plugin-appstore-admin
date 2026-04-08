package com.keqi.gress.plugin.appstore.admin.controller;

import com.keqi.gress.common.model.Result;
import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.plugin.appstore.admin.dto.TrustedRootDTO;
import com.keqi.gress.plugin.appstore.admin.service.AppStoreSigningKeyService;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * AppStore signing API for clients (stage B).
 */
@Service
@RestController
@RequestMapping("/api/appstore/signing")
public class AppStoreSigningApiController {

    private static final Log log = LogFactory.get(AppStoreSigningApiController.class);

    @Inject(source = Inject.BeanSource.PLUGIN)
    private AppStoreSigningKeyService signingKeyService;

    @GetMapping("/trusted-roots")
    public Result<List<TrustedRootDTO>> getTrustedRoots() {
            return Result.success(signingKeyService.getTrustedRoots());
    }
}


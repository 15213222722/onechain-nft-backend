package io.xone.chain.onenft.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.xone.chain.onenft.common.Result;
import io.xone.chain.onenft.entity.Nfts;
import io.xone.chain.onenft.request.NftSearchRequest;
import io.xone.chain.onenft.service.INftsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-16
 */
@Api(tags = "NFT Management")
@RestController
@RequestMapping("/nfts")
public class NftsController {

    @Autowired
    private INftsService nftsService;

    @ApiOperation("Search NFTs")
    @PostMapping("/list")
    public Result<IPage<Nfts>> list(@RequestBody @Validated NftSearchRequest request) {
        return Result.success(nftsService.searchNfts(request));
    }
    
    
    
    
    
    
    
    
    
    
    
}
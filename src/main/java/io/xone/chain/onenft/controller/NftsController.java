package io.xone.chain.onenft.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.xone.chain.onenft.common.Result;
import io.xone.chain.onenft.entity.Nfts;
import io.xone.chain.onenft.request.NftSearchRequest;
import io.xone.chain.onenft.resp.NftResp;
import io.xone.chain.onenft.service.INftsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public Result<IPage<NftResp>> list(@RequestBody @Validated NftSearchRequest request) {
        IPage<Nfts> page = nftsService.searchNfts(request);
        IPage<NftResp> respPage = page.convert(nfts -> BeanUtil.copyProperties(nfts, NftResp.class));
        return Result.success(respPage);
    }

    @ApiOperation("Get NFT Detail by ObjectID")
    @GetMapping("/{objectId}")
    public Result<NftResp> getDetail(@PathVariable String objectId) {
        Nfts nfts = nftsService.getByObjectId(objectId);
        if (nfts == null) {
            return Result.success(null);
        }
        return Result.success(BeanUtil.copyProperties(nfts, NftResp.class));
    }
}
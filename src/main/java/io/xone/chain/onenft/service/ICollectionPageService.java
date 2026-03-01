package io.xone.chain.onenft.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.xone.chain.onenft.dto.CollectionPageDTO;
import io.xone.chain.onenft.request.CollectionPageQueryRequest;

public interface ICollectionPageService {
    IPage<CollectionPageDTO> pageCollections(CollectionPageQueryRequest request);
}

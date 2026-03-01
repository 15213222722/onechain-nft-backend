package io.xone.chain.onenft.request;

import lombok.Data;

@Data
public class CollectionPageQueryRequest {
    private int pageNum = 1;
    private int pageSize = 10;
}

package com.pinyougou.search.service;

import java.util.Map;

public interface ItemSearchService {
    /**
     * 根据搜索条件搜索es中商品sku
     * @param searchMap 搜索条件
     * @return 查询结果
     */
    Map<String, Object> search(Map<String, Object> searchMap);
}

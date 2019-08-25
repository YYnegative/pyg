package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Override
    public Map<String, Object> search(Map<String, Object> searchMap) {
        Map<String, Object> resultMap = new HashMap<>();

        //查询构造对象
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //如果没有搜索关键字的时候则查询全部
        queryBuilder.withQuery(QueryBuilders.matchAllQuery());

        if (searchMap != null) {
            //搜索关键字
            String keywords = searchMap.get("keywords")+"";
            if (StringUtils.isNotBlank(keywords)) {
                //根据搜索关键字查询；参数1：用户输入的关键字，参数2：要在es的哪些域中查询
                queryBuilder.withQuery(QueryBuilders.multiMatchQuery(keywords, "title", "category", "seller", "brand"));
            }
        }

        //创建查询条件对象
        NativeSearchQuery query = queryBuilder.build();
        //搜索
        AggregatedPage<TbItem> aggregatedPage = esTemplate.queryForPage(query, TbItem.class);

        //商品列表
        resultMap.put("itemList", aggregatedPage.getContent());

        return resultMap;
    }
}

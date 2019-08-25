package cn.itcast.es;

import cn.itcast.es.dao.ItemDao;
import com.pinyougou.pojo.TbItem;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-es.xml")
public class ElasticSearchTest {

    //创建索引、映射、复杂的查询
    @Autowired
    private ElasticsearchTemplate esTemplate;

    //简单的CURD使用
    @Autowired
    private ItemDao itemDao;

    //创建索引和映射
    @Test
    public void createIndexAndMapping(){
        //创建索引
        esTemplate.createIndex(TbItem.class);
        //设置映射
        esTemplate.putMapping(TbItem.class);
    }

    //新增或更新
    @Test
    public void save(){
        TbItem item = new TbItem();
        item.setId(100003344497L);
        item.setTitle("222 一加 OnePlus 7 Pro 2K+90Hz 流体屏 骁龙855旗舰 4800万超广角三摄 8GB+256GB 曜岩灰 全面屏拍照游戏手机");
        item.setPrice(4999.0);
        item.setGoodsId(123L);
        item.setBrand("一加");
        item.setCategory("手机");
        item.setSeller("一加");
        item.setImage("https://item.jd.com/100003344497.html");
        item.setUpdateTime(new Date());

        itemDao.save(item);
    }

    //查询全部
    @Test
    public void findAll(){
        Iterable<TbItem> itemList = itemDao.findAll();
        for (TbItem tbItem : itemList) {
            System.out.println(tbItem);
        }
    }

    //根据id删除
    @Test
    public void deleteById(){
        itemDao.deleteById(100003344497L);
    }

    //通配符
    @Test
    public void wildcardQuery(){

        //在spring data es中所有的对象几乎都有一个自身的构造对象

        //查询构造对象
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //设置查询条件对象
        queryBuilder.withQuery(QueryBuilders.wildcardQuery("title", "拍*"));

        //构造查询对象
        NativeSearchQuery query = queryBuilder.build();

        //搜索
        AggregatedPage<TbItem> aggregatedPage = esTemplate.queryForPage(query, TbItem.class);

        //分页信息
        System.out.println("总记录数为：" + aggregatedPage.getTotalElements());
        System.out.println("总页数为：" + aggregatedPage.getTotalPages());

        for (TbItem item : aggregatedPage.getContent()) {
            System.out.println(item);
        }
    }

    //模糊查询 会对搜索的关键字进行分词之后查询；并且查询的结果是以分出的词条的结果 or 的关系
    @Test
    public void matchQuery(){

        //在spring data es中所有的对象几乎都有一个自身的构造对象

        //查询构造对象
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //设置查询条件对象
        queryBuilder.withQuery(QueryBuilders.matchQuery("title", "一加手机"));

        //构造查询对象
        NativeSearchQuery query = queryBuilder.build();

        //搜索
        AggregatedPage<TbItem> aggregatedPage = esTemplate.queryForPage(query, TbItem.class);

        //分页信息
        System.out.println("总记录数为：" + aggregatedPage.getTotalElements());
        System.out.println("总页数为：" + aggregatedPage.getTotalPages());

        for (TbItem item : aggregatedPage.getContent()) {
            System.out.println(item);
        }
    }
}



















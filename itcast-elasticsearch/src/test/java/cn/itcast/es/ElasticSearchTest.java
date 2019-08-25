package cn.itcast.es;

import cn.itcast.es.dao.ItemDao;
import com.pinyougou.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
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
}



















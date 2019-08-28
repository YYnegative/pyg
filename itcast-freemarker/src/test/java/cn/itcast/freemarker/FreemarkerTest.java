package cn.itcast.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class FreemarkerTest {


    @Test
    public void test() throws Exception{
        //1. 模版
        //创建配置对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //设置模版路径
        configuration.setClassForTemplateLoading(FreemarkerTest.class, "/ftl");
        //设置编码
        configuration.setDefaultEncoding("utf-8");
        //获取模版
        Template template = configuration.getTemplate("test.ftl");
        //2. 数据；
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("name", "heima");
        dataModel.put("message", "你好！黑马。");

        //3. 输出；参数1：数据map，参数2：输出对象
        //创建输出对象
        FileWriter fileWriter = new FileWriter("D:\\itcast\\test\\test.html");
        template.process(dataModel, fileWriter);
        fileWriter.close();
    }
}

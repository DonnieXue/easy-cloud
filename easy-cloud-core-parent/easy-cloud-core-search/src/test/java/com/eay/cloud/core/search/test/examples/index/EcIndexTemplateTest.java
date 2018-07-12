package com.eay.cloud.core.search.test.examples.index;

import com.easy.cloud.core.search.EcCoreSearchApplication;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateAction;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequest;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequestBuilder;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;

/**
 * 静态索引模板
 * 静态索引模板 + 动态字段映射（动态模板）
 * https://blog.csdn.net/zhanlanmg/article/details/50352338
 *
 * @Title: EcIndexTemplateTest
 * @Description:
 * @Author tudou
 * @Date 2018/7/3 15:19
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcCoreSearchApplication.class)
public class EcIndexTemplateTest {
    @Autowired
    private TransportClient client;
    /**
     * 使用map构建mapping，并使用PutIndexTemplateRequestBuilder获取结果集
     * restful风格如下：
     * ---------------------------------------------------------------------
     * PUT /_template/my_logs          //创建一个名为 my_logs 的模板。
     *     {
     *       "template": "logstash-*", //将这个模板应用于所有以 logstash- 为起始的索引。
     *       "order":    1,            //这个模板将会覆盖默认的 logstash 模板，因为默认模板的 order 更低。(此模板的优先级更高）
     *       "settings": {
     *         "number_of_shards": 1   //限制主分片数量为 1
     *       },
     *       "mappings": {
     *         "_default_": {
     *           "_all": {              //为所有类型禁用 _all 域。
     *             "enabled": false
     *           }
     *         }
     *       },
     *       "aliases": {
     *         "last_3_months": {}      //添加这个索引至 last_3_months 别名中。
     *       }
     *     }
     * -------------------------------------------------------------------------
     * 参考： https://www.elastic.co/guide/cn/elasticsearch/guide/cn/index-templates.html
     * @see PutIndexTemplateRequestBuilder#execute()
     */
    @Test
    public void testPutIndexTemplate1(){

        Map<String, Object> source = new HashMap<>();
        source.put("number_of_shards", 1);
        Map<String, Object> mappingSource = new HashMap<>();
        Map<String, Object> field = new HashMap<>();
        field.put("enabled", false);
        mappingSource.put("_all",field);

        PutIndexTemplateAction putIndexTemplateAction = PutIndexTemplateAction.INSTANCE;
//        PutIndexTemplateRequestBuilder putIndexTemplateRequestBuilder  = putIndexTemplateAction.newRequestBuilder(client);
        PutIndexTemplateRequestBuilder putIndexTemplateRequestBuilder  = new PutIndexTemplateRequestBuilder(client, putIndexTemplateAction, "test1")
                .setOrder(1)
                .setSettings(source)
                .addMapping("_default_", mappingSource)
                .setPatterns(Collections.singletonList("*-log1"))
                .setAliases("{\"last_3_months\":{}}")
                ;
        PutIndexTemplateResponse actionFuture = putIndexTemplateRequestBuilder.execute().actionGet();
        System.out.println("ok");
    }

    /**
     * 用map构建mapping, 使用IndicesAdminClient获取结果集
     * 有与动态模板中mapping是自己拟造的，可以结合动态字段映射（动态模板）来创建静态索引模板
     * 参考：
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/dynamic-templates.html
     * @see IndicesAdminClient#putTemplate(PutIndexTemplateRequest)
     */
    @Test
    public void testPutIndexTemplate2(){
        Map<String, Object> source = new HashMap<>();
        //设置模板分片个数
        source.put("number_of_shards", 1);
        //配置模板mapping
        Map<String, Object> mappingSource = new HashMap<>();
        List<Object> dynamic_templates = new ArrayList<>();
        Map<String, Object> integers = new HashMap<>();
        Map<String, Object> template1 = new HashMap<>();
        Map<String, Object> mapping = new HashMap<>();
        //过滤long
        integers.put("match_mapping_type","long");
        //筛选出系统识别的long类型转成integer类型
        mapping.put("type", "integer");
        integers.put("mapping",mapping);
        template1.put("mapping",integers);
        dynamic_templates.add(template1);
        //创建动态模板
        mappingSource.put("dynamic_templates", dynamic_templates);
        PutIndexTemplateRequest request = new PutIndexTemplateRequest("test2")
                .order(1)
                .settings(source)
                .mapping("_doc", mappingSource)
                .patterns(Collections.singletonList("*-log2"))
                ;
        IndicesAdminClient indicesAdminClient = client.admin().indices();
        PutIndexTemplateResponse actionFuture = indicesAdminClient.putTemplate(request).actionGet();
        System.out.println("ok");
    }

    /**
     * 使用XContentBuilder构建mapping
     * 参考：
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/dynamic-templates.html
     * @see XContentFactory#jsonBuilder()
     *
     * @throws IOException
     */
    @Test
    public void testPutIndexTemplate3() throws IOException {


//        XContentBuilder builder = getxContentBuilder1();
//        XContentBuilder builder = getxContentBuilder2();
        XContentBuilder builder = getxContentBuilder3();
        //创建模板，设置模板名称
        PutIndexTemplateRequest request = new PutIndexTemplateRequest("test3")
                //设置匹配的正则
                .patterns(Collections.singletonList("*-log3"))
                //设置type
                .mapping("_doc",builder)
                ;
        System.out.println(builder.toString());
        PutIndexTemplateResponse actionFuture = client.admin().indices().putTemplate(request).actionGet();
//        System.out.println(builder.string());
    }



    @Test
    public void testDelIndexTemplate1(){
        client.admin().indices().prepareDeleteTemplate("test1").execute();
        client.admin().indices().prepareDeleteTemplate("test2").execute();
        client.admin().indices().prepareDeleteTemplate("test3").execute();
    }
    /**
     * 配置模板mapping
     * @return
     * @throws IOException
     */
    private XContentBuilder getxContentBuilder1() throws IOException {
        return XContentFactory.jsonBuilder()
                .startObject()
                .startObject("properties")
                .startObject("user")
                .field("type","keyword")
                .endObject()
                .startObject("postDate")
                .field("type","date")
                .endObject()
                .startObject("message")
                .field("type","keyword")
                .endObject()
                .startObject("address")
                .field("type","keyword")
                .endObject()
                .startObject("no")
                .field("type","keyword")
                .endObject()
                .endObject()
                .endObject();
    }
    /**
     * 配置模板mapping
     * @return
     * @throws IOException
     */
    private XContentBuilder getxContentBuilder2() throws IOException {
        return XContentFactory.jsonBuilder()
                .startObject()
                .startArray("dynamic_templates")
                .startObject()
                .startObject("longs_as_strings")
                .field("match_mapping_type","string")
                .field("match","long_*")
                .field("unmatch","*_text")
                .startObject("mapping")
                .field("type","long")
                .endObject()
                .endObject()
                .endObject()
                .endArray()
                .endObject()
                ;
    }
    /**
     * 配置模板mapping
     * @return
     * @throws IOException
     */
    private XContentBuilder getxContentBuilder3() throws IOException {
        return XContentFactory.jsonBuilder()
                .startObject()
                .startArray("dynamic_templates")
                .startObject()
                .startObject("full_name")
                .field("path_match","name.*")
                .field("path_unmatch","*.middle")
                .startObject("mapping")
                .field("type", "text")
                .field("cope_to", "FULL_NAME")
                .endObject()
                .endObject()
                .endObject()
                .endArray()
                .endObject()
                ;
    }
}

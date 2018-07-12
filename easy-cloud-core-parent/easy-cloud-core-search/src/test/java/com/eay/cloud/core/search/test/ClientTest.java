package com.eay.cloud.core.search.test;

import com.easy.cloud.core.search.EcCoreSearchApplication;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateAction;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequest;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequestBuilder;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.support.AbstractClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;

/**
 * @Title: ClientTest
 * @Description:
 * @Author tudou
 * @Date 2018/6/13 16:38
 * @Version 2.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcCoreSearchApplication.class)
public class ClientTest {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private TransportClient client;
    @Test
    public void eTemplate(){
//        elasticsearchTemplate.
    }
    @Test
    public void testClient(){
//        ElasticsearchTemplate
        elasticsearchTemplate.createIndex("test");
    }

    public void testttt(){
//        XContentBuilder mapBuilder = XContentFactory.jsonBuilder();
//        mapBuilder.startObject()
//                .startObject(TypeName)
//                .startObject("_all").field("enabled", false).endObject()
//                .startObject("properties")
//                .startObject(IDFieldName).field("type", "long").endObject()
//                .startObject(SeqNumFieldName).field("type", "long").endObject()
//                .startObject(IMSIFieldName).field("type", "string").field("index", "not_analyzed").endObject()
//                .startObject(IMEIFieldName).field("type", "string").field("index", "not_analyzed").endObject()
//                .startObject(DeviceIDFieldName).field("type", "string").field("index", "not_analyzed").endObject()
//                .startObject(OwnAreaFieldName).field("type", "string").field("index", "not_analyzed").endObject()
//                .startObject(TeleOperFieldName).field("type", "string").field("index", "not_analyzed").endObject()
//                .startObject(TimeFieldName).field("type", "date").field("store", "yes").endObject()
//                .endObject()
//                .endObject()
//                .endObject();
//
//        PutMappingRequest putMappingRequest = Requests
//                .putMappingRequest(IndexName).type(TypeName)
//                .source(mapBuilder);
//        client.admin().indices().putMapping(putMappingRequest).actionGet();
    }
}

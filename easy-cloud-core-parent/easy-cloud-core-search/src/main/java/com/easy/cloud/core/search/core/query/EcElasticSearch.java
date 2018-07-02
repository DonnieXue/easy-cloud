package com.easy.cloud.core.search.core.query;

import org.apache.log4j.Logger;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class EcElasticSearch {

    public Logger logger = Logger.getLogger(EcElasticSearch.class);

    @Autowired
    TransportClient client;






    /**
     * 功能描述：新建索引
     *
     * @param indexName 索引名
     */
    public void createIndex(String indexName) {
        client.admin().indices().create(new CreateIndexRequest(indexName))
                .actionGet();
    }

    /**
     * 功能描述：新建索引
     *
     * @param index 索引名
     * @param type  类型
     */
    public void createIndex(String index, String type) {
        client.prepareIndex(index, type).setSource().get();
    }

    /**
     * 功能描述：删除索引
     *
     * @param index 索引名
     */
    public void deleteIndex(String index) throws Exception {
        if (indexExist(index)) {
            DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(index)
                    .execute().actionGet();
            if (!dResponse.isAcknowledged()) {
                throw new Exception("failed to delete index.");
            }
        } else {
            throw new Exception("index name not exists");
        }
    }

    /**
     * 功能描述：验证索引是否存在
     *
     * @param index 索引名
     */
    public boolean indexExist(String index) {
        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(index);
        IndicesExistsResponse inExistsResponse = client.admin().indices()
                .exists(inExistsRequest).actionGet();
        return inExistsResponse.isExists();
    }

    /**
     * 功能描述：插入数据
     *
     * @param index 索引名
     * @param type  类型
     * @param json  数据
     */
    public void insertData(String index, String type, String json) {
        IndexResponse response = client.prepareIndex(index, type)
                .setSource(json)
                .get();
    }

    /**
     * 功能描述：插入数据
     *
     * @param index 索引名
     * @param type  类型
     * @param _id   数据id
     * @param json  数据
     */
    public void insertData(String index, String type, String _id, String json) {
        IndexResponse response = client.prepareIndex(index, type).setId(_id)
                .setSource(json)
                .get();
    }

    /**
     * 功能描述：更新数据
     *
     * @param index 索引名
     * @param type  类型
     * @param _id   数据id
     * @param json  数据
     */
    public void updateData(String index, String type, String _id, String json) throws Exception {
        try {
            UpdateRequest updateRequest = new UpdateRequest(index, type, _id)
                    .doc(json);
            client.update(updateRequest).get();
        } catch (Exception e) {
            throw new Exception("update data failed.", e);
        }
    }

    /**
     * 功能描述：删除数据
     *
     * @param index 索引名
     * @param type  类型
     * @param _id   数据id
     */
    public void deleteData(String index, String type, String _id) {
        DeleteResponse response = client.prepareDelete(index, type, _id)
                .get();
    }

    /**
     * 功能描述：批量插入数据
     *
     * @param index 索引名
     * @param type  类型
     * @param data  (_id 主键, json 数据)
     */
    public void bulkInsertData(String index, String type, Map<String, String> data) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        data.forEach((param1, param2) -> {
            bulkRequest.add(client.prepareIndex(index, type, param1)
                    .setSource(param2)
            );
        });
        BulkResponse bulkResponse = bulkRequest.get();
    }

    /**
     * 功能描述：批量插入数据
     *
     * @param index    索引名
     * @param type     类型
     * @param jsonList 批量数据
     */
    public void bulkInsertData(String index, String type, List<String> jsonList) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        jsonList.forEach(item -> {
            bulkRequest.add(client.prepareIndex(index, type)
                    .setSource(item)
            );
        });
        BulkResponse bulkResponse = bulkRequest.get();
    }

    /**
     * 功能描述：关闭链接
     */
    public void close() {
        client.close();
    }

}
package com.xuecheng.serarch;

import com.xuecheng.search.SearchApplication;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SearchApplication.class)
public class TestEs {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private RestClient restClient;

    @Test
    public void testCreateIndex() throws IOException {


        CreateIndexRequest createIndexRequest = new CreateIndexRequest("xc_course");
        createIndexRequest.settings(Settings.builder().put("number_of_shards", 1).put("number_of_replicas", 0));

        createIndexRequest.mapping("doc", " {\n" +
                " \t\"properties\": {\n" +
                "            \"studymodel\":{\n" +
                "             \"type\":\"keyword\"\n" +
                "           },\n" +
                "            \"name\":{\n" +
                "             \"type\":\"keyword\"\n" +
                "           },\n" +
                "           \"description\": {\n" +
                "              \"type\": \"text\",\n" +
                "              \"analyzer\":\"ik_max_word\",\n" +
                "              \"search_analyzer\":\"ik_smart\"\n" +
                "           },\n" +
                "           \"pic\":{\n" +
                "             \"type\":\"text\",\n" +
                "             \"index\":false\n" +
                "           }\n" +
                " \t}\n" +
                "}", XContentType.JSON);

        IndicesClient indices = client.indices();
        CreateIndexResponse createIndexResponse = indices.create(createIndexRequest);
        boolean acknowledged = createIndexResponse.isAcknowledged();


        System.out.println(acknowledged);

    }

    @Test
    public void testDeleteIndex() throws IOException {

        DeleteIndexRequest deleteIndexRequest=new DeleteIndexRequest("xc_course");

        IndicesClient indices = client.indices();
        DeleteIndexResponse delete = indices.delete(deleteIndexRequest);

        boolean acknowledged = delete.isAcknowledged();
        System.out.println(acknowledged);

    }

    @Test
    public void testAddDoc() throws IOException {
        IndexRequest indexRequest = new IndexRequest("xc_course", "doc");
        Map<String,Object> jsonMap=new HashMap<>();
        jsonMap.put("name", "我的头好疼");
        jsonMap.put("describtion", "好人吧");
        jsonMap.put("pic", "http://192.168.25.133/group1/M00/00/00/wKgZhVyzFoGAOhvPAAqLkmtc0HY364.png");
        jsonMap.put("studymodel", "20001");

        indexRequest.source(jsonMap);
        IndexResponse indexResponse = client.index(indexRequest);
        DocWriteResponse.Result result = indexResponse.getResult();
        System.out.println(result);

    }
    @Test
    public void testUpdateDoc() throws IOException {
        UpdateRequest updateRequest=new UpdateRequest("xc_course","doc","wiRxB2sBrXFJsan72f9T");

        Map map=new HashMap();
        map.put("name", "我的囧");
        updateRequest.doc(map);

        UpdateResponse updateResponse = client.update(updateRequest);
        RestStatus status = updateResponse.status();
        System.out.println(status);

    }
    @Test
    public void testDeleteDoc() throws IOException {


        DeleteRequest deleteRequest = new DeleteRequest("xc_course", "doc", "wiRxB2sBrXFJsan72f9T");

        DeleteResponse delete = client.delete(deleteRequest);
        RestStatus status = delete.status();
        System.out.println(status);

    }
}

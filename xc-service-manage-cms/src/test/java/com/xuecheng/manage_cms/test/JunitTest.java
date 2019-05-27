package com.xuecheng.manage_cms.test;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import sun.misc.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JunitTest {
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Test
    public void testGridFs() throws FileNotFoundException {
        File file = new File("d:/index_banner.ftl");
        FileInputStream fis = new FileInputStream(file);
        ObjectId objectId = gridFsTemplate.store(fis, "轮播图模板");
        String id = objectId.toString();
        System.out.println(id);
    }
    @Autowired
    private GridFSBucket gridFSBucket;
    @Test
    public void testGridFSDownLoad() throws IOException {
        GridFSFile fsFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("5ce924f156d51d447c737868")));
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(fsFile.getObjectId());
        GridFsResource gridFsResource = new GridFsResource(fsFile, gridFSDownloadStream);
        String s = org.apache.commons.io.IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
        System.out.println(s);
    }



}

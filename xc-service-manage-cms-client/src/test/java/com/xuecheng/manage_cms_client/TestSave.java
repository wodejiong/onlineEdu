package com.xuecheng.manage_cms_client;

import com.xuecheng.manage_cms_client.service.PageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSave {
    @Autowired
    private PageService pageService;
    @Test
    public void testGet() {
        pageService.savePageToServerPath("5ce936d056d51d11f4fb4cf0");
    }
}

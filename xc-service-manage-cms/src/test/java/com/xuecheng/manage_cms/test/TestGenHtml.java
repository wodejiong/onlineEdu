package com.xuecheng.manage_cms.test;

import com.xuecheng.manage_cms.service.CmsPageService;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestGenHtml {

    @Autowired
    private CmsPageService cmsPageService;
    @Test
    public void testGenHtml() throws IOException, TemplateException {
        String generateHtml = cmsPageService.getGenerateHtml("5ce936d056d51d11f4fb4cf0");

        System.out.println(generateHtml);
    }
}

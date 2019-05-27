package com.xuecheng.manage_cms.controller;

import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.CmsPageService;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

@Controller
@RequestMapping("/cms")
public class CmsPagePreviewController extends BaseController {
    @Autowired
    private CmsPageService cmsPageService;

    @RequestMapping(value = "/preview/{pageId}", method = RequestMethod.GET)
    public void preview(@PathVariable("pageId") String pageId) {
        try {
            String html = cmsPageService.getGenerateHtml(pageId);
            if (StringUtils.isNotEmpty(html)) {
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(html.getBytes("utf-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}

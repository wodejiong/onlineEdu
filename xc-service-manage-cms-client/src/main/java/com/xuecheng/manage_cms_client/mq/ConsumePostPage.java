package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.service.PageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
@Component
public class ConsumePostPage {
    @Autowired
    private PageService pageService;
    @Autowired
    private CmsPageRepository cmsPageRepository;

    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPage(String msg) {

        Map map = JSON.parseObject(msg, Map.class);
        String pageId = (String) map.get("pageId");

        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()) {
            ExceptionCast.castException(CommonCode.INVALIDIDATE_PARAM);
        }

        pageService.savePageToServerPath(pageId);

    }
}

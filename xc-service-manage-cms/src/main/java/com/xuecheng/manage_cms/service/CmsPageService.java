package com.xuecheng.manage_cms.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.CustomException;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.netty.util.internal.StringUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CmsPageService {
    @Autowired
    private CmsRepository cmsRepository;

    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }

        //设置条件
        CmsPage cmsPage = new CmsPage();
        //设置别名
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        //设置站点id
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        //设置模板id
        if (StringUtils.isNotEmpty(queryPageRequest.getTemplateId())) {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }

        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("pageAliase",
                ExampleMatcher.GenericPropertyMatchers.contains());
        Example<CmsPage> example = Example.of(cmsPage, matcher);

        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsRepository.findAll(example, pageable);
        QueryResult<CmsPage> queryResult = new QueryResult<>();
        queryResult.setTotal(all.getTotalElements());
        queryResult.setList(all.getContent());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    //新增一条记录
    public CmsPageResult add(CmsPage cmsPage) {
        if (cmsPage == null) {
            ExceptionCast.castException(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }

        //查询唯一性
        CmsPage cmsPage1 = cmsRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if (cmsPage1 != null) {
            ExceptionCast.castException(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }

        cmsPage.setPageId(null);
        CmsPage save = cmsRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS, save);


    }

    //根据id进行查询
    public CmsPage findOne(String id) {
        Optional<CmsPage> optional = cmsRepository.findById(id);
        if (optional.isPresent()) {
            CmsPage cmsPage = optional.get();
            return cmsPage;
        }
        return null;
    }

    public CmsPageResult update(String id, CmsPage cmsPage) {
        CmsPage one = findOne(id);
        if (one != null) {
            one.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            one.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            one.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            one.setPageName(cmsPage.getPageName());
            //更新访问路径
            one.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            one.setDataUrl(cmsPage.getDataUrl());
            one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            CmsPage save = cmsRepository.save(one);
            if (save != null) {
                return new CmsPageResult(CommonCode.SUCCESS, save);
            }
        }
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    public ResponseResult deleteById(String id) {
        Optional<CmsPage> op = cmsRepository.findById(id);
        if (op.isPresent()) {
            cmsRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }


    public String getGenerateHtml(String pageId) throws IOException, TemplateException {

        //获取model
        Map<String, Object> model = getModel(pageId);
        if (model == null) {
            ExceptionCast.castException(CmsCode.CMS_GENERATEHTML_SAVEHTMLERROR);
        }

        //获取template
        String stringTemplate = getTemplate(pageId);
        if (StringUtils.isEmpty(stringTemplate)) {
            ExceptionCast.castException(CmsCode.CMS_GENERATEHTML_SAVEHTMLERROR);
        }
        //静态化
        String html = genHtml(stringTemplate, model);
        if (StringUtils.isEmpty(html)) {
            ExceptionCast.castException(CmsCode.CMS_GENERATEHTML_SAVEHTMLERROR);
        }
        return html;
    }

    /**
     * 获取model
     *
     * @return
     */
    @Autowired
    private RestTemplate restTemplate;

    private Map<String, Object> getModel(String pageId) {
        CmsPage one = findOne(pageId);
        if (one == null) {
            ExceptionCast.castException(CmsCode.CMS_GENERATEHTML_INVALIDADE);
        }
        //获取url
        String dataUrl = one.getDataUrl();
        if (StringUtils.isEmpty(dataUrl)) {
            ExceptionCast.castException(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }

        ResponseEntity<Map> entity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = entity.getBody();
        return body;

    }

    /**
     * 获取模板字符串
     * @return
     */
    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;
    @Autowired
    private GridFSBucket gridFSBucket;
    @Autowired
    private GridFsTemplate gridFsTemplate;

    private String getTemplate(String pageId) throws IOException {
        CmsPage one = findOne(pageId);
        if (one == null) {
            ExceptionCast.castException(CmsCode.CMS_GENERATEHTML_INVALIDADE);
        }

        String templateId = one.getTemplateId();
        if (StringUtils.isEmpty(templateId)) {
            ExceptionCast.castException(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if (optional.isPresent()) {
            CmsTemplate cmsTemplate = optional.get();
            if (cmsTemplate == null) {
                ExceptionCast.castException(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
            }
            String templateFileId = cmsTemplate.getTemplateFileId();

            GridFSFile fsFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(fsFile.getObjectId());
            GridFsResource gridFsResource = new GridFsResource(fsFile, gridFSDownloadStream);
            String s = org.apache.commons.io.IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
            return s;
        }
        return null;
    }

    //生成静态页面
    private String genHtml(String templateString,Map model) throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.getVersion());
        StringTemplateLoader templateLoader=new StringTemplateLoader();
        templateLoader.putTemplate("template", templateString);
        configuration.setTemplateLoader(templateLoader);
        Template template = configuration.getTemplate("template", "utf-8");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        return html;
    }
}

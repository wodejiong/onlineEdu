package com.xuecheng.manage_cms_client.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class PageService {

    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    CmsSiteRepository cmsSiteRepository;
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    GridFSBucket gridFSBucket;

    //将html内容从gridfs中取出,
    //拼接保存路径
    //保存到本地


    public void savePageToServerPath(String pageId){
         //拼接保存路径
        String path = getPath(pageId);

        //将html从gridFs中取出
        InputStream htmlInputStream = getHtml(pageId);

        //将html保存到服务器本地
        FileOutputStream fos = null;
        try {
           fos=new FileOutputStream(new File(path));
            IOUtils.copy(htmlInputStream, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                htmlInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    //将html从gridFs中取出
    private InputStream getHtml(String pageId) {

        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()) {
            ExceptionCast.castException(CommonCode.INVALIDIDATE_PARAM);
        }

        CmsPage cmsPage = optional.get();
        //获取htmlFiledId
        String htmlFileId = cmsPage.getHtmlFileId();

        GridFSFile fsFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(htmlFileId)));
        GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(fsFile.getObjectId());
        return downloadStream;
    }
    //拼接保存路径
    private String getPath(String pageId) {
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()) {
            ExceptionCast.castException(CommonCode.INVALIDIDATE_PARAM);
        }

        CmsPage cmsPage = optional.get();
        String pagePath = cmsPage.getPageWebPath();//页面路径

        String pageName = cmsPage.getPageName();//页面名称

        String siteId = cmsPage.getSiteId();

        Optional<CmsSite> optionalCmsSite = cmsSiteRepository.findById(siteId);
        if (!optionalCmsSite.isPresent()) {
            ExceptionCast.castException(CommonCode.INVALIDIDATE_PARAM);
        }

        CmsSite cmsSite = optionalCmsSite.get();
        String siteWebPath = cmsSite.getSiteWebPath();

        String path = siteWebPath + pagePath + pageName;
        return path;

    }

}

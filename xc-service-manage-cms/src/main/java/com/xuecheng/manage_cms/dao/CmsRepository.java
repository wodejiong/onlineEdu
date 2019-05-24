package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CmsRepository extends MongoRepository<CmsPage, String> {


    public CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName,String siteId,String webPath);
}

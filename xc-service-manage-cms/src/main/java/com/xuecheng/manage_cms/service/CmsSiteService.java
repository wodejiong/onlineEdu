package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.manage_cms.dao.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CmsSiteService {
    @Autowired
    private SiteRepository siteRepository;

    public List<CmsSite> findAll() {
       return siteRepository.findAll();
    }
}

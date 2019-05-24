package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSiteRepository {
    @Autowired
    private SiteRepository siteRepository;

    @Test
    public void testFindAll() {
        List<CmsSite> all = siteRepository.findAll();
        System.out.println(all);
    }
}

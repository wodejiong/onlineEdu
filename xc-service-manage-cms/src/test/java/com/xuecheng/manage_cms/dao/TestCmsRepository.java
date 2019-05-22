package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestCmsRepository {


    @Autowired
    private CmsRepository cmsRepository;

    @Test
    public void testFindAll() {
        List<CmsPage> all = cmsRepository.findAll();
        System.out.println(all);
    }
    @Test
    public void testPage() {
        int page=0;
        int size = 2;
        Pageable pageable = PageRequest.of(page, size);

        Page<CmsPage> all = cmsRepository.findAll(pageable);
        System.out.println(all);
    }

    @Test
    public void testUpdate() {
        Optional<CmsPage> optionalCmsPage = cmsRepository.findById("5ce552d956d51d4ba40ebd8f");
        if (optionalCmsPage.isPresent()) {
            CmsPage cmsPage = optionalCmsPage.get();
            cmsPage.setPageAliase("aaa");
            CmsPage save = cmsRepository.save(cmsPage);
            System.out.println(save);
        }
    }
    @Test
    public void testDelete() {
        cmsRepository.deleteById("5ce552d956d51d4ba40ebd8f");
    }
}

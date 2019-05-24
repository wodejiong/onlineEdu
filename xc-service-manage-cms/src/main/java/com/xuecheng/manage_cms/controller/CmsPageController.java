package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cms")
public class CmsPageController implements CmsPageControllerApi {
    @Autowired
    private CmsPageService cmsPageService;

    @Override
    @GetMapping("/page/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest) {

//        QueryResult<CmsPage> queryResult=new QueryResult<>();
//        List<CmsPage> list=new ArrayList<>();
//        CmsPage cmspage=new CmsPage();
//        cmspage.setPageAliase("nihaoa ");
//
//        list.add(cmspage);
//        queryResult.setList(list);
//        queryResult.setTotal(1);
//
//        QueryResponseResult result = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
//
//        return result;
       return cmsPageService.findList(page, size, queryPageRequest);

    }

    @Override
    @PostMapping("/add")
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return cmsPageService.add(cmsPage);
    }

    @Override
    @GetMapping("/get/{id}")
    public CmsPage findById(@PathVariable("id") String id) {
        return cmsPageService.findOne(id);
    }

    @Override
    @PutMapping("/put/{id}")
    public CmsPageResult edit(@PathVariable("id") String id, @RequestBody CmsPage cmsPage) {
        return cmsPageService.update(id, cmsPage);
    }
}

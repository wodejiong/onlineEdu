package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@Api(value="cms站点管理接口",description="cms站点管理接口，提供站点的查询")
public interface CmsSiteControllerApi {
    @ApiOperation("查询站点列表")
    public List<CmsSite> findAll() ;

}

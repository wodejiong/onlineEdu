package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("cms_config的查询功能的接口")
public interface CmsConfigControllerApi {
    @ApiOperation("通过id查询,返回一个cmsConfig对象")
    public CmsConfig findById(String id);

}

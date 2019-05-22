package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;

public interface CmsPageControllerApi {
    //分页查询
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);
}

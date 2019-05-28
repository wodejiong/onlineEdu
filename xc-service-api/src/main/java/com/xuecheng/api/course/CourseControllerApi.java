package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("课程信息的接口,课程信息的增删改查")
public interface CourseControllerApi {
    @ApiOperation("课程计划查询,课程计划树")
    public TeachplanNode findTeachPlanList(String courseId);
}

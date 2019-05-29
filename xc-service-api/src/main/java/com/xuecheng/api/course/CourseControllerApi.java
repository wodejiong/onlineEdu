package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("课程信息的接口,课程信息的增删改查")
public interface CourseControllerApi {
    @ApiOperation("课程计划查询,课程计划树")
    public TeachplanNode findTeachPlanList(String courseId);

    @ApiOperation("添加课程计划")
    public ResponseResult addTeachPlan(Teachplan teachplan);

    @ApiOperation("添加图片")
    public ResponseResult addPic(String courseId,String pic);

    @ApiOperation("查询图片")
    public CoursePic queryPic(String courseId);

    @ApiOperation("删除图片")
    public ResponseResult deletePic(String courseId);


}

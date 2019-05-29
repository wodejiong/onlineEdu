package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CoursePicService;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {
    @Autowired
    private CourseService courseService;
    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachPlanList(@PathVariable("courseId") String courseId) {
        return courseService.findTeachPlanList(courseId);
    }

    @Override
    @PostMapping("/teachplan/add")
    public ResponseResult addTeachPlan(@RequestBody Teachplan teachplan) {
        return courseService.addTeachplan(teachplan);
    }

    @Autowired
    CoursePicService coursePicService;

    @Override
    @PostMapping("/coursepic/add")
    public ResponseResult addPic(@RequestParam("courseId") String courseId, @RequestParam("pic") String pic) {
        return coursePicService.addPic(courseId,pic);
    }

    @Override
    public CoursePic queryPic(String courseId) {
        return coursePicService.queryPic(courseId);
    }

    @Override
    public ResponseResult deletePic(String courseId) {
        return coursePicService.deletePic(courseId);
    }

}

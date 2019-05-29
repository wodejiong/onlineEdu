package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CoursePicRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CoursePicService {

    @Autowired
    CoursePicRespository coursePicRespository;
    public ResponseResult addPic(String courseId, String pic) {
        //从数据库中查询
        Optional<CoursePic> optional = coursePicRespository.findById(courseId);
        CoursePic coursePic=null;
        if (!optional.isPresent()) {//若没有查询到
            coursePic=new CoursePic();
        }
        coursePic = optional.get();//若查询到

        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);

        CoursePic save = coursePicRespository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public CoursePic queryPic(String courseId) {
        Optional<CoursePic> optional = coursePicRespository.findById(courseId);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public ResponseResult deletePic(String courseId) {
        long l = coursePicRespository.deleteByCourseid(courseId);
        if (l >= 1) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }
}

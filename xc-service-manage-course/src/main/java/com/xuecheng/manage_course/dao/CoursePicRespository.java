package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursePicRespository extends JpaRepository<CoursePic,String>{


    public long deleteByCourseid(String courseId);
}

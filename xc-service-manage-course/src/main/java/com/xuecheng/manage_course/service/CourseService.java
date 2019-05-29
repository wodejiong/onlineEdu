package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import com.xuecheng.manage_course.dao.TeachplanRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    TeachplanMapper teachplanMapper;

    //查询课程计划列表,查询课程计划树
    public TeachplanNode findTeachPlanList(String courseId) {
        return teachplanMapper.findTeachPlanList(courseId);
    }

    //添加一个课程计划
    public ResponseResult addTeachplan(Teachplan teachplan) {
        //若选择了父节点,则添加一个三级节点,若未选择父节点,则添加一个二级节点,若是新课程,则创建一个一级节点(根节点)
        if (teachplan == null ||
                StringUtils.isEmpty(teachplan.getCourseid()) ||
                StringUtils.isEmpty(teachplan.getPname())) {
            ExceptionCast.castException(CommonCode.INVALIDIDATE_PARAM);
        }
        //获取父节点
        String parentId = teachplan.getParentid();
        String courseid = teachplan.getCourseid();//获取课程id

        if (StringUtils.isEmpty(parentId)) {
            parentId = getParentId(courseid);
        }

        Optional<Teachplan> parentNode = teachplanRepository.findById(parentId);
        if (!parentNode.isPresent()) {
            ExceptionCast.castException(CommonCode.INVALIDIDATE_PARAM);
        }
        Teachplan teachplanParent = parentNode.get();
        String grade = teachplanParent.getGrade();
        if (grade.equals("1")) {
            teachplan.setGrade("2");
        }
        if (grade.equals("2")) {
            teachplan.setGrade("3");
        }
        teachplan.setParentid(parentId);
        teachplan.setCourseid(courseid);
        teachplan.setStatus("0");
        teachplanRepository.save(teachplan);
        return new ResponseResult(CommonCode.SUCCESS);


    }

    @Autowired
    private CourseBaseRepository courseBaseRepository;
    @Autowired
    private TeachplanRepository teachplanRepository;


    private String getParentId(String courseId) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()) {
            return null;
        }
        CourseBase courseBase = optional.get();

        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndAndParentid(courseId, "0");

        if (teachplanList.size() <= 0 || teachplanList == null) {//根节点不存在,该课程为刚创建课程
            //创建根节点
            Teachplan teachplanNew = new Teachplan();

            teachplanNew.setPname(courseBase.getName());
            teachplanNew.setStatus("0");
            teachplanNew.setCourseid(courseId);
            teachplanNew.setGrade("1");
            teachplanNew.setParentid("0");

            Teachplan save = teachplanRepository.save(teachplanNew);

            return save.getId();
        }

        return teachplanList.get(0).getId();
    }
}

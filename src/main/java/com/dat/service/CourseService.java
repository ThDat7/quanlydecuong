package com.dat.service;

import com.dat.pojo.Course;

import java.util.List;
import java.util.Map;

public interface CourseService extends BaseService<Course, Integer> {
    List<Course> getAll();

    List<Course> getCourseNotCreatedAssign(Map<String, String> params);

    Long countCourseNotCreatedAssign(Map<String, String> params);
}

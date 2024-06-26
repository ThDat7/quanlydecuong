package com.dat.controllers;


import com.dat.pojo.Course;
import com.dat.pojo.CourseOutline;
import com.dat.pojo.OutlineStatus;
import com.dat.pojo.Teacher;
import com.dat.service.CourseOutlineService;
import com.dat.service.CourseService;
import com.dat.service.MajorService;
import com.dat.service.TeacherService;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/course-outlines")
@PropertySource("classpath:configs.properties")
public class CourseOutlineController
        extends EntityListController<CourseOutline, Integer> {

    private Environment env;
    private CourseOutlineService courseOutlineService;

    private CourseService courseService;

    private MajorService majorService;

    private TeacherService teacherService;

    public CourseOutlineController(Environment env, CourseOutlineService courseOutlineService,
                                   CourseService courseService, MajorService majorService,
                                   TeacherService teacherService) {
        super("courseOutline", "/course-outlines",
                "Đề cương môn học",
                List.of("id",
                        "Tên môn học",
                        "Năm học tạo",
                        "Trạng thái",
                        "Giáo viên biên soạn"
                ),
                env, courseOutlineService);
        this.courseOutlineService = courseOutlineService;
        this.env = env;
        this.courseService = courseService;
        this.majorService = majorService;
        this.teacherService = teacherService;
    }

    @Override
    protected Boolean isCanCreate() {
        return false;
    }

    protected List<List> getRecords(Map<String, String> params) {
        List<CourseOutline> courseOutlines = courseOutlineService.getAll(params);
        return courseOutlines.stream().map(courseOutline -> List.of(
                        courseOutline.getId(),
                        courseOutline.getCourse().getName(),
                        courseOutline.getYearPublished(),
                        courseOutline.getStatus().toString(),
                        String.format("%s %s",
                                courseOutline.getTeacher().getUser().getLastName(),
                                courseOutline.getTeacher().getUser().getFirstName())
                ))
                .collect(Collectors.toList());
    }

    @Override
    protected List<Filter> getFilters() {
        Filter statusFilter = new Filter("Trạng thái", "status",
                List.of(new FilterItem(OutlineStatus.DOING.toString(), OutlineStatus.DOING.name()),
                        new FilterItem(OutlineStatus.COMPLETED.toString(), OutlineStatus.COMPLETED.name()),
                        new FilterItem(OutlineStatus.PUBLISHED.toString(), OutlineStatus.PUBLISHED.name())));

        Filter courseFilter = new Filter("Môn học", "course",
                courseService.getAll().stream()
                        .map(c -> new FilterItem(c.getName(), c.getId().toString()))
                        .collect(Collectors.toList()));

        Filter majorFilter = new Filter("Ngành học", "major",
                majorService.getAll().stream()
                        .map(m -> new FilterItem(m.getName(), m.getId().toString()))
                        .collect(Collectors.toList()));

        List<FilterItem> yearFilterItem = new ArrayList();
        for (int i = 2026; i > 2020; i--) {
            yearFilterItem.add(new FilterItem(String.valueOf(i), String.valueOf(i)));
        }

        Filter yearFilter = new Filter("Năm học tạo", "year", yearFilterItem);
        return List.of(statusFilter, courseFilter, majorFilter, yearFilter);
    }

    @Override
    public void addAtributes(Model model) {
        Map allCourses = courseService.getAll().stream()
                .collect(Collectors.toMap(Course::getId, Course::getName));
        Map teachers = teacherService.getAll().stream()
                .collect(Collectors.toMap(Teacher::getId, t -> String.format("%s %s",
                        t.getUser().getLastName(), t.getUser().getFirstName())));

        model.addAttribute("courses", allCourses);
        model.addAttribute("teachers", teachers);
        model.addAttribute("statuses", OutlineStatus.values());
    }
}

package com.dat.controllers;

import com.dat.pojo.Faculty;
import com.dat.service.FacultyService;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/faculties")
@PropertySource("classpath:configs.properties")
public class FacultyController
        extends EntityListController<Faculty, Integer> {

    private Environment env;
    private FacultyService facultyService;

    public FacultyController(Environment env, FacultyService facultyService) {
        super("faculty", "/faculties",
                "Khoa",
                List.of("id",
                        "Tên",
                        "Tên viết tắt"),
                env, facultyService);
        this.facultyService = facultyService;
        this.env = env;
    }

    protected List<List> getRecords(Map<String, String> params) {
        List<Faculty> faculties = facultyService.getAll(params);
        return faculties.stream().map(faculty -> List.of(
                        faculty.getId(),
                        faculty.getName(),
                        faculty.getAlias()))
                .collect(Collectors.toList());
    }

    @Override
    protected List<Filter> getFilters() {
        return List.of();
    }

    @PostMapping
    public String add(@Valid Faculty f, BindingResult rs, Model model) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return super.add(f, rs, model);
    }

    @Override
    public void addAtributes(Model model) {

    }
}

package com.dat.controllers;

import com.dat.pojo.Teacher;
import com.dat.pojo.User;
import com.dat.pojo.UserRole;
import com.dat.pojo.UserStatus;
import com.dat.service.BaseService;
import com.dat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/users")
@PropertySource("classpath:configs.properties")
public class UserController extends EntityListController<User, Integer> {
    @Autowired
    private UserService userService;
    @Autowired
    private Environment env;

    public UserController(Environment env, UserService userService) {
        super("user", "/users", "Người dùng",
                List.of("id",
                        "Tên",
                        "Chức vụ",
                        "Email"), env, userService);
        this.env = env;
        this.userService = userService;
    }

    @Override
    public String detail(Model model, @PathVariable Integer id) {
        String destination = super.detail(model, id);
        User user = (User) model.getAttribute("user");
        user.setPassword("");
        model.addAttribute("user", user);
        return destination;
    }

    @PostMapping
    public String add(@Valid @ModelAttribute("user") User user,
                      BindingResult rs,
                      @RequestParam(name = "avatar", required = false) MultipartFile avatar,
                      Model model) throws IOException {
        if (rs.hasErrors()) {
            addDetailAttributes(model);
            return "user-detail";
        }

        userService.addOrUpdate(user, avatar);
        return "redirect:/users";
    }

    @Override
    protected List<List> getRecords(Map<String, String> params) {
        List<User> users = userService.getAll(params);
        return users.stream().map(user -> List.of(
                        user.getId(),
                        String.format("%s %s", user.getLastName(), user.getFirstName()),
                        user.getRole().toString(),
                        user.getEmail()))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    protected List<EntityListController<User, Integer>.Filter> getFilters() {
        Filter roleFilter = new Filter("Chức vụ", "role",
                List.of(new FilterItem(UserRole.TEACHER.toString(), UserRole.TEACHER.name()),
                        new FilterItem(UserRole.STUDENT.toString(), UserRole.STUDENT.name()),
                        new FilterItem(UserRole.ADMIN.toString(), UserRole.ADMIN.name())));
        Filter statusFilter = new Filter("Trạng thái", "status",
                List.of(new FilterItem(UserStatus.ENABLED.toString(), UserStatus.ENABLED.name()),
                        new FilterItem(UserStatus.DISABLED.toString(), UserStatus.DISABLED.name()),
                        new FilterItem(UserStatus.PENDING.toString(), UserStatus.PENDING.name()),
                        new FilterItem(UserStatus.NEED_INFO.toString(), UserStatus.NEED_INFO.name())));

        return List.of(roleFilter, statusFilter);
    }

    public void addAtributes(Model model) {
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("statuses", UserStatus.values());
    }


}

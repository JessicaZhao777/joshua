package org.gyt.web.admin;

import org.apache.commons.lang3.StringUtils;
import org.gyt.web.api.service.RoleService;
import org.gyt.web.api.service.UserService;
import org.gyt.web.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * 后台页面路由器
 * Created by Administrator on 2016/9/16.
 */
@RestController
@RequestMapping("/admin")
public class AdminUserPageController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @RequestMapping("/user")
    public ModelAndView userTablePage(
            @RequestParam(required = false) String type
    ) {
        ModelAndView modelAndView = new ModelAndView("admin-user");

        if (StringUtils.isEmpty(type)) {
            modelAndView.addObject("users", userService.getAll());
            modelAndView.addObject("subtitle", "全部用户");
        } else if (type.equalsIgnoreCase("ADMIN")) {
            modelAndView.addObject("users", userService.getAll().stream().filter(user -> user.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"))).collect(Collectors.toList()));
            modelAndView.addObject("subtitle", "系统管理员");
        } else if (type.equalsIgnoreCase("EDITOR")) {
            modelAndView.addObject("users", userService.getAll().stream().filter(user -> user.getRoles().stream().anyMatch(role -> role.getName().equals("EDITOR"))).collect(Collectors.toList()));
            modelAndView.addObject("subtitle", "网站编辑");
        } else if (type.equalsIgnoreCase("FS_ADMIN")) {
            modelAndView.addObject("users", userService.getAll().stream().filter(user -> user.getRoles().stream().anyMatch(role -> role.getName().equals("FS_ADMIN"))).collect(Collectors.toList()));
            modelAndView.addObject("subtitle", "团契管理员");
        } else if (type.equalsIgnoreCase("RE_ADMIN")) {
            modelAndView.addObject("users", userService.getAll().stream().filter(user -> user.getRoles().stream().anyMatch(role -> role.getName().equals("RE_ADMIN"))).collect(Collectors.toList()));
            modelAndView.addObject("subtitle", "资源管理员");
        } else if (type.equalsIgnoreCase("MEMBER")) {
            modelAndView.addObject("users", userService.getAll().stream().filter(user -> user.getRoles().stream().anyMatch(role -> role.getName().equals("MEMBER"))).collect(Collectors.toList()));
            modelAndView.addObject("subtitle", "团契成员");
        } else if (type.equalsIgnoreCase("USER")) {
            modelAndView.addObject("users", userService.getAll().stream().filter(user -> user.getRoles().stream().anyMatch(role -> role.getName().equals("USER"))).collect(Collectors.toList()));
            modelAndView.addObject("subtitle", "注册用户");
        } else {
            modelAndView.addObject("users", new ArrayList<>());
            modelAndView.addObject("subtitle", "未知类型");
        }

        return modelAndView;
    }

    @RequestMapping("/user/{username}")
    public ModelAndView userDetailsPage(
            @PathVariable String username
    ) {
        ModelAndView modelAndView = new ModelAndView("admin-user-details");

        User user = userService.get(username);

        if (null == user) {
            modelAndView.setViewName("404");
        } else {
            modelAndView.addObject("user", user);
            modelAndView.addObject("roles", roleService.get());
        }

        return modelAndView;
    }
}

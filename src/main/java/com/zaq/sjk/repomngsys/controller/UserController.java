package com.zaq.sjk.repomngsys.controller;

import com.zaq.sjk.repomngsys.entity.User;
import com.zaq.sjk.repomngsys.entity.UserDTO;
import com.zaq.sjk.repomngsys.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.List;

import static com.zaq.sjk.repomngsys.repository.impl.UserRepositoryImpl.DUPLICATE_PK;

/**
 * @author ZAQ
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private UserServiceImpl userService;

    public UserController(@Autowired UserServiceImpl userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/register")
    public String toRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerSubmit(User user) {
        int res = userService.save(user);
        if (res > 0) {
            Model model = new RedirectAttributesModelMap();
            model.addAttribute(user);
            return login(model);
        } else {
            return "error";
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addUser(User user) {
        int res = userService.save(user);
        if (res > 0) {
            return "success";
        }
        if (res == DUPLICATE_PK) {
            return "该手机号已被注册!";
        }
        return "fail";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @RequestMapping(value = "/error")
    public String error(Model model) {
        model.addAttribute("error", "登陆失败");
        return "error";
    }

    @RequestMapping(value = "")
    public String init(Model model) {
        List<User> users = userService.listAll();
        model.addAttribute("users", users);
        return "users";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String delete(@RequestParam("phone") String phone) {
        int status = userService.deleteByPhone(phone);
        return status > 0 ? "success" : "fail";
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public String update(UserDTO user) {
        int res = userService.update(user);
        if (res > 0) {
            return "success";
        }
        if (res == DUPLICATE_PK) {
            return "该手机号已被注册!";
        }
        return "fail";
    }

}

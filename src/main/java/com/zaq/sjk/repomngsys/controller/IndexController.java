package com.zaq.sjk.repomngsys.controller;

import com.zaq.sjk.repomngsys.entity.User;
import com.zaq.sjk.repomngsys.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

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

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public String registerSubmit(User user) {
        int res = userService.save(user);
        if (res > 0){
            Model model = new RedirectAttributesModelMap();
            model.addAttribute(user);
            return login(model);
        }else{
            return "error";
        }
    }
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }
    @RequestMapping(value = "/error")
    public String error(Model model) {
        model.addAttribute("error", "登陆失败");
        return "error";
    }

}

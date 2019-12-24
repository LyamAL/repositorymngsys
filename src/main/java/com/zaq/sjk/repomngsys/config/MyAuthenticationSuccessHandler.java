package com.zaq.sjk.repomngsys.config;

import com.zaq.sjk.repomngsys.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author ZAQ
 */
@Component("authenticationSuccessHandler")
public class MyAuthenticationSuccessHandler
        extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    @Qualifier("userServiceImpl")
    private UserServiceImpl userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        logger.info("进入登录成功处理类");
        // 把authentication对象转成 json 格式 字符串 通过 response 以application/json;charset=UTF-8 格式写到响应里面去
        //TODO 记住用户
        HttpSession session = request.getSession();
        User authUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        String username = userService.getUserInfoByPhone(authUser.getUsername());
        session.setAttribute("username", username);
        session.setAttribute("phone", authUser.getUsername());
        session.setAttribute("authorities", authentication.getAuthorities());
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect("/index");
    }
}

package com.zaq.sjk.repomngsys;

import com.zaq.sjk.repomngsys.config.MyAccessDecisionManager;
import com.zaq.sjk.repomngsys.config.MyAuthenticationFailureHandler;
import com.zaq.sjk.repomngsys.config.MyAuthenticationSuccessHandler;
import com.zaq.sjk.repomngsys.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.zaq.sjk.repomngsys.entity.RoleTitle.*;

/**
 * @author ZAQ
 */
@Configuration
public class AppConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {
    @Autowired
    @Qualifier("userServiceImpl")
    private UserServiceImpl userService;
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private MyAuthenticationSuccessHandler authenticationSuccessHandler;

    @Bean
    public RoleHierarchy getRoleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(SysAdmin + ">" + Consignor);
        roleHierarchy.setHierarchy(SysAdmin + ">" + Purchaser);
        roleHierarchy.setHierarchy(SysAdmin + ">" + RepoAdmin);
        roleHierarchy.setHierarchy(SysAdmin + ">" + User);
        roleHierarchy.setHierarchy(RepoAdmin + ">" + Consignor);
        roleHierarchy.setHierarchy(RepoAdmin + ">" + Purchaser);
        return roleHierarchy;
    }

    @Bean
    public AccessDecisionManager getAccessDecisionManager() {
        return new MyAccessDecisionManager();
    }

    @Bean
    public BCryptPasswordEncoder getbCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationFailureHandler getMyAuthenticationFailureHandler() {
        return new MyAuthenticationFailureHandler();
    }

    @Bean
    public MyAuthenticationSuccessHandler getAuthenticationSuccessHandler() {
        return new MyAuthenticationSuccessHandler();
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    }

    @Bean
    public AuthenticationProvider getAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setHideUserNotFoundExceptions(false);
        provider.setPasswordEncoder(getbCryptPasswordEncoder());
        return provider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/user/login", "/user/register", "/user/error", "/user/logout",
                        "/css/**", "/data/**", "/img/**", "/js/**", "/webjars/**", "/fonts/**", "/icons-reference/**", "/vendor/**")
                .permitAll()
                .and().authorizeRequests().antMatchers("/**").authenticated()
                .and()
                .formLogin()
                .loginPage("/user/login")
                .usernameParameter("phone")
                .passwordParameter("password")
                .failureHandler(getMyAuthenticationFailureHandler())
                .successHandler(authenticationSuccessHandler)
                .and()
                .logout()
                .logoutUrl("/user/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/user/login");
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

}

package com.zaq.sjk.repomngsys.config;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author ZAQ
 */
public class MyAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication,
                       Object o,
                       Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        // TODO Auto-generated method stub
        System.out.println("MyAccessDecisionManager.decide()------------------验证用户是否具有一定的权限--------");
        if (collection == null) {
            return;
        }
        for (ConfigAttribute configAttribute : collection) {
            String needResource = configAttribute.getAttribute();
            //authentication.getAuthorities()  用户所有的权限
            for (GrantedAuthority ga : authentication.getAuthorities()) {
                if (needResource.equals(ga.getAuthority())) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("--------MyAccessDescisionManager：decide-------权限认证失败！");

    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}

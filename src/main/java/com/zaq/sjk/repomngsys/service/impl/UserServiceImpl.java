package com.zaq.sjk.repomngsys.service.impl;

import com.zaq.sjk.repomngsys.entity.RoleTitle;
import com.zaq.sjk.repomngsys.entity.User;
import com.zaq.sjk.repomngsys.entity.UserDTO;
import com.zaq.sjk.repomngsys.repository.impl.UserRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ZAQ
 */
@Service
@Qualifier("userServiceImpl")
public class UserServiceImpl implements UserDetailsService {
    private BCryptPasswordEncoder encoder;
    private UserRepositoryImpl userRepository;

    private Logger logger = LoggerFactory.getLogger("");

    public UserServiceImpl(@Autowired BCryptPasswordEncoder encoder, @Autowired UserRepositoryImpl userRepository) {
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        UserDetails user = userRepository.loadUsersByUsername(phone);
        if (user == null) {
            this.logger.debug("User {} not found", phone);
            throw new UsernameNotFoundException("User " + phone + " not found");
        }
        //配置用户权限
        Set<GrantedAuthority> dbAuthsSet = new HashSet();
        if (userRepository.isEnableAuthorities()) {
            dbAuthsSet.addAll(userRepository.loadUserAuthorities(user.getUsername()));
        }
        if (userRepository.isEnableGroups()) {
            dbAuthsSet.addAll(userRepository.loadGroupAuthorities(user.getUsername()));
        }
        List<GrantedAuthority> dbAuths = new ArrayList(dbAuthsSet);
        if (dbAuths.size() == 0) {
            this.logger.debug("User {} has no authorities and will be treated as not found", phone);
            throw new UsernameNotFoundException("JdbcDaoImpl.noAuthority" + phone + "User has no GrantedAuthority");
        } else {
            return userRepository.createUserDetails(phone, user, dbAuths);
        }
    }

    public short save(User user) {
        String encodedPsw = encoder.encode(user.getPassword());
        user.setPassword(encodedPsw);
        if (user.getTitles() != null && user.getTitles().split(",").length > 0) {
            return userRepository.insertUserWithCustomRole(user);
        }
        return (short) userRepository.insertUserWithDefaultRole(user);
    }

    public int countUsers() {
        return userRepository.count();
    }

    public List<User> listAll() {
        List<User> users = userRepository.selectAll();
        for (User u : users) {
            String[] titles = u.getTitles().split(",");
            StringBuilder sb = new StringBuilder();
            for (String title : titles) {
                sb.append(toChinese(title)).append(",");
            }
            sb.replace(sb.length() - 1, sb.length(), "");
            u.setTitles(sb.toString());
        }
        return users;
    }

    private String toChinese(String title) {
        switch (title) {
            case RoleTitle.Purchaser:
                return "采购员";
            case RoleTitle.User:
                return "普通用户";
            case RoleTitle.RepoAdmin:
                return "仓库管理员";
            case RoleTitle.SysAdmin:
                return "系统管理员";
            case RoleTitle.Consignor:
                return "发货员";
            default:
                return "";
        }
    }

    public int deleteByPhone(String phone) {
        return userRepository.deleteByPhone(phone);
    }

    public int update(UserDTO user) {
        return userRepository.update(user);
    }

    public List<User> listByRole(String roleTitle) {
        return this.userRepository.selectByRole(roleTitle);
    }

    public String getUserInfoByPhone(String phone) {
        return userRepository.selectUserNameByPhone(phone);
    }
}

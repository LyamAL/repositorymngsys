package com.zaq.sjk.repomngsys.repository;

import com.zaq.sjk.repomngsys.entity.User;
import com.zaq.sjk.repomngsys.entity.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author ZAQ
 */

public interface UserRepository {

    UserDetails createUserDetails(String username, UserDetails userFromUserQuery, List<GrantedAuthority> combinedAuthorities);

    Collection<? extends GrantedAuthority> loadGroupAuthorities(String phone);

    List<GrantedAuthority> loadUserAuthorities(String username);

    UserDetails loadUsersByUsername(String username);


    int count();

    List<User> selectAll();

    int deleteByPhone(String phone);

    short insertUserWithCustomRole(User user);

    int insertUserWithDefaultRole(User user);

    int update(UserDTO user);

    List<User> selectByRole(String roleTitle);

    String selectUserNameByPhone(String phone);
}

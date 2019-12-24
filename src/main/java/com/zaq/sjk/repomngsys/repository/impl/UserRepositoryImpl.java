package com.zaq.sjk.repomngsys.repository.impl;

import com.zaq.sjk.repomngsys.entity.UserDTO;
import com.zaq.sjk.repomngsys.exception.DuplicatePKException;
import com.zaq.sjk.repomngsys.repository.JdbcTemplate;
import com.zaq.sjk.repomngsys.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author ZAQ
 */
@Repository
@Getter
@Setter
public class UserRepositoryImpl implements UserRepository {
    public static final String DEF_USERS_BY_USERNAME_QUERY = "select username,password from users where phone = ?";
    public static final String DEF_AUTHORITIES_BY_USERNAME_QUERY = "select username,authority from authorities where username = ?";
    public static final String DEF_GROUP_AUTHORITIES_BY_USERNAME_QUERY = "select g.id, g.group_name, ga.authority from groups g, group_members gm, group_authorities ga where gm.username = ? and g.id = ga.group_id and g.id = gm.group_id";
    public static final int DUPLICATE_PK = -1;
    private String groupAuthoritiesByPhoneQuery = "select id, title from role where id in (select rid from userrole where uid = ?)";
    private String userByPhoneQuery = "select username,phone,password from users where phone = ?";
    private JdbcTemplate jdbcTemplate;
    private boolean usernameBasedPrimaryKey = true;
    private boolean enableAuthorities = true;
    private boolean enableGroups = false;
    private String rolePrefix = "";
    private String insertUserDefaultRoleQuery = "insert into users values(?,?,?)";
    private String executeUserCustomRoleProcedure =
            "{call insertUserWithCustomRoles(?,?,?,?,?,?,?,?)}";
    private String countUsersQuery = "select count(*) as count from users";
    private String selectAllUsersAndRolesQuery =
            "select distinct username, phone, roles = (stuff((select ',' + title from role r, userrole ur where ur.rid = r.id and ur.uid = us.phone for xml path('')),1,1,'')) from userrole u inner join users us on us.phone = u.uid";
    private String deleteUserByPhone = "delete from users where phone = ?";
    private String executeUpdateUserAndRoleProcedure = "{call updateUserByPhone(?,?,?,?,?,?,?,?)}";
    private String selectUsersByRole = "select username, phone from users join userrole on users.phone=userrole.uid where userrole.rid in (select id from role where title = ?)";
    private String selectUserNameByPhoneQuery = "select username from users where phone = ?";

    public UserRepositoryImpl(@Autowired JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDetails createUserDetails(String username, UserDetails userFromUserQuery, List<GrantedAuthority> combinedAuthorities) {
        String returnUsername = userFromUserQuery.getUsername();
        if (!this.usernameBasedPrimaryKey) {
            returnUsername = username;
        }
        return new User(returnUsername, userFromUserQuery.getPassword(), userFromUserQuery.isEnabled(), userFromUserQuery.isAccountNonExpired(), userFromUserQuery.isCredentialsNonExpired(), userFromUserQuery.isAccountNonLocked(), combinedAuthorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> loadGroupAuthorities(String phone) {
        return new ArrayList<>();
    }

    @Override
    public List<GrantedAuthority> loadUserAuthorities(String phone) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Map<String, List<Object>> map;
        try {
            map = this.getJdbcTemplate().query(this.groupAuthoritiesByPhoneQuery, new String[]{phone});
            String roleName;
            List<String> titles = new ArrayList<>();
            if (map.get("title") == null || map.get("title").size() == 0) {
                titles = new ArrayList<>();
            } else {
                map.get("title").parallelStream().map(String::valueOf).forEach(titles::add);
            }
            for (Object title : titles) {
                roleName = this.rolePrefix + title.toString();
                grantedAuthorities.add(new SimpleGrantedAuthority(roleName));
            }
            return grantedAuthorities;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public UserDetails loadUsersByUsername(String phone) {
        Map<String, List<Object>> map;
        try {
            map = this.getJdbcTemplate().query(this.userByPhoneQuery, new String[]{phone});
            if (map.get("phone") == null || map.get("phone").size() == 0) {
                return null;
            }
            String id = String.valueOf(map.get("phone").get(0));
            String psw = String.valueOf(map.get("password").get(0));
            return new org.springframework.security.core.userdetails.User(id, psw,
                    true, true, true, true, AuthorityUtils.NO_AUTHORITIES);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public short insertUserWithCustomRole(com.zaq.sjk.repomngsys.entity.User user) {
        String[] userParams;
        userParams = new String[]{null, null, null, null, null, null, null};
        userParams[0] = user.getUsername();
        userParams[1] = user.getPhone();
        userParams[2] = user.getPassword();
        int idx = 3;
        for (String title :
                user.getTitles().split(",")) {
            userParams[idx] = title.trim();
            idx++;
            if (idx == 7) {
                break;
            }
        }
        Map<String, Object> res;
        try {
            res = this.getJdbcTemplate()
                    .call(this.executeUserCustomRoleProcedure, userParams, new Integer[]{8}, new Integer[]{Types.TINYINT});
        } catch (DuplicatePKException e) {
            return DUPLICATE_PK;
        }
        return (short) res.get("8");
    }

    @Override
    public int insertUserWithDefaultRole(com.zaq.sjk.repomngsys.entity.User user) {
        String[] userParams = new String[]{user.getUsername(),
                user.getPhone(), user.getPassword()};
        return this.getJdbcTemplate().update(this.insertUserDefaultRoleQuery, userParams);
    }


    @Override
    public int count() {
        int count = 0;
        try {
            Map<String, List<Object>> map = this.getJdbcTemplate().query(this.countUsersQuery, null);
            if (map.get("count") == null || map.get("count").size() == 0) {
                return count;
            }
            count = (int) map.get("count").get(0);
        } catch (SQLException e) {
            e.printStackTrace();
            return count;
        }
        return count;
    }

    @Override
    public List<com.zaq.sjk.repomngsys.entity.User> selectAll() {
        List<com.zaq.sjk.repomngsys.entity.User> users;
        try {
            Map<String, List<Object>> map = this.getJdbcTemplate().query(this.selectAllUsersAndRolesQuery, null);
            int size = map.get("phone").size();
            users = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                com.zaq.sjk.repomngsys.entity.User user = new com.zaq.sjk.repomngsys.entity.User();
                user.setUsername((String) map.get("username").get(i));
                user.setPhone((String) map.get("phone").get(i));
                user.setTitles((String) map.get("roles").get(i));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            users = new ArrayList<>();
        }
        return users;
    }

    @Override
    public List<com.zaq.sjk.repomngsys.entity.User> selectByRole(String roleTitle) {
        List<com.zaq.sjk.repomngsys.entity.User> users;
        try {
            Map<String, List<Object>> map = this.getJdbcTemplate().query(this.selectUsersByRole, new Object[]{roleTitle});
            int size = map.get("phone").size();
            users = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                com.zaq.sjk.repomngsys.entity.User user = new com.zaq.sjk.repomngsys.entity.User();
                user.setUsername((String) map.get("username").get(i));
                user.setPhone((String) map.get("phone").get(i));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            users = new ArrayList<>();
        }
        return users;
    }

    @Override
    public int update(UserDTO user) {
        String[] userParams = new String[]{null, null, null, null, null, null, null};
        int res;
        userParams[0] = user.getUsername();
        userParams[1] = user.getPhone();
        userParams[2] = user.getOldPhone();
        if (user.getTitles() != null && user.getTitles().length() > 0) {
            int i = 3;
            for (String title :
                    user.getTitles().split(",")) {
                if ("User".equals(title)) {
                    continue;
                }
                userParams[i] = title;
                i++;
                if (i == 7) {
                    break;
                }
            }
        }
        try {
            Map<String, Object> map = this.getJdbcTemplate().call(this.executeUpdateUserAndRoleProcedure, userParams,
                    new Integer[]{8}, new Integer[]{Types.TINYINT});
            res = (int) map.get("8");
        } catch (DuplicatePKException e) {
            e.printStackTrace();
            return DUPLICATE_PK;
        }
        return res;
    }

    @Override
    public int deleteByPhone(String phone) {
        return this.getJdbcTemplate().update(this.deleteUserByPhone, new Object[]{phone});
    }

    @Override
    public String selectUserNameByPhone(String phone) {
        Map<String, List<Object>> maps;
        try {
            maps = this.getJdbcTemplate().query(this.selectUserNameByPhoneQuery, new Object[]{phone});
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
        return (String) maps.get("username").get(0);
    }
}

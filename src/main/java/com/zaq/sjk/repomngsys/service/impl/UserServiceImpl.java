package com.zaq.sjk.repomngsys.service;

import com.zaq.sjk.repomngsys.entity.User;
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

    public UserServiceImpl(@Autowired BCryptPasswordEncoder encoder,@Autowired UserRepositoryImpl userRepository) {
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        UserDetails user = userRepository.loadUsersByUsername(phone);
        if (user == null){
            this.logger.debug("User {} not found", phone);
            throw new UsernameNotFoundException("User "+phone +" not found");
        }
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

    public int save(User user) {
        String encodedPsw = encoder.encode(user.getPassword());
        user.setPassword(encodedPsw);
        return userRepository.insert(user);
    }

    public int countUsers() {
        return userRepository.count();

    }
}

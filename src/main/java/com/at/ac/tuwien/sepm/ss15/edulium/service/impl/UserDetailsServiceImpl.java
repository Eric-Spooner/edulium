package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of the UserDetailsService Interface (Spring security)
 */
@Service
@Transactional(readOnly = true)
class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger LOGGER = LogManager.getLogger(UserDetailsServiceImpl.class);

    @Resource(name = "userDAO")
    private DAO<User> userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.debug("Entering loadUserByUsername with parameters: " + username);

        try {
            List<User> users = userDAO.find(User.withIdentity(username));
            if (users.isEmpty()) {
                throw new UsernameNotFoundException("No user with username '" + username + "' found");
            }

            return buildUserDetailsOfUser(users.get(0));
        } catch (DAOException e) {
            throw new UsernameNotFoundException("Searching for users has failed");
        }
    }

    private UserDetails buildUserDetailsOfUser(User user) {
        String identity = user.getIdentity();
        String password = ""; // not implemented

        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));

        return new org.springframework.security.core.userdetails.User(identity, password, enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, authorities);
    }
}

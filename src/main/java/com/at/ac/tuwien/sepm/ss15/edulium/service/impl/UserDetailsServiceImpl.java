package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of the UserDetailsService Interface (Spring security)
 */
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private DAO<User> userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            List<User> users = userDAO.find(User.withIdentity(username));
            if (users.isEmpty()) {
                throw new UsernameNotFoundException("No user with username '" + username + "' found");
            }

            User user = users.get(0);

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
        } catch (DAOException e) {
            throw new UsernameNotFoundException("Searching for users has failed");
        }
    }
}

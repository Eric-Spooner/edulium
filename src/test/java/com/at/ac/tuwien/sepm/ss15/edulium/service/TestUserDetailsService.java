package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit Test of the UserDetailsService interface (Spring security)
 */
public class TestUserDetailsService extends AbstractServiceTest {
    @Autowired
    private UserDetailsService userDetailsService;
    @Mock
    private DAO<User> userDAO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(getTargetObject(userDetailsService), "userDAO", userDAO);
    }

    @Test
    public void testLoadUserByUsername_existingUserShouldReturnUserDetails() throws ServiceException, DAOException, UsernameNotFoundException {
        // PREPARE
        User user = new User();
        user.setIdentity("werewolf");
        user.setName("Wily Werewolf");
        user.setRole("MANAGER");

        Mockito.when(userDAO.find(User.withIdentity("werewolf"))).thenReturn(Arrays.asList(user));

        // WHEN
        UserDetails userDetails = userDetailsService.loadUserByUsername("werewolf");

        // THEN
        Mockito.verify(userDAO).find(User.withIdentity("werewolf"));

        assertEquals("werewolf", userDetails.getUsername());
        assertTrue(userDetails.getPassword().isEmpty()); // not implemented
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());

        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) userDetails.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("MANAGER")));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsername_nonExistingUserShouldThrow() throws ServiceException, DAOException {
        // PREPARE
        Mockito.when(userDAO.find(User.withIdentity("werewolf"))).thenReturn(Arrays.asList());

        // WHEN
        UserDetails userDetails = userDetailsService.loadUserByUsername("werewolf");
    }
}

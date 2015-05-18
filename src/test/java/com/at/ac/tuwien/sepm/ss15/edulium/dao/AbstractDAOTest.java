package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:Spring-DAO.xml")
@WithMockUser(username="daotester")
@Transactional
public abstract class AbstractDAOTest {
    @Autowired
    private DAO<User> userDAO;

    protected User getCurrentUser() {
        List<User> users = null;
        try {
            users = userDAO.find(User.withIdentity(
                    SecurityContextHolder.getContext().getAuthentication().getName()));
        } catch (DAOException e) {
            fail("retrieving test user failed");
        }

        assertEquals("retrieving test user failed", 1, users.size());
        return users.get(0);
    }
}

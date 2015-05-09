package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import org.junit.runner.RunWith;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:Spring-DAO.xml")
@WithMockUser(username="daotester")
@Transactional
public abstract class AbstractDAOTest {
}

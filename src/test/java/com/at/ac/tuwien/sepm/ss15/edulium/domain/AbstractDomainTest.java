package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Base class for all Domain Tests which loads the domain context configuration.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:Spring-DAO.xml")
public abstract class AbstractDomainTest {
}

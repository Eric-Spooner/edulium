package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import org.junit.runner.RunWith;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base class for all Gui Tests which loads the domain context configuration.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/Spring-Edulium.xml")
@WithMockUser(username="servicetester")
@Transactional
public abstract class AbstractGuiTest {
}

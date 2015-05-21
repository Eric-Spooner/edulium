package com.at.ac.tuwien.sepm.ss15.edulium.service;

import org.junit.runner.RunWith;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base class for all Service Tests which loads the service context configuration.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/Spring-Service.xml")
@WithMockUser(username="servicetester")
@Transactional
public abstract class AbstractServiceTest {
    protected <T> T getTargetObject(Object proxy) throws Exception {
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            return (T) ((Advised) proxy).getTargetSource().getTarget();
        } else {
            return (T) proxy;
        }
    }
}
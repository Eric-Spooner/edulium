package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

/*+
 * Unit Test of the MenuService interface
 */
public class TestTipService extends AbstractServiceTest {
        @Autowired
        TipService tipService;

        @Mock
        DAO<User> userDAO;
        @Mock
        Validator<User> userValidator;

        @Before
        public void setUp() throws Exception {
            MockitoAnnotations.initMocks(this);
            ReflectionTestUtils.setField(getTargetObject(tipService), "userDAO", userDAO);
            ReflectionTestUtils.setField(getTargetObject(tipService), "userValidator", userValidator);
        }
}
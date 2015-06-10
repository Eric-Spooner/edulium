package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.UserService;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class TestPollingList extends AbstractGuiTest {
    @Autowired
    private UserService userService;
    @Autowired
    private TaskScheduler taskScheduler;

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testPolling_addAndDeleteUsers() throws ServiceException, ValidationException, InterruptedException {
        // PREPARE
        User user1 = new User();
        user1.setIdentity("tester1");
        user1.setName("Tester 1");
        user1.setRole("POLLINGTESTER");

        User user2 = new User();
        user2.setIdentity("tester2");
        user2.setName("Tester 2");
        user2.setRole("POLLINGTESTER");

        User user3 = new User();
        user3.setIdentity("tester3");
        user3.setName("Tester 3");
        user3.setRole("POLLINGTESTER");

        User user4 = new User();
        user4.setIdentity("tester4");
        user4.setName("Tester 4");
        user4.setRole("POLLINGTESTER");

        PollingList<User> users = new PollingList<>(taskScheduler);
        users.setInterval(100);
        users.setSupplier(new Supplier<List<User>>() {
            @Override
            public List<User> get() {
                try {
                    User matcher = new User();
                    matcher.setRole("POLLINGTESTER");
                    return userService.findUsers(matcher);
                } catch (ServiceException e) {
                    fail("supply has failed");
                    return null;
                }
            }
        });

        // WHEN
        users.startPolling();
        // THEN
        assertTrue(users.isPolling());
        Thread.sleep(150L);
        assertTrue(users.isEmpty());

        // WHEN
        userService.addUser(user1);
        // THEN
        Thread.sleep(150L);
        assertTrue(users.containsAll(Arrays.asList(user1)));

        // WHEN
        userService.addUser(user2);
        userService.addUser(user3);
        // THEN
        Thread.sleep(150L);
        assertTrue(users.containsAll(Arrays.asList(user1, user2, user3)));

        // WHEN
        userService.deleteUser(user1);
        userService.deleteUser(user3);
        userService.addUser(user4);
        // THEN
        Thread.sleep(150L);
        assertTrue(users.containsAll(Arrays.asList(user2, user4)));

        // WHEN
        userService.deleteUser(user2);
        userService.deleteUser(user4);
        // THEN
        Thread.sleep(150L);
        assertTrue(users.isEmpty());

        // WHEN
        users.stopPolling();
        // THEN
        assertFalse(users.isPolling());
    }
}

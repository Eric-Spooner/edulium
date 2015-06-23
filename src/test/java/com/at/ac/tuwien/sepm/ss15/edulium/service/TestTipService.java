package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/*+
 * Unit Test of the MenuService interface
 */
public class TestTipService extends AbstractServiceTest {
        @Autowired
        TipService tipService;
        @Autowired
        OrderService orderService;
        @Autowired
        InteriorService interiorService;
        @Autowired
        UserService userService;
        @Autowired
        MenuService menuService;
        @Autowired
        InvoiceService invoiceService;
        @Autowired
        DAO<User> userDAO;
        @Autowired
        Validator<User> userValidator;

        private User creator1;

        private Order createOrder(BigDecimal value, String additionalInformation,
                                  BigDecimal taxRate, LocalDateTime time, Order.State state, int MenuEntryID) throws ServiceException{

            Order order = new Order();
            order.setTable(interiorService.getAllTables().get(1));
            order.setMenuEntry(menuService.getAllMenuEntries().get(MenuEntryID));
            order.setBrutto(value);
            order.setTax(taxRate);
            order.setAdditionalInformation(additionalInformation);
            order.setTime(time);
            order.setState(state);

            return order;
        }

        @Before
        public void setUp() throws Exception {
        }

        @Ignore // test data from testdata.sql isn't available when test runs via maven test (odd ...)
        @Test
        @WithMockUser(username = "servicetester", roles={"SERVICE"})
        public void testDivideAndMatchTip_shouldWork() throws ServiceException, ValidationException {
            //PREPARE
            Order order1 = createOrder(BigDecimal.valueOf(100),"Order Information", BigDecimal.valueOf(0.2),
                    LocalDateTime.now(), Order.State.QUEUED, 1);
            orderService.addOrder(order1);

            Order order2 = createOrder(BigDecimal.valueOf(200),"Order Information", BigDecimal.valueOf(0.2),
                    LocalDateTime.now(), Order.State.QUEUED, 1);
            orderService.addOrder(order2);

            Invoice invoice = new Invoice();
            invoice.setCreator(User.withIdentity("servicetester"));
            invoice.setTime(LocalDateTime.now());
            invoice.setGross(new BigDecimal("300"));
            invoice.setOrders(Arrays.asList(order1, order2));
            invoiceService.addInvoice(invoice);

            //WHEN
            tipService.divideAndMatchTip(invoice, BigDecimal.valueOf(20));

            //THEN
            List<User> userList = userService.findUsers(User.withIdentity("servicetester"));
            assertEquals(1, userList.size());
            assertTrue(BigDecimal.valueOf(20).compareTo(userList.get(0).getTip()) == 0);
        }

        @Ignore // test data from testdata.sql isn't available when test runs via maven test (odd ...)
        @Test
        @WithMockUser(username = "servicetester", roles={"SERVICE"})
        public void testDivideAndMatchTip_shouldWorkWithMultipleUsers() throws ServiceException, ValidationException {
            //PREPARE
            //The used data is in the testdata.sql file already prepared
            List<Invoice> invoices = invoiceService.findInvoices(Invoice.withIdentity(1));
            assertFalse("should exist, because invoice was prepared in testdata.sql", invoices.isEmpty());
            Invoice invoice = invoices.get(0);

            //WHEN
            tipService.divideAndMatchTip(invoice, BigDecimal.valueOf(100));

            //THEN
            List<User> userList = userService.findUsers(User.withIdentity("servicetester"));
            assertEquals(1, userList.size());
            assertTrue(BigDecimal.valueOf(50).compareTo(userList.get(0).getTip()) == 0);

            userList = userService.findUsers(User.withIdentity("daotester"));
            assertEquals(1, userList.size());
            assertTrue(BigDecimal.valueOf(50).compareTo(userList.get(0).getTip()) == 0);
        }
}
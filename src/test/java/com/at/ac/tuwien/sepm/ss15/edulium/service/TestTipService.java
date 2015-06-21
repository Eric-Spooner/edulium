package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
            creator1 = new User();
            creator1.setIdentity("A");
            creator1.setName("Bob");
            creator1.setRole("ROLE1");
        }

        @Test
        @WithMockUser(username = "servicetester", roles={"SERVICE"})
        public void testCalcTipAndMatchUser_shouldWork() throws ServiceException {
            //PREPARE
            Order order1 = createOrder(BigDecimal.valueOf(100),"Order Information", BigDecimal.valueOf(0.2),
                    LocalDateTime.now(), Order.State.QUEUED, 1);
            Order order2 = createOrder(BigDecimal.valueOf(200),"Order Information", BigDecimal.valueOf(0.2),
                    LocalDateTime.now(), Order.State.QUEUED, 1);
            List<Order> orderList = new LinkedList<>();
            orderList.add(order1);
            orderList.add(order2);
            Invoice invoice = Invoice.withIdentity(1);
            invoice.setCreator(creator1);
            invoice.setTime(LocalDateTime.now());
            invoice.setGross(new BigDecimal("300"));
            invoice.setOrders(orderList);

            //WHEN
            tipService.calculateTheTipPerUserAndMatchItToUser(invoice, BigDecimal.valueOf(20));

            //THEN
            List<User> userList = userService.findUsers(User.withIdentity("servicetester"));

            assertEquals(1, userList.size());
            assertTrue(BigDecimal.valueOf(20).compareTo(userList.get(0).getTip()) != 0);
        }
}
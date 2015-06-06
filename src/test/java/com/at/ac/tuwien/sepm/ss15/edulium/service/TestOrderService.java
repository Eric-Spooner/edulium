package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.*;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.service.impl.InteriorServiceImpl;
import org.h2.command.dml.Update;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.QualifierEntry;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;

/**
 * Unit Test for the taxRateService
 */
public class TestOrderService extends AbstractServiceTest {
    @Autowired
    OrderService orderService;
    @Mock
    private DAO<Order> orderDAO;
    @Mock
    Validator<Order> orderValidator;


    @Autowired
    private MenuService menuService;
    @Autowired
    private InteriorService interiorService;
    @Autowired
    private UserService userService;
    @Autowired
    private TaxRateService taxRateService;


    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    private Order createOrder(BigDecimal brutto, String info, BigDecimal tax,  LocalDateTime time, Order.State state)
            throws ValidationException, DAOException, ServiceException {

        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setName("cat");
        menuService.addMenuCategory(menuCategory);

        TaxRate taxRate = new TaxRate();
        taxRate.setValue(BigDecimal.valueOf(0.5));
        taxRateService.addTaxRate(taxRate);

        User user = User.withIdentity("username");
        int i = 0;
        while(userService.findUsers(user).size()>0){
            user = User.withIdentity("username" + i);
            i++;
        }
        user.setRole("role");
        user.setName("name");
        userService.addUser(user);

        Section section = new Section();
        section.setName("Garden");
        interiorService.addSection(section);

        Table table = new Table();
        table.setColumn(4);
        table.setRow(3);
        table.setSeats(5);
        table.setNumber(1L);
        table.setSection(section);
        table.setUser(user);
        interiorService.addTable(table);

        MenuEntry entry = new MenuEntry();
        entry.setAvailable(true);
        entry.setName("Entry");
        entry.setDescription("Desc");
        entry.setPrice(BigDecimal.valueOf(50.0));
        entry.setCategory(menuCategory);
        entry.setTaxRate(taxRate);
        menuService.addMenuEntry(entry);

        Order order = new Order();
        order.setTable(table);
        order.setMenuEntry(entry);
        order.setBrutto(brutto);
        order.setTax(tax);
        order.setAdditionalInformation(info);
        order.setTime(time);
        order.setState(state);

        return order;
    }


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(getTargetObject(orderService), "orderDAO", orderDAO);
        ReflectionTestUtils.setField(getTargetObject(orderService), "orderValidator", orderValidator);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testAddOrder_shouldAdd() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order = createOrder(BigDecimal.valueOf(500),"Order Information", BigDecimal.valueOf(0.2),
                LocalDateTime.now(), Order.State.QUEUED);

        // WHEN
        orderService.addOrder(order);

        // THEN
        Mockito.verify(orderDAO).create(order);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testAddOrder_withInvalidObjectShouldFail() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order  = new Order();
        Mockito.doThrow(new ValidationException("")).when(orderValidator).validateForCreate(order);

        // WHEN
        orderService.addOrder(order);

        // THEN
        Mockito.verify(orderDAO, never()).create(order);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testAddOrder_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order  = new Order();
        Mockito.doThrow(new DAOException("")).when(orderDAO).create(order);

        // WHEN
        orderService.addOrder(order);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"COOK"})
    public void testAddOrder_withoutPermissionShouldFail() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order  =  createOrder(BigDecimal.valueOf(500),"Order Information", BigDecimal.valueOf(0.2),
                LocalDateTime.now(), Order.State.QUEUED);

        // WHEN
        orderService.addOrder(order);

        // THEN
        Mockito.verify(orderDAO, never()).create(order);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testUpdateOrder_shouldUpdate() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order  = createOrder(BigDecimal.valueOf(500),"Order Information", BigDecimal.valueOf(0.2),
                LocalDateTime.now(), Order.State.QUEUED);
        orderService.addOrder(order);

        // WHEN
        order.setAdditionalInformation("hallo");
        orderService.updateOrder(order);

        // THEN
        Mockito.verify(orderDAO).update(order);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testUpdateOrder_withInvalidObjectShouldFail() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order  = new Order();
        Mockito.doThrow(new ValidationException("")).when(orderValidator).validateForUpdate(order);

        // WHEN
        orderService.updateOrder(order);

        // THEN
        Mockito.verify(orderDAO, never()).update(order);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testUpdateOrder_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order  = new Order();
        Mockito.doThrow(new DAOException("")).when(orderDAO).update(order);

        // Then
        orderService.updateOrder(order);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"COOK"})
    public void testUpdateOrder_withoutPermissionShouldFail() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order  = createOrder(BigDecimal.valueOf(500),"Order Information", BigDecimal.valueOf(0.2),
                LocalDateTime.now(), Order.State.QUEUED);
        orderService.addOrder(order);

        // WHEN
        order.setState(Order.State.IN_PROGRESS);
        orderService.updateOrder(order);

        // THEN
        Mockito.verify(orderDAO, never()).update(order);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testUpdateOrder_stateNotInQueueAndUpdateAddInfo() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order  = createOrder(BigDecimal.valueOf(500),"Order Information", BigDecimal.valueOf(0.2),
                LocalDateTime.now(), Order.State.IN_PROGRESS);
        orderService.addOrder(order);

        // WHEN
        order.setAdditionalInformation("Info");

        // THEN
        orderService.updateOrder(order);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testUpdateOrder_menuEntryIsNotAllowedToBeUpdated() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order  = createOrder(BigDecimal.valueOf(500),"Order Information", BigDecimal.valueOf(0.2),
                LocalDateTime.now(), Order.State.IN_PROGRESS);
        orderService.addOrder(order);

        // WHEN
        order.setMenuEntry(MenuEntry.withIdentity(1));

        // THEN
        orderService.updateOrder(order);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testUpdateOrder_timeIsNotAllowedToBeUpdated() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order  = createOrder(BigDecimal.valueOf(500),"Order Information", BigDecimal.valueOf(0.2),
                LocalDateTime.now(), Order.State.IN_PROGRESS);
        orderService.addOrder(order);

        // WHEN
        order.setTime(LocalDateTime.now());

        // THEN
        orderService.updateOrder(order);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testCancelOrder_shouldRemove() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order  = createOrder(BigDecimal.valueOf(500),"Order Information", BigDecimal.valueOf(0.2),
                LocalDateTime.now(), Order.State.QUEUED);
        orderService.addOrder(order);

        // WHEN
        orderService.cancelOrder(order);

        // THEN
        Mockito.verify(orderDAO).delete(order);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testCancelOrder_withInvalidObjectShouldFail() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order  = new Order();
        Mockito.doThrow(new ValidationException("")).when(orderValidator).validateForDelete(order);

        // WHEN
        orderService.cancelOrder(order);

        // THEN
        Mockito.verify(orderDAO, never()).delete(order);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testCancelOrder_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order  = new Order();
        Mockito.doThrow(new DAOException("")).when(orderDAO).delete(order);

        // WHEN
       orderService.cancelOrder(order);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"COOK"})
    public void testCancelOrder_withoutPermissionShouldFail() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order  = new Order();

        // WHEN
        orderService.cancelOrder(order);

        // THEN
        Mockito.verify(orderDAO, never()).delete(order);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testFindOrder_shouldReturnObjects() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order  = new Order();
        Mockito.when(orderDAO.find(order)).thenReturn(Arrays.asList(order));

        // WHEN
        List<Order> orders = orderService.findOrder(order);

        // THEN
        assertEquals(1, orders.size());
        assertTrue(orders.contains(order));

    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testGetAllOrderHistories_shouldReturnObjects() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order = new Order();
        History<Order> history = new History<>();
        Mockito.when(orderDAO.getHistory(order)).thenReturn(Arrays.asList(history));

        // WHEN
        List<History<Order>> histories = orderService.getOrderHistory(order);

        // THEN
        assertEquals(1, histories.size());
        assertTrue(histories.contains(history));
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testGetAllOrders_shouldReturnObjects() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order1 = new Order();
        Order order2 = new Order();
        Order order3 = new Order();
        Mockito.when(orderDAO.getAll()).thenReturn(Arrays.asList(order1, order2, order3));

        // WHEN
        List<Order> orders = orderService.getAllOrders();

        // THEN
        assertEquals(3, orders.size());
        assertTrue(orders.contains(order1));
        assertTrue(orders.contains(order2));
        assertTrue(orders.contains(order3));
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testGetAllOrders_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Mockito.doThrow(new DAOException("")).when(orderDAO).getAll();

        // WHEN
        orderService.getAllOrders();
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"COOK"})
    public void testMarkAsInProgress_shouldWork() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order1 = new Order();
        order1.setState(Order.State.QUEUED);

        // WHEN
        orderService.markAsInProgress(order1);

        // THEN
        Mockito.verify(orderDAO).update(order1);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"COOK"})
    public void testMarkAsInProgress_shouldThrowServiceException() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order1 = new Order();
        order1.setState(Order.State.IN_PROGRESS);

        // WHEN
        orderService.markAsInProgress(order1);

        // THEN
        Mockito.verify(orderDAO).update(order1);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"WAITER"})
    public void testMarkAsInProgress_withoutPermissionShouldNotWork() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order1 = new Order();
        order1.setState(Order.State.QUEUED);

        // WHEN
        orderService.markAsInProgress(order1);

        // THEN
        Mockito.verify(orderDAO).update(order1);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"COOK"})
    public void testMarkAsReadyForDeliver_shouldWork() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order1 = new Order();
        order1.setState(Order.State.IN_PROGRESS);

        // WHEN
        orderService.markAsReadyForDelivery(order1);

        // THEN
        Mockito.verify(orderDAO).update(order1);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"COOK"})
    public void testMarkAsReadyForDeliver_shouldThrowServiceException() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order1 = new Order();
        order1.setState(Order.State.READY_FOR_DELIVERY);

        // WHEN
        orderService.markAsReadyForDelivery(order1);

        // THEN
        Mockito.verify(orderDAO).update(order1);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"WAITER"})
    public void testMarkAsReadyForDeliver_withoutPermissionShouldNotWork() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order1 = new Order();
        order1.setState(Order.State.IN_PROGRESS);

        // WHEN
        orderService.markAsReadyForDelivery(order1);

        // THEN
        Mockito.verify(orderDAO).update(order1);
    }


    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testMarkAsDelivered_shouldWork() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order1 = new Order();
        order1.setState(Order.State.READY_FOR_DELIVERY);

        // WHEN
        orderService.markAsDelivered(order1);

        // THEN
        Mockito.verify(orderDAO).update(order1);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testMarkAsDelivered_shouldThrowServiceException() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order1 = new Order();
        order1.setState(Order.State.DELIVERED);

        // WHEN
        orderService.markAsDelivered(order1);

        // THEN
        Mockito.verify(orderDAO).update(order1);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"COOK"})
    public void testMarkAsDelivered_withoutPermissionShouldNotWork() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Order order1 = new Order();
        order1.setState(Order.State.READY_FOR_DELIVERY);

        // WHEN
        orderService.markAsDelivered(order1);

        // THEN
        Mockito.verify(orderDAO).update(order1);
    }

}

package com.at.ac.tuwien.sepm.ss15.edulium.business;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InteriorService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.OrderService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.scene.control.Tab;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by - on 23.06.2015.
 */
public class TestTableBusinessLogic extends AbstractBusinessLogicTest {
    @Autowired
    private InteriorService interiorService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private TableBusinessLogic tableBusinessLogic;
    @Autowired
    private MenuService menuService;

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


    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testAddedOrderToTable_ShouldSetTableUserToOrderUser() throws ValidationException, ServiceException{
        //Prepare
        Order order = createOrder(BigDecimal.valueOf(500), "Order Information", BigDecimal.valueOf(0.2),
                LocalDateTime.now(), Order.State.QUEUED, 1);
        orderService.addOrder(order);
        Section section = new Section();
        section.setName("New Section");
        interiorService.addSection(section);
        Table table = new Table();
        table.setColumn(100);
        table.setRow(100);
        table.setSeats(5);
        table.setSection(section);
        table.setNumber(50L);
        interiorService.addTable(table);

        //WHEN
        order.setTable(table);
        orderService.updateOrder(order);
        tableBusinessLogic.addedOrderToTable(table,order);

        //THEN
        List<Table> results = interiorService.findTables(Table.withIdentity(section, table.getNumber()));

        assertEquals(1, results.size());
        assertEquals(orderService.getOrderSubmitter(order), results.get(0).getUser());
    }

    @Test
    public void testPaidOrderFromTable_ShouldSetTableUserToNull(){

    }
}

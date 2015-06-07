package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.*;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller used for the Manager View
 */
@Component
public class OrdersOverviewController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(OrdersOverviewController.class);

    private OrderService orderService;
    private MenuService menuService;
    private InteriorService interiorService;
    private ArrayList<Button> buttons = new ArrayList<>();

    @FXML
    GridPane categoriesGP;
    @FXML
    GridPane ordersGP;
    @FXML
    VBox entriesVB;

    @Override
    @PreAuthorize("hasRole('SERVICE')")
    public void initialize(URL location, ResourceBundle resources) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/Spring-Edulium.xml");
        orderService = context.getBean("orderService", OrderService.class);
        menuService = context.getBean("menuService", MenuService.class);
        interiorService = context.getBean("interiorService", InteriorService.class);

        try {
            Section section = new Section();
            section.setIdentity(1L);
            section.setName("A");
            interiorService.addSection(section);
            Table table = new Table();
            table.setNumber(1L);
            table.setSection(section);
            table.setRow(1);
            table.setColumn(1);
            table.setSeats(4);
            interiorService.addTable(table);
            Order order = createOrder(BigDecimal.valueOf(500), "Order Information", BigDecimal.valueOf(0.2),
                    LocalDateTime.now(), Order.State.QUEUED, 1);
            //orderService.addOrder(order);
        } catch(ServiceException e) {
            System.out.println(e);
        }/* catch(ValidationException e) {
            System.out.println(e);
        }*/

        try {
            System.out.println(orderService.getAllOrders());
        } catch(ServiceException e) {
        }

        try {
            categoriesGP.setVgap(4);
            categoriesGP.setHgap(150);
            int row = 0;
            int col = 0;
            for(MenuCategory menuCategory : menuService.getAllMenuCategories()) {
                Button button = new Button();
                button.setText(menuCategory.getName());
                button.setPrefSize(240, 40);
                button.setMinWidth(140);
                button.setStyle("-fx-font-size: 18px;");
                button.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t){
                        entriesVB.getChildren().clear();
                        MenuEntry matcher = new MenuEntry();
                        matcher.setCategory(menuCategory);
                        try {
                            int i = 0;
                            for (MenuEntry entry : menuService.findMenuEntry(matcher)) {
                                Button button = new Button();
                                button.setText(entry.getName());
                                button.setPrefSize(240, 40);
                                button.setMinWidth(140);
                                button.setStyle("-fx-font-size: 18px;");
                                entriesVB.getChildren().add(i, button);
                                i++;
                            }
                        } catch(ServiceException e) {
                            System.out.println(e);
                        }
                    }
                });

                if(categoriesGP.getRowConstraints().size() <= row) {
                    Separator sepVert1 = new Separator();
                    categoriesGP.setRowSpan(sepVert1, row);
                }
                buttons.add(button);
                categoriesGP.add(button, col, row);
                if(col == 1) row++;
                //col = (col == 0) ? 1 : 0;
                if(col++ == 1)
                    col = 0;
            }
        } catch(ServiceException e) {
            System.out.println(e);
        }
    }

    public void backButtonClicked(ActionEvent event) {

    }

    public void cashButtonClicked(ActionEvent event) {

    }

    private Order createOrder(BigDecimal value, String additionalInformation,
                              BigDecimal taxRate, LocalDateTime time, Order.State state, int MenuEntryID) throws ServiceException{

        Order order = new Order();
        order.setTable(interiorService.getAllTables().get(0));
        order.setMenuEntry(menuService.getAllMenuEntries().get(MenuEntryID));
        order.setBrutto(value);
        order.setTax(taxRate);
        order.setAdditionalInformation(additionalInformation);
        order.setTime(time);
        order.setState(state);

        return order;
    }
}

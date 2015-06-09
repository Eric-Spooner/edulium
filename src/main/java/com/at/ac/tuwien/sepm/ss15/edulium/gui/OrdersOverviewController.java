package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.*;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
import java.util.*;

/**
 * Controller used for the Manager View
 */
@Component
public class OrdersOverviewController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(OrdersOverviewController.class);

    private OrderService orderService;
    private MenuService menuService;
    private InteriorService interiorService;
    private int ordersRow = 0;
    private LinkedList<OrderEntry> orderEntries = new LinkedList<>();

    @FXML
    GridPane categoriesGP;
    @FXML
    GridPane ordersGP;
    @FXML
    VBox entriesVB;

    @Override
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

        ordersGP.setVgap(4);
        ordersGP.setHgap(4);

        try {
            categoriesGP.setVgap(4);
            categoriesGP.setHgap(150);
            int row = 0;
            int col = 0;
            for(MenuCategory menuCategory : menuService.getAllMenuCategories()) {
                Button buttonCategory = new Button();
                buttonCategory.setText(menuCategory.getName());
                buttonCategory.setPrefSize(240, 40);
                buttonCategory.setMinWidth(140);
                buttonCategory.setStyle("-fx-font-size: 18px;");
                buttonCategory.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t){
                        entriesVB.getChildren().clear();
                        MenuEntry matcher = new MenuEntry();
                        matcher.setCategory(menuCategory);
                        try {
                            int i = 0;
                            for (MenuEntry entry : menuService.findMenuEntry(matcher)) {
                                Button buttonEntry = new Button();
                                buttonEntry.setText(entry.getName());
                                buttonEntry.setPrefSize(240, 40);
                                buttonEntry.setMinWidth(140);
                                buttonEntry.setStyle("-fx-font-size: 18px;");
                                buttonEntry.setOnAction(new EventHandler<ActionEvent>() {
                                    public void handle(ActionEvent t) {
                                        // Check if entry already in orders, then increase amount
                                        /*for(OrderEntry orderEntry : orderEntries) {
                                            if(orderEntry.getEntryId().equals(entry.getIdentity())) {
                                                return;
                                            }
                                        }*/
                                        if(ordersGP.getRowConstraints().size() <= ordersRow) {
                                            Separator sepVert1 = new Separator();
                                            ordersGP.setRowSpan(sepVert1, ordersRow);
                                        }
                                        ordersGP.setMinHeight((ordersRow+1)*44);
                                        Label amountOrdered = new Label();
                                        amountOrdered.setText("1");
                                        amountOrdered.setStyle("-fx-font-size: 18px;");
                                        ordersGP.add(amountOrdered, 0, ordersRow);
                                        OrderEntry orderEntry = new OrderEntry();
                                        orderEntry.setAmount(1);
                                        orderEntry.setEntryId(entry.getIdentity());
                                        orderEntry.setRow(ordersRow);
                                        orderEntries.add(orderEntry);
                                        Button buttonPlus = new Button();
                                        buttonPlus.setText("+");
                                        buttonPlus.setPrefSize(40, 40);
                                        buttonPlus.setMinWidth(40);
                                        buttonPlus.setStyle("-fx-font-size: 18px;");
                                        buttonPlus.setOnAction(new EventHandler<ActionEvent>() {
                                            public void handle(ActionEvent t) {
                                                amountOrdered.setText(String.valueOf(Integer.valueOf(amountOrdered.getText()) + 1));
                                                orderEntry.setAmount(Integer.valueOf(amountOrdered.getText()));
                                            }
                                        });
                                        ordersGP.add(buttonPlus, 1, ordersRow);
                                        Button buttonMinus = new Button();
                                        buttonMinus.setText("-");
                                        buttonMinus.setPrefSize(40, 40);
                                        buttonMinus.setMinWidth(40);
                                        buttonMinus.setStyle("-fx-font-size: 18px;");
                                        buttonMinus.setOnAction(new EventHandler<ActionEvent>() {
                                            public void handle(ActionEvent t) {
                                                // Remove order if amount = 1 and "-" pressed
                                                if(Integer.valueOf(amountOrdered.getText()) <= 1) {
                                                    orderEntries.remove(orderEntry);
                                                    List<Node> children = new LinkedList<Node>(ordersGP.getChildren());
                                                    for (Node node : children) {
                                                        int nodeRow = GridPane.getRowIndex(node);
                                                        if (nodeRow == orderEntry.getRow()) {
                                                            ordersGP.getChildren().remove(node);
                                                        }
                                                        else if (nodeRow > orderEntry.getRow()) {
                                                            ordersGP.setRowIndex(node, nodeRow - 1);
                                                        }
                                                    }
                                                    for(OrderEntry oe : orderEntries) {
                                                        if(oe.getRow() > orderEntry.getRow()) {
                                                            oe.setRow(oe.getRow() - 1);
                                                        }
                                                    }
                                                    ordersGP.getRowConstraints().get(0).setMaxHeight(44);
                                                    ordersRow--;
                                                } else {
                                                    amountOrdered.setText(String.valueOf(Integer.valueOf(amountOrdered.getText()) - 1));
                                                    orderEntry.setAmount(Integer.valueOf(amountOrdered.getText()));
                                                }
                                            }
                                        });
                                        ordersGP.add(buttonMinus, 2, ordersRow);
                                        Text entryName = new Text();
                                        entryName.setText(entry.getName());
                                        entryName.setStyle("-fx-font-size: 18px;");
                                        ordersGP.add(entryName, 3, ordersRow);
                                        ordersRow++;
                                    }
                                });
                                entriesVB.getChildren().add(i, buttonEntry);
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
                categoriesGP.add(buttonCategory, col, row);
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
        String out = new String();
        for(OrderEntry entry : orderEntries) {
            MenuEntry matcher = new MenuEntry();
            matcher.setIdentity(entry.getEntryId());
            MenuEntry en = null;
            try {
                en = menuService.findMenuEntry(matcher).get(0);
            } catch(ServiceException e) {
                System.out.println(e);
            }
            out += en.getName() + ", "+entry.getAmount()+","+entry.getRow()+"\n";
        }
        System.out.println(out);
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

    private class OrderEntry {
        private int amount;
        private Long EntryId;
        private int row;

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public Long getEntryId() {
            return EntryId;
        }

        public void setEntryId(Long entryId) {
            EntryId = entryId;
        }
    }
}

package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.*;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Controller used for the Manager View
 */
@Component
public class OrderOverviewController implements Initializable, Controller {
    private static final Logger LOGGER = LogManager.getLogger(OrderOverviewController.class);

    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private InteriorService interiorService;

    private int ordersRow = 0;
    private LinkedList<OrderEntry> orderEntries = new LinkedList<>();
    private static Table table = null;

    @FXML
    GridPane categoriesGP;
    @FXML
    GridPane ordersGP;
    @FXML
    VBox entriesVB;
    @FXML
    AnchorPane orderAnchor;
    @FXML
    ScrollPane ordersSP;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ApplicationContext context = EduliumApplicationContext.getContext();

        ordersSP.setStyle("-fx-font-size: 40px;");

        ordersGP.setVgap(4);
        ordersGP.setHgap(4);

        orderEntries.clear();

        try {
            categoriesGP.setVgap(4);
            categoriesGP.setHgap(150);
            int row = 0;
            int col = 0;
            for (MenuCategory menuCategory : menuService.getAllMenuCategories()) {
                Button buttonCategory = new Button();
                buttonCategory.setText(menuCategory.getName());
                buttonCategory.setPrefSize(240, 40);
                buttonCategory.setMinWidth(140);
                buttonCategory.setStyle("-fx-font-size: 18px;");
                buttonCategory.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
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
                                        for (OrderEntry orderEntry : orderEntries) {
                                            if (orderEntry.getEntryId().equals(entry.getIdentity())) {
                                                orderEntry.setAmountLabelText(String.valueOf(Integer.valueOf(orderEntry.getAmountLabelText()) + 1));
                                                return;
                                            }
                                        }
                                        if (ordersGP.getRowConstraints().size() <= ordersRow) {
                                            Separator sepVert1 = new Separator();
                                            ordersGP.setRowSpan(sepVert1, ordersRow);
                                        }
                                        ordersGP.setMinHeight((ordersRow + 1) * 44);
                                        if (!orderEntries.isEmpty()) {
                                            orderAnchor.setMinHeight(orderEntries.get(orderEntries.size() - 1).getRow() * 44 + 120);
                                            orderAnchor.setMaxHeight(orderEntries.get(orderEntries.size() - 1).getRow() * 44 + 120);
                                        }
                                        Label amountOrdered = new Label();
                                        amountOrdered.setText("1");
                                        amountOrdered.setStyle("-fx-font-size: 18px;");
                                        ordersGP.add(amountOrdered, 0, ordersRow);
                                        OrderEntry orderEntry = new OrderEntry();
                                        orderEntry.setAmountLabel(amountOrdered);
                                        orderEntry.setAmountLabelText("1");
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
                                                //amountOrdered.setText(String.valueOf(Integer.valueOf(amountOrdered.getText()) + 1));
                                                orderEntry.setAmountLabelText(String.valueOf(Integer.valueOf(orderEntry.getAmountLabelText()) + 1));
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
                                                if (Integer.valueOf(amountOrdered.getText()) <= 1) {
                                                    orderEntries.remove(orderEntry);
                                                    List<Node> children = new LinkedList<Node>(ordersGP.getChildren());
                                                    for (Node node : children) {
                                                        int nodeRow = GridPane.getRowIndex(node);
                                                        if (nodeRow == orderEntry.getRow()) {
                                                            ordersGP.getChildren().remove(node);
                                                        } else if (nodeRow > orderEntry.getRow()) {
                                                            ordersGP.setRowIndex(node, nodeRow - 1);
                                                        }
                                                    }
                                                    for (OrderEntry oe : orderEntries) {
                                                        if (oe.getRow() > orderEntry.getRow()) {
                                                            oe.setRow(oe.getRow() - 1);
                                                        }
                                                    }
                                                    ordersGP.getRowConstraints().get(0).setMaxHeight(44);
                                                    orderAnchor.setMinHeight(orderEntries.get(orderEntries.size() - 1).getRow() * 44 + 88);
                                                    orderAnchor.setMaxHeight(orderEntries.get(orderEntries.size() - 1).getRow() * 44 + 88);
                                                    ordersRow--;
                                                } else {
                                                    //amountOrdered.setText(String.valueOf(Integer.valueOf(amountOrdered.getText()) - 1));
                                                    orderEntry.setAmountLabelText(String.valueOf(Integer.valueOf(orderEntry.getAmountLabelText()) - 1));
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
                        } catch (ServiceException e) {
                            showErrorDialog("Error", "Cannot retrieve menus", "There is a problem with accessing the database " + e);
                        }
                    }
                });

                if (categoriesGP.getRowConstraints().size() <= row) {
                    Separator sepVert1 = new Separator();
                    categoriesGP.setRowSpan(sepVert1, row);
                }
                categoriesGP.add(buttonCategory, col, row);
                if (col == 1) row++;
                //col = (col == 0) ? 1 : 0;
                if (col++ == 1)
                    col = 0;
            }
        } catch (ServiceException e) {
            showErrorDialog("Error", "Cannot retrieve menus", "There is a problem with accessing the database " + e);
        }
    }

    public static void setSelectedTable(Table table) {
        OrderOverviewController.table = table;
    }

    public void backButtonClicked(ActionEvent event) {
    }

    public void commitButtonClicked(ActionEvent event) {
        //String out = new String();
        for (OrderEntry entry : orderEntries) {
            MenuEntry matcher = new MenuEntry();
            matcher.setIdentity(entry.getEntryId());
            MenuEntry en = null;
            try {
                en = menuService.findMenuEntry(matcher).get(0);

                for(int i = 0; i < Integer.valueOf(entry.getAmountLabelText()); i++) {
                    Order order = new Order();
                    order.setTable(table);
                    order.setMenuEntry(en);
                    order.setBrutto(en.getPrice());
                    order.setTax(en.getTaxRate().getValue());
                    order.setAdditionalInformation("No additional information"); //TODO additional information?
                    order.setTime(LocalDateTime.now());
                    order.setState(Order.State.QUEUED);
                    orderService.addOrder(order);
                }
            } catch (ServiceException e) {
                showErrorDialog("Error", "Cannot store order", "There is a problem with accessing the database " + e);
            } catch (ValidationException e) {
                showErrorDialog("Error", "Cannot store order", "There is a problem with accessing the database " + e);
            }
            //out += en.getName() + ", " + entry.getAmountLabelText() + "," + entry.getRow() + "\n";
        }
        //System.out.println(out);
    }

    @Override
    public void disable(boolean disabled) {

    }

    private class OrderEntry {
        private Label amountLb;
        private Long EntryId;
        private int row;

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public String getAmountLabelText() {
            return amountLb.getText();
        }

        public void setAmountLabel(Label amountLb) {
            this.amountLb = amountLb;
        }

        public void setAmountLabelText(String amount) {
            this.amountLb.setText(amount);
        }

        public Long getEntryId() {
            return EntryId;
        }

        public void setEntryId(Long entryId) {
            EntryId = entryId;
        }
    }

    public static void showErrorDialog(String title, String head, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(head);
        alert.setContentText(content);

        alert.showAndWait();
    }
}

package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InteriorService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import sun.font.FontScalerException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public class TableOverviewController implements Initializable, Controller {
    @FXML
    private Canvas tablesCanvas;
    @FXML
    private Label tableIdLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label numberPersonsLabel;
    @FXML
    private AnchorPane anchor;
    @FXML
    private AnchorPane anchorLeft;
    @FXML
    private AnchorPane anchorRight;
    @FXML
    private ScrollPane scrollPaneLeft;

    @Autowired
    private InteriorService interiorService;

    private ArrayList<Rect> rects = new ArrayList<Rect>();
    private double scaleX = 1.0;
    private double scaleY = 1.0;

    private final int FACT = 10;
    private final int CANVAS_PADDING = 20;
    private final int TABLE_SIZE = 40;
    private final int SECTION_OFFSET = 40;
    private final int SECTION_PADDING = 10;
    private final int TEXT_BORDER_BOTTOM = 2;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        ApplicationContext context = EduliumApplicationContext.getContext();

        drawCanvas();

        tablesCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                for (Rect rect : rects) {
                    try {
                        Table clickedTable = rect.getTable(t.getX(), t.getY());
                        if (clickedTable != null) {
                            System.out.println("Table " + clickedTable);
                            OrderOverviewController.setSelectedTable(clickedTable);
                            FXMLPane orderViewPane = context.getBean("orderOverviewPane", FXMLPane.class);
                            /*Stage stage = new Stage();
                            stage.setTitle("OrderOverview");
                            Scene scene = new Scene(orderViewPane);
                            stage.setScene(scene);
                            stage.showAndWait();*/
                            StackPane orderStackPane = new StackPane();
                            orderStackPane.getChildren().setAll(orderViewPane);
                            Scene orderScene = new Scene(orderStackPane);
                            Stage orderStage = new Stage();
                            orderStage.setTitle("Orders Overview");
                            orderStage.setScene(orderScene);
                            orderStage.show();
                            tableIdLabel.setText(String.valueOf(clickedTable.getNumber()));
                        }
                    } catch (ServiceException e) {
                        showErrorDialog("Error", "Cannot retrieve tables", "There is a problem with accessing the database " + e);
                    }

                }
            }
        });

        scrollPaneLeft.setPrefSize(120, 120);
        scrollPaneLeft.setContent(tablesCanvas);
        scrollPaneLeft.setStyle("-fx-font-size: 40px;");
        /*scrollPaneLeft.vvalueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                System.out.println(new_val.intValue());
            }
        });*/

        setupListeners();
    }

    public void setupListeners() {
        anchorLeft.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                tablesCanvas.setWidth(tablesCanvas.getWidth() - (oldSceneWidth.intValue() - newSceneWidth.intValue()));
                scaleX = newSceneWidth.doubleValue() / 550.0;
                scaleX = Math.min(scaleX, 2.0);
                scaleX = Math.max(scaleX, 0.5);
                drawCanvas();
            }
        });
        anchorLeft.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                tablesCanvas.setHeight(tablesCanvas.getHeight() - (oldSceneHeight.intValue() - newSceneHeight.intValue()));
                scaleY = newSceneHeight.doubleValue() / 598.0;
                scaleY = Math.min(scaleY, 2.0);
                scaleY = Math.max(scaleY, 0.5);
                drawCanvas();
            }
        });
    }

    @FXML
    public void filterButtonClicked(ActionEvent event) {

    }

    @FXML
    public void reservationButtonClicked(ActionEvent event) {

    }

    private int calculateWidth(Section section) throws ServiceException {
        int max = -1;
        Table matcher = new Table();
        matcher.setSection(section);
        for (Table table : interiorService.findTables(matcher)) {
            if (table.getColumn() > max) {
                max = table.getColumn();
            }
        }
        return max * FACT + TABLE_SIZE + 2 * SECTION_PADDING;
    }

    private int calculateHeight(Section section) throws ServiceException {
        int max = -1;
        Table matcher = new Table();
        matcher.setSection(section);
        for (Table table : interiorService.findTables(matcher)) {
            if (table.getRow() > max) {
                max = table.getRow();
            }
        }
        return max * FACT + TABLE_SIZE + 2 * SECTION_PADDING;
    }

    @Override
    public void disable(boolean disabled) {

    }

    private class Rect {
        private double x, y, w, h;
        private Section section;
        private long number;

        public Rect(double x, double y, double w, double h, Section section, long number) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.section = section;
            this.number = number;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getW() {
            return w;
        }

        public double getH() {
            return h;
        }

        public long getNumber() { return number; }

        public Table getTable(double x, double y) throws ServiceException {
            if (x >= this.x && x <= this.x + this.w && y >= this.y && y <= this.y + this.h) {
                Table matcher = new Table();
                matcher.setNumber(number);
                matcher.setSection(section);
                return interiorService.findTables(matcher).get(0);
            }

            return null;
        }
    }

    private void drawCanvas() {
        GraphicsContext gc = tablesCanvas.getGraphicsContext2D();
        double scaleText = scaleY;
        boolean firstSection = true;
        Section prevSection = null;
        int rowHeight = 0;
        int x = CANVAS_PADDING;
        int y = CANVAS_PADDING;
        tablesCanvas.setWidth(anchorLeft.getWidth() - 60);

        gc.clearRect(0, 0, tablesCanvas.getWidth(), tablesCanvas.getHeight());

        try {
            for (Section section : interiorService.getAllSections()) {
                if (firstSection) {
                    firstSection = false;
                } else {
                    if (x * scaleX + calculateWidth(prevSection) * scaleX + SECTION_PADDING * scaleX + CANVAS_PADDING * scaleX + calculateWidth(section) * scaleX < tablesCanvas.getWidth()) {
                        x += calculateWidth(prevSection) + SECTION_PADDING * scaleX;
                    } else {
                        x = CANVAS_PADDING;
                        y += rowHeight + SECTION_OFFSET * scaleY;
                        rowHeight = 0;
                    }
                }

                tablesCanvas.setHeight(y * scaleY + calculateHeight(section) * scaleY + CANVAS_PADDING * scaleY);
                gc.strokeRoundRect(x * scaleX, y * scaleY, calculateWidth(section) * scaleX, calculateHeight(section) * scaleY, 10, 10);
                gc.setFont(new Font(gc.getFont().getName(), 20 * scaleText));
                gc.fillText(section.getName() + ":", x * scaleX, (y - TEXT_BORDER_BOTTOM) * scaleY);
                Table matcher = new Table();
                matcher.setSection(section);
                for (Table table : interiorService.findTables(matcher)) {
                    Rect rect = new Rect(((x + SECTION_PADDING) + (table.getColumn() * FACT)) * scaleX, ((y + SECTION_PADDING) + (table.getRow() * FACT)) * scaleY, TABLE_SIZE * scaleX, TABLE_SIZE * scaleY, section, table.getNumber());
                    gc.strokeRoundRect(rect.getX(), rect.getY(), rect.getW(), rect.getH(), 2, 2);
                    // Remove old rect and add new rect with changed parameters
                    Iterator<Rect> iter = rects.iterator();
                    while(iter.hasNext()) {
                        Rect current = iter.next();
                        if(current.getNumber() == table.getNumber()) {
                            iter.remove();
                        }
                    }
                    rects.add(rect);
                    gc.fillText(String.valueOf(table.getNumber()), ((x + SECTION_PADDING) + (table.getColumn() * FACT) + TABLE_SIZE / 4) * scaleX, ((y + SECTION_PADDING) + (table.getRow() * FACT) + TABLE_SIZE / 1.5) * scaleY);
                }
                rowHeight = Math.max(rowHeight, calculateHeight(section));
                prevSection = section;
            }
        } catch (ServiceException e) {
            showErrorDialog("Error", "Cannot retrieve sections", "There is a problem with accessing the database " + e);
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

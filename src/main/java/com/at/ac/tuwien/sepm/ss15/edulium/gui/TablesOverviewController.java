package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sun.font.FontScalerException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
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

    private static InteriorService interiorService;

    private ArrayList<Rect> rects = new ArrayList<Rect>();
    private double scaleX = 1.0;
    private double scaleY = 1.0;

    private final int FACT = 10;
    private final int CANVAS_PADDING = 20;
    private final int TABLE_SIZE = 40;
    private final int SECTION_OFFSET = 40;
    private final int SECTION_PADDING = 10;
    private final int TEXT_BORDER_BOTTOM = 2;

    public static void setInteriorService(InteriorService interiorService) {
        Controller.interiorService = interiorService;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Section section1 = new Section();
        section1.setIdentity((long)1);
        section1.setName("Garten");
        Table table1 = new Table();
        table1.setSection(section1);
        table1.setRow(0);
        table1.setColumn(0);
        table1.setNumber((long)1);
        table1.setSeats(4);
        Table table2 = new Table();
        table2.setSection(section1);
        table2.setRow(5);
        table2.setColumn(0);
        table2.setNumber((long)2);
        table2.setSeats(4);
        Table table3 = new Table();
        table3.setSection(section1);
        table3.setRow(5);
        table3.setColumn(5);
        table3.setNumber((long)3);
        table3.setSeats(4);

        Section section2 = new Section();
        section2.setIdentity((long)2);
        section2.setName("Bar");
        Table table4 = new Table();
        table4.setSection(section2);
        table4.setRow(0);
        table4.setColumn(0);
        table4.setNumber((long)4);
        table4.setSeats(4);
        Table table5 = new Table();
        table5.setSection(section2);
        table5.setRow(5);
        table5.setColumn(5);
        table5.setNumber((long)5);
        table5.setSeats(4);
        Table table6 = new Table();
        table6.setSection(section2);
        table6.setRow(0);
        table6.setColumn(10);
        table6.setNumber((long)6);
        table6.setSeats(4);

        Section section3 = new Section();
        section3.setIdentity((long)3);
        section3.setName("Saal");
        Table table7 = new Table();
        table7.setSection(section3);
        table7.setRow(0);
        table7.setColumn(0);
        table7.setNumber((long)4);
        table7.setSeats(4);
        Table table8 = new Table();
        table8.setSection(section3);
        table8.setRow(5);
        table8.setColumn(5);
        table8.setNumber((long)5);
        table8.setSeats(4);
        Table table9 = new Table();
        table9.setSection(section3);
        table9.setRow(5);
        table9.setColumn(10);
        table9.setNumber((long)6);
        table9.setSeats(4);

        Section section4 = new Section();
        section4.setIdentity((long) 4);
        section4.setName("Gang");
        Table table10 = new Table();
        table10.setSection(section4);
        table10.setRow(0);
        table10.setColumn(0);
        table10.setNumber((long)4);
        table10.setSeats(4);
        Table table11 = new Table();
        table11.setSection(section4);
        table11.setRow(5);
        table11.setColumn(5);
        table11.setNumber((long)5);
        table11.setSeats(4);

        try {
            interiorService.addSection(section1);
            interiorService.addTable(table1);
            interiorService.addTable(table2);
            interiorService.addTable(table3);
            interiorService.addSection(section2);
            interiorService.addTable(table4);
            interiorService.addTable(table5);
            interiorService.addTable(table6);
            interiorService.addSection(section3);
            interiorService.addTable(table7);
            interiorService.addTable(table8);
            interiorService.addTable(table9);
            interiorService.addSection(section4);
            interiorService.addTable(table10);
            interiorService.addTable(table11);
        } catch (ServiceException e) {
            //TODO alert
        }

        drawCanvas();

        tablesCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                for(Rect rect : rects) {
                    try {
                        Table clickedTable = rect.getTable(t.getX(), t.getY());
                        if(clickedTable != null) {
                            tableIdLabel.setText(String.valueOf(clickedTable.getNumber()));
                        }
                    } catch(ServiceException e) {
                        //TODO alert
                    }

                }
            }
        });

        scrollPaneLeft.setPrefSize(120, 120);
        scrollPaneLeft.setContent(tablesCanvas);
        scrollPaneLeft.setStyle("-fx-font-size: 40px;");
        scrollPaneLeft.vvalueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                System.out.println(new_val.intValue());
            }
        });

        setupListeners();
    }

    public void setupListeners() {
        anchorLeft.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                tablesCanvas.setWidth(tablesCanvas.getWidth() - (oldSceneWidth.intValue() - newSceneWidth.intValue()));
                scaleX = newSceneWidth.doubleValue()/550.0;
                scaleX = Math.min(scaleX, 2.0);
                scaleX = Math.max(scaleX, 0.5);
                drawCanvas();
            }
        });
        anchorLeft.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                tablesCanvas.setHeight(tablesCanvas.getHeight() - (oldSceneHeight.intValue() - newSceneHeight.intValue()));
                scaleY = newSceneHeight.doubleValue()/598.0;
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
        for(Table table : interiorService.findTables(matcher)) {
            if(table.getColumn() > max) {
                max = table.getColumn();
            }
        }
        return max*FACT + TABLE_SIZE + 2*SECTION_PADDING;
    }

    private int calculateHeight(Section section) throws ServiceException {
        int max = -1;
        Table matcher = new Table();
        matcher.setSection(section);
        for(Table table : interiorService.findTables(matcher)) {
            if(table.getRow() > max) {
                max = table.getRow();
            }
        }
        return max*FACT + TABLE_SIZE + 2*SECTION_PADDING;
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

        public Table getTable(double x, double y) throws ServiceException {
            if(x >= this.x && x <= this.x+this.w && y >= this.y && y <= this.y+this.h) {
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
        tablesCanvas.setWidth(anchorLeft.getWidth()-60);

        gc.clearRect(0, 0, tablesCanvas.getWidth(), tablesCanvas.getHeight());

        try {
            for (Section section : interiorService.getAllSections()) {
                if(firstSection) {
                    firstSection = false;
                } else {
                    if(x*scaleX+calculateWidth(prevSection)*scaleX+SECTION_PADDING*scaleX+CANVAS_PADDING*scaleX+calculateWidth(section)*scaleX < tablesCanvas.getWidth()) {
                        x += calculateWidth(prevSection)+SECTION_PADDING*scaleX;
                    } else {
                        x = CANVAS_PADDING;
                        y += rowHeight+SECTION_OFFSET*scaleY;
                        rowHeight = 0;
                    }
                }

                tablesCanvas.setHeight(y+calculateHeight(section)*scaleY+CANVAS_PADDING);
                gc.strokeRoundRect(x*scaleX, y*scaleY, calculateWidth(section)*scaleX, calculateHeight(section)*scaleY, 10, 10);
                gc.setFont(new Font(gc.getFont().getName(), 20 * scaleText));
                gc.fillText(section.getName() + ":", x*scaleX, (y-TEXT_BORDER_BOTTOM)*scaleY);
                Table matcher = new Table();
                matcher.setSection(section);
                for (Table table : interiorService.findTables(matcher)) {
                    Rect rect = new Rect(((x+SECTION_PADDING)+(table.getColumn()*FACT))*scaleX, ((y+SECTION_PADDING)+(table.getRow()*FACT))*scaleY, TABLE_SIZE*scaleX, TABLE_SIZE*scaleY, section, table.getNumber());
                    gc.strokeRoundRect(rect.getX(), rect.getY(), rect.getW(), rect.getH(), 2, 2);
                    rects.add(rect);
                    gc.fillText(String.valueOf(table.getNumber()), ((x+SECTION_PADDING)+(table.getColumn()*FACT)+TABLE_SIZE/4)*scaleX, ((y+SECTION_PADDING)+(table.getRow()*FACT)+TABLE_SIZE/1.5)*scaleY);
                }
                rowHeight = Math.max(rowHeight, calculateHeight(section));
                prevSection = section;
            }
        } catch(ServiceException e) {
            //TODO Alert
        }
    }
}
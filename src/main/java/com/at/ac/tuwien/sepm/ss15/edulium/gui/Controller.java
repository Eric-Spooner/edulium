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
    //private int MARGIN = 20;
    //private int TABLE_SIZE = 40;
    private double scaleX = 1.0;
    private double scaleY = 1.0;
    private static InteriorService interiorService;
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
        section2.setIdentity((long)1);
        section2.setName("Bar");
        Table table4 = new Table();
        table4.setSection(section2);
        table4.setRow(0);
        table4.setColumn(0);
        table4.setNumber((long)4);
        table4.setSeats(4);
        Table table5 = new Table();
        table5.setSection(section2);
        table5.setRow(0);
        table5.setColumn(5);
        table5.setNumber((long)5);
        table5.setSeats(4);
        Table table6 = new Table();
        table6.setSection(section2);
        table6.setRow(0);
        table6.setColumn(30);
        table6.setNumber((long)6);
        table6.setSeats(4);

        try {
            interiorService.addSection(section1);
            interiorService.addTable(table1);
            interiorService.addTable(table2);
            interiorService.addTable(table3);
            interiorService.addSection(section2);
            interiorService.addTable(table4);
            interiorService.addTable(table5);
            interiorService.addTable(table6);
        } catch (ServiceException e) {

        }

        /*rooms = new ArrayList<Room>();
        Room room1 = new Room("Garten");
        room1.add(new Table(1, 20, 20, 40, 40));
        room1.add(new Table(2, 80, 20, 40, 40));
        room1.add(new Table(3, 20, 80, 40, 40));
        room1.add(new Table(4, 80, 80, 40, 40));
        room1.add(new Table(5, 140, 20, 40, 40));
        rooms.add(room1);
        Room room2 = new Room("Bar");
        room2.add(new Table(6, 20, 20, 40, 40));
        room2.add(new Table(7, 80, 20, 40, 40));
        room2.add(new Table(8, 140, 20, 40, 40));
        room2.add(new Table(9, 140, 80, 40, 40));
        room2.add(new Table(10, 200, 20, 40, 40));
        room2.add(new Table(11, 140, 140, 40, 40));
        rooms.add(room2);
        Room room3 = new Room("Gang");
        room3.add(new Table(12, 20, 20, 40, 40));
        room3.add(new Table(13, 20, 80, 40, 40));
        room3.add(new Table(14, 20, 140, 40, 40));
        room3.add(new Table(15, 20, 200, 40, 40));
        room3.add(new Table(16, 20, 260, 40, 40));
        room3.add(new Table(17, 20, 320, 40, 40));
        rooms.add(room3);

        //Occupy random tables
        room1.getTables().get(3).setOccupied(true);
        room1.getTables().get(4).setOccupied(true);
        room2.getTables().get(1).setOccupied(true);
        room2.getTables().get(2).setOccupied(true);
        room2.getTables().get(5).setOccupied(true);
*/
        drawCanvas();

        tablesCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                /*int roomOffset = 0;
                try {
                    for (Section section : interiorService.getAllSections()) {
                        if (t.getY() <= (roomOffset + getHeight(section) + MARGIN) * scaleY) {
                            Table table = getTable(section, (int) ((t.getX()) / scaleX) - 2*MARGIN, (int) ((t.getY() - roomOffset) / scaleY) - 2*MARGIN);
                            if (table != null) {
                                tableIdLabel.setText(String.valueOf(table.getNumber()));
                            }
                            return;
                        }
                        roomOffset += (getHeight(section) + 2 * MARGIN) * scaleY;
                    }
                } catch(ServiceException e) {

                }*/
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

    /*private int getHeight(Section section) throws ServiceException {
        int max = -1;
        Table matcher = new Table();
        matcher.setSection(section);
        for(Table table : interiorService.findTables(matcher)) {
            if(table.getRow()*TABLE_SIZE+TABLE_SIZE > max) {
                max = table.getRow()*TABLE_SIZE+TABLE_SIZE;
            }
        }
        return max;
    }

    private int getWidth(Section section) throws ServiceException {
        int max = -1;
        Table matcher = new Table();
        matcher.setSection(section);
        for(Table table : interiorService.findTables(matcher)) {
            if(table.getColumn()*TABLE_SIZE+TABLE_SIZE > max) {
                max = table.getColumn()*TABLE_SIZE+TABLE_SIZE;
            }
        }
        return max;
    }

    public Table getTable(Section section, int x, int y) throws ServiceException {
        Table matcher = new Table();
        matcher.setSection(section);
        for(Table table : interiorService.findTables(matcher)) {
            if(table.getColumn()*TABLE_SIZE <= x && table.getColumn()*TABLE_SIZE+TABLE_SIZE >= x
                    &&table.getRow()*TABLE_SIZE <= y && table.getRow()*TABLE_SIZE+TABLE_SIZE >= y)
                return table;
        }
        return null;
    }*/

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

    private void drawCanvas() {
        GraphicsContext gc = tablesCanvas.getGraphicsContext2D();
        double scaleText = scaleY;
        boolean firstSection = true;
        Section prevSection = null;
        int x = CANVAS_PADDING;
        int y = CANVAS_PADDING;
        tablesCanvas.setWidth(anchorLeft.getWidth()-60);

        gc.clearRect(0, 0, tablesCanvas.getWidth(), tablesCanvas.getHeight());

        try {
            for (Section section : interiorService.getAllSections()) {
                if(firstSection) {
                    firstSection = false;
                } else {
                    if(x+calculateWidth(prevSection)*scaleX+CANVAS_PADDING+calculateWidth(section)*scaleX < tablesCanvas.getWidth()) {
                        x += calculateWidth(prevSection)+SECTION_PADDING*scaleX;
                    } else {
                        x = CANVAS_PADDING;
                        y += calculateHeight(prevSection)+SECTION_OFFSET*scaleY;
                    }
                }

                gc.strokeRoundRect(x*scaleX, y*scaleY, calculateWidth(section)*scaleX, calculateHeight(section)*scaleY, 10, 10);
                gc.setFont(new Font(gc.getFont().getName(), 20 * scaleText));
                gc.fillText(section.getName() + ":", x*scaleX, (y-TEXT_BORDER_BOTTOM)*scaleY);
                Table matcher = new Table();
                matcher.setSection(section);
                for (Table table : interiorService.findTables(matcher)) {
                    gc.strokeRoundRect(((x+SECTION_PADDING)+(table.getColumn()*FACT))*scaleX, ((y+SECTION_PADDING)+(table.getRow()*FACT))*scaleY, TABLE_SIZE*scaleX, TABLE_SIZE*scaleY, 2, 2);
                    gc.fillText(String.valueOf(table.getNumber()), ((x+SECTION_PADDING)+(table.getColumn()*FACT)+TABLE_SIZE/4)*scaleX, ((y+SECTION_PADDING)+(table.getRow()*FACT)+TABLE_SIZE/1.5)*scaleY);
                }
                prevSection = section;
            }
        } catch(ServiceException e) {
            //TODO Alert
        }

        /*tablesCanvas.setHeight(MARGIN);
        GraphicsContext gc = tablesCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, tablesCanvas.getWidth(), tablesCanvas.getHeight());

        double scaleText = scaleY; //Math.min(scaleX, scaleY);

        int roomOffset = 0;
        try {
            for (Section section : interiorService.getAllSections()) {
                tablesCanvas.setHeight(tablesCanvas.getHeight() + (getHeight(section) + 2 * MARGIN) * scaleY);
                gc.strokeRoundRect(MARGIN * scaleX, (roomOffset + MARGIN) * scaleY, (getWidth(section) + MARGIN) * scaleX, (getHeight(section) + MARGIN) * scaleY, 10, 10);
                gc.setFont(new Font(gc.getFont().getName(), 20 * scaleText));
                gc.fillText(section.getName() + ":", MARGIN * scaleX, (roomOffset + MARGIN - 2) * scaleY);
                Table matcher = new Table();
                matcher.setSection(section);
                for (Table table : interiorService.findTables(matcher)) {
                    gc.strokeRoundRect((table.getColumn()*TABLE_SIZE+2*MARGIN) * scaleX, (roomOffset + 2*MARGIN + table.getRow()*TABLE_SIZE) * scaleY, TABLE_SIZE * scaleX, TABLE_SIZE * scaleY, 2, 2);
                if(table.isOccupied()) {
                    gc.setFill(Color.ORANGERED);
                    gc.fillRoundRect((table.getX() + MARGIN)*scaleX, (roomOffset + MARGIN + table.getY())*scaleY, table.getWidth()*scaleX, table.getHeight()*scaleY, 2, 2);
                    gc.setFill(Color.BLACK);
                }
                    gc.fillText(String.valueOf(table.getNumber()), (table.getColumn()*TABLE_SIZE + 2.5 * MARGIN) * scaleX, (table.getRow()*TABLE_SIZE + roomOffset + 3.5 * MARGIN) * scaleY);
                }
                roomOffset += getHeight(section) + 2 * MARGIN;
            }
        } catch(ServiceException e) {

        }*/
    }
}

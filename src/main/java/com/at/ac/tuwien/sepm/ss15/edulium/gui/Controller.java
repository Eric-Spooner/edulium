package com.at.ac.tuwien.sepm.ss15.edulium.gui;

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
    private int MARGIN = 20;
    private List<Room> rooms;
    private double scaleX = 1.0;
    private double scaleY = 1.0;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        rooms = new ArrayList<Room>();
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

        drawCanvas();

        tablesCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                int roomOffset = 0;
                for (Room room : rooms) {
                    if (t.getY() <= (roomOffset + room.getHeight() + MARGIN)*scaleY) {
                        Table table = room.getTable((int)((t.getX())/scaleX) - MARGIN, (int) ((t.getY() - roomOffset)/scaleY) - MARGIN);
                        if (table != null) {
                            tableIdLabel.setText(String.valueOf(table.getId()));
                        }
                        return;
                    }
                    roomOffset += (room.getHeight() + 2 * MARGIN)*scaleY;
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

    private void drawCanvas() {
        tablesCanvas.setHeight(MARGIN);
        GraphicsContext gc = tablesCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, tablesCanvas.getWidth(), tablesCanvas.getHeight());

        double scaleText = Math.min(scaleX, scaleY);

        int roomOffset = 0;
        for(Room room : rooms) {
            tablesCanvas.setHeight(tablesCanvas.getHeight()+(room.getHeight()+2*MARGIN)*scaleY);
            gc.strokeRoundRect(MARGIN*scaleX, (roomOffset+MARGIN)*scaleY, (room.getWidth()+MARGIN)*scaleX, (room.getHeight()+MARGIN)*scaleY, 10, 10);
            gc.setFont(new Font(gc.getFont().getName(), 20*scaleText));
            gc.fillText(room.getName() + ":", MARGIN*scaleX, (roomOffset+MARGIN-2)*scaleY);
            for(Table table : room.getTables()) {
                gc.strokeRoundRect((table.getX() + MARGIN)*scaleX, (roomOffset + MARGIN + table.getY())*scaleY, table.getWidth()*scaleX, table.getHeight()*scaleY, 2, 2);
                if(table.isOccupied()) {
                    gc.setFill(Color.ORANGERED);
                    gc.fillRoundRect((table.getX() + MARGIN)*scaleX, (roomOffset + MARGIN + table.getY())*scaleY, table.getWidth()*scaleX, table.getHeight()*scaleY, 2, 2);
                    gc.setFill(Color.BLACK);
                }
                gc.fillText(String.valueOf(table.getId()), (table.getX()+1.5*MARGIN)*scaleX, (table.getY()+roomOffset+2.5*MARGIN)*scaleY);
            }
            roomOffset += room.getHeight()+2*MARGIN;
        }
    }
}

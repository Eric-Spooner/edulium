package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InteriorService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller used for Edit Section window
 */
@Component
public class EditSectionController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(EditSectionController.class);

    private boolean createTable = false;
    private boolean moveTable = false;
    private boolean removeTable = false;
    private boolean updateTable = false;
    private static final int FACT = 40;
    private static final int TABLE_SIZE = 40;
    private static InteriorService interiorService;
    private static Stage thisStage;
    private static RoomViewController.UpdateCanvas updateCanvas;
    private static ArrayList<Rect> rects = new ArrayList<Rect>();
    private Rect movingRect;
    private int prevX = 0;
    private int prevY = 0;
    private static Long sectionId;
    private static String sectionName;

    @FXML
    private Canvas canvas;
    @FXML
    private TextField seatsTF;
    @FXML
    private TextField numberTF;
    @FXML
    private Label sectionNameLb;

    public static void setSectionId(Long sectionId) {
        EditSectionController.sectionId = sectionId;
    }

    // Only called in editing mode, initalizes tables on the canvas
    public void initTables() {
        rects.clear();
        Section sectionMatcher = new Section();
        sectionMatcher.setIdentity(sectionId);

        try {
            Section section = interiorService.findSections(sectionMatcher).get(0);
            sectionName = section.getName();
            Table tableMatcher = new Table();
            tableMatcher.setSection(section);
            for(Table table : interiorService.findTables(tableMatcher)) {
                Rect rect = new Rect(table.getColumn()*FACT, table.getRow()*FACT, TABLE_SIZE, TABLE_SIZE, interiorService);
                rect.setNumber(table.getNumber());
                rect.setSeats(table.getSeats());
                rects.add(rect);
            }
        } catch(ServiceException e) {
            showErrorDialog("Error", "Database problem", "Could not access database!");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        LOGGER.info("Initializing Edit Section Controller");
        initTables();
        seatsTF.setText("6");
        numberTF.setText("1");
        sectionNameLb.setText(sectionName);
        drawCanvas();

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                try {
                    if (createTable) {
                        if (numberTF.getText().isEmpty()) {
                            showErrorDialog("Error", "Number missing", "Please insert a number for the table!");
                        } else if (Long.valueOf(numberTF.getText()) < 1) {
                            showErrorDialog("Error", "Number invalid", "The table number must be >= 1!");
                        } else if (seatsTF.getText().isEmpty()) {
                            showErrorDialog("Error", "Seats missing", "Please insert the number of seats for the table!");
                        } else if (Integer.valueOf(seatsTF.getText()) < 0) {
                            showErrorDialog("Error", "Seats invalid", "The number of seats must be >= 0!");
                        } else {
                            boolean intersectsWithExistingTable = false;
                            GraphicsContext gc = canvas.getGraphicsContext2D();

                            //Check if table number is not used in this section
                            for (Rect iteratingRect : rects) {
                                if (iteratingRect.getNumber() == Long.valueOf(numberTF.getText())) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Error");
                                    alert.setHeaderText("Number already used");
                                    alert.setContentText("The table number is already used in this section!");
                                    alert.showAndWait();
                                    return;
                                }
                            }

                            Rect rect = new Rect(Math.max(((((int) t.getX())) / FACT) * FACT, 0), Math.max(((((int) t.getY())) / FACT) * FACT, 0), TABLE_SIZE, TABLE_SIZE, interiorService);
                            for (Rect iteratingRect : rects) {
                                if (iteratingRect.getRect(rect.getX() + 1, rect.getY() + 1) != null ||
                                        iteratingRect.getRect(rect.getX() + TABLE_SIZE - 1, rect.getY() + 1) != null ||
                                        iteratingRect.getRect(rect.getX() + 1, rect.getY() + TABLE_SIZE - 1) != null ||
                                        iteratingRect.getRect(rect.getX() + TABLE_SIZE - 1, rect.getY() + TABLE_SIZE - 1) != null) {
                                    intersectsWithExistingTable = true;
                                }
                            }
                            if (!intersectsWithExistingTable) {
                                rect.setNumber(Long.valueOf(numberTF.getText()));
                                rect.setSeats(Integer.valueOf(seatsTF.getText()));
                                gc.strokeRoundRect(rect.getX(), rect.getY(), rect.getW(), rect.getH(), 2, 2);
                                numberTF.setText(String.valueOf(Integer.valueOf(numberTF.getText()) + 1));
                                rects.add(rect);
                            }
                        }
                    } else if (removeTable) {
                        Rect deleteRect = null;
                        for (Rect rect : rects) {
                            if (rect.getRect(t.getX(), t.getY()) != null) {
                                deleteRect = rect;
                            }
                        }
                        if (deleteRect != null)
                            rects.remove(deleteRect);
                        drawCanvas();
                    } else if (updateTable) {
                        for (Rect rect : rects) {
                            if (rect.getRect(t.getX(), t.getY()) != null) {
                                try {
                                    Stage stage = new Stage();
                                    UpdateTableController.setThisStage(stage);
                                    UpdateTableController.setRects(rects);
                                    UpdateTableController.setClickedRect(rect);
                                    UpdateTableController.setEditUpdateCanvas(new UpdateCanvas());
                                    stage.setTitle("");
                                    AnchorPane myPane = FXMLLoader.load(getClass().getResource("/gui/UpdateTable.fxml"));
                                    Scene scene = new Scene(myPane);
                                    stage.setScene(scene);
                                    stage.initModality(Modality.APPLICATION_MODAL);
                                    stage.setResizable(false);
                                    stage.showAndWait();
                                } catch (IOException e) {
                                    LOGGER.error("Unable to Load Update table" + e);
                                }
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    showErrorDialog("Error", "Invalid value", "Only valid numbers are allowed!");
                }
                drawCanvas();
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if(createTable) {
                    drawCanvas();
                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    gc.setStroke(Color.GRAY);
                    gc.strokeRoundRect(Math.max(((((int)t.getX()))/FACT)*FACT, 0), Math.max(((((int)t.getY()))/FACT)*FACT, 0), TABLE_SIZE, TABLE_SIZE, 2, 2);
                    gc.setStroke(Color.BLACK);
                }
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if(moveTable) {
                    if(movingRect != null) {
                        Rect movedRect = new Rect(Math.max(((((int) t.getX())) / FACT) * FACT, 0), Math.max(((((int) t.getY())) / FACT) * FACT, 0), TABLE_SIZE, TABLE_SIZE, interiorService);
                        movedRect.setNumber(movingRect.getNumber());
                        rects.add(movedRect);
                        movingRect = null;
                    }
                }
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if(moveTable) {
                    for (Rect rect : rects) {
                        if (rect.getRect(t.getX(), t.getY()) != null) {
                            if(movingRect == null) {
                                movingRect = rect;
                            }
                        }
                    }

                    if(rects.contains(movingRect))
                        rects.remove(movingRect);

                    if(movingRect != null) {
                        drawCanvas();
                        GraphicsContext gc = canvas.getGraphicsContext2D();

                        boolean intersectsWithExistingTable = false;
                        for (Rect iteratingRect : rects) {
                            if (iteratingRect.getRect(Math.max(((((int) t.getX())) / FACT) * FACT, 0) + 1, Math.max(((((int) t.getY())) / FACT) * FACT, 0) + 1) != null ||
                                    iteratingRect.getRect(Math.max(((((int) t.getX())) / FACT) * FACT, 0) + TABLE_SIZE - 1, Math.max(((((int) t.getY())) / FACT) * FACT, 0) + 1) != null ||
                                    iteratingRect.getRect(Math.max(((((int) t.getX())) / FACT) * FACT, 0) + 1, Math.max(((((int) t.getY())) / FACT) * FACT, 0) + TABLE_SIZE - 1) != null ||
                                    iteratingRect.getRect(Math.max(((((int) t.getX())) / FACT) * FACT, 0) + TABLE_SIZE - 1, Math.max(((((int) t.getY())) / FACT) * FACT, 0) + TABLE_SIZE - 1) != null) {
                                intersectsWithExistingTable = true;
                            }
                        }

                        if (intersectsWithExistingTable) {
                            gc.strokeRoundRect(Math.max(((((int) prevX)) / FACT) * FACT, 0), Math.max(((((int) prevY)) / FACT) * FACT, 0), TABLE_SIZE, TABLE_SIZE, 2, 2);
                            gc.fillText(String.valueOf(movingRect.getNumber()), Math.max(((((int) prevX)) / FACT) * FACT, 0) + TABLE_SIZE / 4, Math.max(((((int) prevY)) / FACT) * FACT, 0) + TABLE_SIZE / 1.5);
                        } else {
                            gc.strokeRoundRect(Math.max(((((int) t.getX())) / FACT) * FACT, 0), Math.max(((((int) t.getY())) / FACT) * FACT, 0), TABLE_SIZE, TABLE_SIZE, 2, 2);
                            gc.fillText(String.valueOf(movingRect.getNumber()), Math.max(((((int) t.getX())) / FACT) * FACT, 0) + TABLE_SIZE / 4, Math.max(((((int) t.getY())) / FACT) * FACT, 0) + TABLE_SIZE / 1.5);
                            prevX = Math.max(((((int) t.getX())) / FACT) * FACT, 0);
                            prevY = Math.max(((((int) t.getY())) / FACT) * FACT, 0);
                        }
                    }
                    /*if(movingRect != null) {
                        Rect movedRect = new Rect(Math.max(((((int) t.getX())) / FACT) * FACT, 0), Math.max(((((int) t.getY())) / FACT) * FACT, 0), TABLE_SIZE, TABLE_SIZE, interiorService);
                        movedRect.setNumber(movingRect.getNumber());
                        if(rects.contains(movingRect)) {
                            if (rects.remove(movingRect))
                                rects.add(movedRect);
                        }
                        drawCanvas();
                    }*/
                }
            }
        });
    }

    public static void setInteriorService(InteriorService interiorService) {
        EditSectionController.interiorService = interiorService;
    }

    public static void setThisStage(Stage thisStage) {
        EditSectionController.thisStage = thisStage;
    }

    public static void setUpdateCanvas(RoomViewController.UpdateCanvas updateCanvas) {
        EditSectionController.updateCanvas = updateCanvas;
    }

    public void addTableButtonClicked(ActionEvent event) {
        LOGGER.info("Add Table Button Click");
        drawCanvas();
        createTable = true;
        moveTable = false;
        removeTable = false;
        updateTable = false;
    }

    public void moveTableButtonClicked(ActionEvent event) {
        LOGGER.info("Move Table Button Click");
        drawCanvas();
        moveTable = true;
        createTable = false;
        removeTable = false;
        updateTable = false;
    }

    public void removeTableButtonClicked(ActionEvent event) {
        LOGGER.info("Remove Table Button Click");
        drawCanvas();
        removeTable = true;
        moveTable = false;
        createTable = false;
        updateTable = false;
    }

    public void updateTableButtonClicked(ActionEvent event) {
        LOGGER.info("Update Table Button Click");
        drawCanvas();
        updateTable = true;
        removeTable = false;
        moveTable = false;
        createTable = false;
    }

    public void saveButtonClicked(ActionEvent event) {
        LOGGER.info("Save Section Button Click");
        try {
            Section sectionMatcher = new Section();
            sectionMatcher.setIdentity(sectionId);
            Section section = interiorService.findSections(sectionMatcher).get(0);

            // Remove all tables in this section and add changed/unchanged tables
            Table tableMatcher = new Table();
            tableMatcher.setSection(section);
            for(Table removeTable : interiorService.findTables(tableMatcher)) {
                interiorService.deleteTable(removeTable);
            }

            for (Rect rect : rects) {
                Table table = new Table();
                table.setSeats(rect.getSeats());
                table.setNumber(rect.getNumber());
                table.setSection(section);
                table.setColumn((int) (rect.getX() / FACT));
                table.setRow((int)(rect.getY()/FACT));
                interiorService.addTable(table);
            }
            updateCanvas.update();
            thisStage.close();
        } catch (ServiceException e) {
            showErrorDialog("Error", "Database problem", "Could not access database!");
        } catch (ValidationException e) {
            showErrorDialog("Error", "Validation problem", "Validation has failed!");
        }
    }

    private void drawCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        int maxX = (int)canvas.getWidth();
        int maxY = (int)canvas.getHeight();
        for(Rect rect : rects) {
            if(rect.getX()+rect.getW() > maxX-120)
                maxX = (int)(rect.getX()+rect.getW()+120);
            if(rect.getY()+rect.getH() > maxY-120)
                maxY = (int)(rect.getY()+rect.getH()+120);
        }
        canvas.setWidth(maxX);
        canvas.setHeight(maxY);

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for(Rect rect : rects) {
            gc.strokeRoundRect(rect.getX(), rect.getY(), rect.getW(), rect.getH(), 2, 2);
            gc.fillText(String.valueOf(rect.getNumber()), rect.getX()+TABLE_SIZE/4, rect.getY()+TABLE_SIZE/1.5);
        }
    }

    public class UpdateCanvas {
        public void update() {
            drawCanvas();
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
package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.*;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InteriorService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.MenuService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.TaxRateService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.xml.soap.Text;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * Controller used for the Manager View
 */
@Component
public class AddSectionController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(AddSectionController.class);

    private boolean createTable = false;
    private boolean moveTable = false;
    private boolean removeTable = false;
    private boolean updateTable = false;
    private final int FACT = 10;
    private final int CANVAS_PADDING = 20;
    private final int TABLE_SIZE = 40;
    private final int SECTION_OFFSET = 40;
    private final int SECTION_PADDING = 10;
    private final int TEXT_BORDER_BOTTOM = 2;
    private static InteriorService interiorService;
    private static Stage thisStage;
    private static ManagerController.UpdateCanvas updateCanvas;
    private ArrayList<Rect> rects = new ArrayList<Rect>();
    private Rect movingRect;

    @FXML
    private Canvas canvas;
    @FXML
    private TextField seatsTF;
    @FXML
    private TextField numberTF;
    @FXML
    private TextField nameTF;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        seatsTF.setText("6");
        numberTF.setText("1");

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

                            Rect rect = new Rect(Math.max(((((int) t.getX()) - TABLE_SIZE / 2) / FACT) * FACT, 0), Math.max(((((int) t.getY()) - TABLE_SIZE / 2) / FACT) * FACT, 0), TABLE_SIZE, TABLE_SIZE, interiorService);
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
                                    UpdateTableController.setInteriorService(interiorService);
                                    UpdateTableController.setUpdateCanvas(new UpdateCanvas());
                                    stage.setTitle("");
                                    AnchorPane myPane = FXMLLoader.load(getClass().getResource("/gui/UpdateTable.fxml"));
                                    Scene scene = new Scene(myPane);
                                    stage.setScene(scene);
                                    stage.showAndWait();
                                } catch (IOException e) {
                                    LOGGER.error("Unable to Load Update table" + e);
                                }
                            }
                        }
                    }
                } catch(NumberFormatException e) {
                    showErrorDialog("Error", "Invalid value", "Only valid numbers are allowed!");
                }
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if(createTable) {
                    drawCanvas();
                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    gc.setStroke(Color.GRAY);
                    gc.strokeRoundRect(Math.max(((((int)t.getX())-TABLE_SIZE/2)/FACT)*FACT, 0), Math.max(((((int)t.getY())-TABLE_SIZE/2)/FACT)*FACT, 0), TABLE_SIZE, TABLE_SIZE, 2, 2);
                    gc.setStroke(Color.BLACK);
                }
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if(moveTable) {
                    for (Rect rect : rects) {
                        if (rect.getRect(t.getX(), t.getY()) != null) {
                            movingRect = rect;
                        }
                    }

                    if(movingRect != null) {
                        Rect movedRect = new Rect(Math.max(((((int) t.getX()) - TABLE_SIZE / 2) / FACT) * FACT, 0), Math.max(((((int) t.getY()) - TABLE_SIZE / 2) / FACT) * FACT, 0), TABLE_SIZE, TABLE_SIZE, interiorService);
                        movedRect.setNumber(movingRect.getNumber());
                        rects.remove(movingRect);
                        rects.add(movedRect);
                        drawCanvas();
                    }
                }
            }
        });
    }

    public static void setInteriorService(InteriorService interiorService) {
        AddSectionController.interiorService = interiorService;
    }

    public static void setThisStage(Stage thisStage) {
        AddSectionController.thisStage = thisStage;
    }

    public static void setUpdateCanvas(ManagerController.UpdateCanvas updateCanvas) {
        AddSectionController.updateCanvas = updateCanvas;
    }

    public void addTableButtonClicked(ActionEvent event) {
        drawCanvas();
        createTable = true;
        moveTable = false;
        removeTable = false;
        updateTable = false;
    }

    public void moveTableButtonClicked(ActionEvent event) {
        drawCanvas();
        moveTable = true;
        createTable = false;
        removeTable = false;
        updateTable = false;
    }

    public void removeTableButtonClicked(ActionEvent event) {
        drawCanvas();
        removeTable = true;
        moveTable = false;
        createTable = false;
        updateTable = false;
    }

    public void updateTableButtonClicked(ActionEvent event) {
        drawCanvas();
        updateTable = true;
        removeTable = false;
        moveTable = false;
        createTable = false;
    }

    public void createButtonClicked(ActionEvent event) {
        if(nameTF.getText().isEmpty()) {
            showErrorDialog("Error", "Name missing", "Please insert a name for the section!");
        } else {
            try {
                Section section = new Section();
                section.setName(nameTF.getText());
                section.setIdentity((long) 10);  //TODO auto generate id
                interiorService.addSection(section);
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
            } catch(ServiceException e) {
                showErrorDialog("Error", "Database problem", "Could not access database!");
            }
        }
    }

    private void drawCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for(Rect rect : rects) {
            gc.strokeRoundRect(rect.getX(), rect.getY(), rect.getW(), rect.getH(), 2, 2);
            gc.fillText(String.valueOf(rect.getNumber()), rect.getX()+TABLE_SIZE/4, rect.getY()+TABLE_SIZE/1.5);
        }
    }

    //TODO think of a better solution
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

package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InteriorService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller used for Room view in Manager Window
 */
public class RoomViewController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(RoomViewController.class);

    @FXML
    private Canvas tablesCanvas;
    @FXML
    private ScrollPane scrollPaneLeft;
    @FXML
    private AnchorPane tableAnchor;
    private final ArrayList<Rect> rects = new ArrayList<>();
    private double scaleX = 1.0;
    private double scaleY = 1.0;
    private long clickedSectionId = -1;

    private final int FACT = 40;
    private final int CANVAS_PADDING = 20;
    private final int TABLE_SIZE = 40;
    private final int SECTION_OFFSET = 40;
    private final int SECTION_PADDING = 10;
    private final int TEXT_BORDER_BOTTOM = 2;

    @Autowired
    private InteriorService interiorService;
    @Autowired
    private TaskScheduler taskScheduler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        drawCanvas();

        tablesCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
            boolean noSectionClicked = true;
            for(Rect rect : rects) {
                try {
                    Table clickedTable = rect.getTable(t.getX(), t.getY());
                    if(clickedTable != null) {
                        System.out.println((String.valueOf(clickedTable.getNumber()) + " clicked"));
                    }
                    Section clickedSection = rect.getSection(t.getX(), t.getY());
                    if(clickedSection != null) {
                        noSectionClicked = false;
                        clickedSectionId = clickedSection.getIdentity();
                        System.out.println((String.valueOf(clickedSection.getName()) + " clicked" + clickedSection.getIdentity()));
                    }
                } catch(ServiceException e) {
                    showErrorDialog("Error", e.getMessage());
                }
            }
            if(noSectionClicked)
                clickedSectionId = -1;
            drawCanvas();
        });

        scrollPaneLeft.setPrefSize(120, 120);
        scrollPaneLeft.setContent(tablesCanvas);
        //scrollPaneLeft.setStyle("-fx-font-size: 40px;");

        setupListeners();

    }

    public class UpdateCanvas {
        public void update() {
            drawCanvas();
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
        int canvasWidth = (int)scrollPaneLeft.getWidth()-20;
        //tablesCanvas.setWidth(scrollPaneLeft.getWidth()-20);

        gc.clearRect(0, 0, tablesCanvas.getWidth(), tablesCanvas.getHeight());

        // Set canvas width to width of biggest section
        try {
            for (Section section : interiorService.getAllSections()) {
                if(SECTION_PADDING*scaleX+CANVAS_PADDING*scaleX+calculateWidth(section)*scaleX > canvasWidth) {
                    canvasWidth = (int)(SECTION_PADDING*scaleX+CANVAS_PADDING*scaleX+calculateWidth(section)*scaleX);
                }
            }
        } catch(ServiceException e) {
            showErrorDialog("Error", e.getMessage());
        }

        tablesCanvas.setWidth(canvasWidth);

        try {
            for (Section section : interiorService.getAllSections()) {
                // Draw section in a new line if there is not enough space
                if(firstSection) {
                    firstSection = false;
                } else {
                    if(x*scaleX+calculateWidth(prevSection)*scaleX+SECTION_PADDING*scaleX+CANVAS_PADDING*scaleX+calculateWidth(section)*scaleX < tablesCanvas.getWidth()) {
                        x += calculateWidth(prevSection)+SECTION_PADDING*scaleX;
                    } else {
                        x = CANVAS_PADDING;
                        y += rowHeight + SECTION_OFFSET * scaleY;
                        rowHeight = 0;
                    }
                }

                if(tablesCanvas.getHeight() < y*scaleY + calculateHeight(section)*scaleY+CANVAS_PADDING*scaleY)
                    tablesCanvas.setHeight(y*scaleY + calculateHeight(section)*scaleY+CANVAS_PADDING*scaleY);
                if(section.getIdentity().equals(Long.valueOf(clickedSectionId)))
                    gc.setStroke(Color.RED);
                gc.strokeRoundRect(x*scaleX, y*scaleY, calculateWidth(section)*scaleX, calculateHeight(section)*scaleY, 10, 10);
                gc.setStroke(Color.BLACK);
                Rect rectSection = new Rect(x*scaleX, y*scaleY, calculateWidth(section)*scaleX, calculateHeight(section)*scaleY, interiorService);
                rectSection.setIdentity(section.getIdentity());
                rects.add(rectSection);
                gc.setFont(new Font(gc.getFont().getName(), 20 * scaleText));
                gc.fillText(section.getName() + ":", x*scaleX, (y-TEXT_BORDER_BOTTOM)*scaleY);
                Table matcher = new Table();
                matcher.setSection(section);
                for (Table table : interiorService.findTables(matcher)) {
                    Rect rectTable = new Rect(((x+SECTION_PADDING)+(table.getColumn()*FACT))*scaleX, ((y+SECTION_PADDING)+(table.getRow()*FACT))*scaleY, TABLE_SIZE*scaleX, TABLE_SIZE*scaleY, interiorService);
                    rectTable.setNumber(table.getNumber());
                    rectTable.setSection(section);
                    gc.strokeRoundRect(rectTable.getX(), rectTable.getY(), rectTable.getW(), rectTable.getH(), 2, 2);
                    rects.add(rectTable);
                    gc.fillText(String.valueOf(table.getNumber()), ((x+SECTION_PADDING)+(table.getColumn()*FACT)+TABLE_SIZE/4)*scaleX, ((y+SECTION_PADDING)+(table.getRow()*FACT)+TABLE_SIZE/1.5)*scaleY);
                }
                rowHeight = Math.max(rowHeight, calculateHeight(section));
                prevSection = section;
            }
        } catch(ServiceException e) {
            showErrorDialog("Error", e.getMessage());
        }
    }



    public void buttonAddSectionClicked() {
        LOGGER.info("Add Section Button Click");
        drawCanvas();
        try {
            Stage stage = new Stage();
            AddSectionController.setInteriorService(interiorService);
            AddSectionController.setThisStage(stage);
            AddSectionController.setUpdateCanvas(new UpdateCanvas());
            stage.setTitle("Add Section");
            AnchorPane myPane = FXMLLoader.load(getClass().getResource("/gui/AddSection.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            LOGGER.error("Unable to Load Add Section" + e);
        }
    }

    public void buttonEditSectionClicked() {
        LOGGER.info("Edit Section Button Click");
        if(clickedSectionId != -1) {
            drawCanvas();
            try {
                Stage stage = new Stage();
                EditSectionController.setInteriorService(interiorService);
                EditSectionController.setThisStage(stage);
                EditSectionController.setUpdateCanvas(new UpdateCanvas());
                EditSectionController.setSectionId(clickedSectionId);
                stage.setTitle("Edit Section");
                AnchorPane myPane = FXMLLoader.load(getClass().getResource("/gui/EditSection.fxml"));
                Scene scene = new Scene(myPane);
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.showAndWait();
            } catch (IOException e) {
                LOGGER.error("Unable to Load Edit Section" + e);
            }
        }
    }

    public void buttonRemoveSectionClicked() {
        LOGGER.info("Remove Section Button Click");
        if(clickedSectionId != -1) {
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Remove Section?");
                alert.setHeaderText("Remove Section?");
                alert.setContentText("Do you really want to remove the selected Section and all the tables inside?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Section sectionMatcher = new Section();
                    sectionMatcher.setIdentity(clickedSectionId);
                    Section section = interiorService.findSections(sectionMatcher).get(0);
                    Table tableMatcher = new Table();
                    tableMatcher.setSection(section);
                    ArrayList<Table> deleteTables = new ArrayList<>();
                    for(Table table : interiorService.findTables(tableMatcher)) {
                        deleteTables.add(table);    // Avoid deleting while iterating
                    }
                    for(Table table : deleteTables) {
                        interiorService.deleteTable(table);
                    }
                    interiorService.deleteSection(section);
                    clickedSectionId = -1;
                }
            } catch (ServiceException e) {
                LOGGER.error("Unable to remove section");
            } catch (ValidationException e) {
                LOGGER.error("Validation error");
            }
        }
        drawCanvas();
    }

    private void setupListeners() {
        scrollPaneLeft.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> {
            tablesCanvas.setWidth(tablesCanvas.getWidth() - (oldSceneWidth.intValue() - newSceneWidth.intValue()));
            scaleX = newSceneWidth.doubleValue()/550.0;
            scaleX = Math.min(scaleX, 2.0);
            scaleX = Math.max(scaleX, 0.5);
            drawCanvas();
        });
        scrollPaneLeft.heightProperty().addListener((observableValue, oldSceneHeight, newSceneHeight) -> {
            tablesCanvas.setHeight(tablesCanvas.getHeight() - (oldSceneHeight.intValue() - newSceneHeight.intValue()));
            scaleY = newSceneHeight.doubleValue()/598.0;
            scaleY = Math.min(scaleY, 2.0);
            scaleY = Math.max(scaleY, 0.5);
            drawCanvas();
        });
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

    private void showErrorDialog(String head, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(head);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller used for the table update View
 */
@Component
public class UpdateTableController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(UpdateTableController.class);

    private static Stage thisStage;
    private static ArrayList<Rect> rects = new ArrayList<>();
    private static Rect clickedRect;
    private static AddSectionController.UpdateCanvas updateAddCanvas;
    private static EditSectionController.UpdateCanvas updateEditCanvas;

    @FXML
    private TextField numberTF;
    @FXML
    private TextField seatsTF;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Initializing Update Table Controller");
        numberTF.setText(String.valueOf(clickedRect.getNumber()));
        seatsTF.setText(String.valueOf(clickedRect.getSeats()));
    }

    public static void setThisStage(Stage thisStage) {
        UpdateTableController.thisStage = thisStage;
    }

    public static void setRects(ArrayList<Rect> rects) {
        UpdateTableController.rects = rects;
    }

    public static void setClickedRect(Rect rect) {
        UpdateTableController.clickedRect = rect;
    }

    public static void setAddUpdateCanvas(AddSectionController.UpdateCanvas updateCanvas) {
        UpdateTableController.updateAddCanvas = updateCanvas;
    }

    public static void setEditUpdateCanvas(EditSectionController.UpdateCanvas updateCanvas) {
        UpdateTableController.updateEditCanvas = updateCanvas;
    }

    public void cancelButtonClicked() {
        LOGGER.info("Cancel Update Table Button Click");
        thisStage.close();
    }

    public void updateButtonClicked() {
        LOGGER.info("Update Table Button Click");
        try {
            if (numberTF.getText().isEmpty()) {
                showErrorDialog("Number missing", "Please insert a number for the table!");
            } else if (Long.valueOf(numberTF.getText()) < 1) {
                showErrorDialog("Number invalid", "The table number must be >= 1!");
            } else if (seatsTF.getText().isEmpty()) {
                showErrorDialog("Seats missing", "Please insert the number of seats for the table!");
            } else if (Integer.valueOf(seatsTF.getText()) < 0) {
                showErrorDialog("Seats invalid", "The number of seats must be >= 0!");
            } else {
                //Check if table number is not used in this section
                for (Rect iteratingRect : rects) {
                    if (iteratingRect != clickedRect && iteratingRect.getNumber() == Long.valueOf(numberTF.getText())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Number already used");
                        alert.setContentText("The table number is already used in this section!");
                        alert.showAndWait();
                        return;
                    }
                }

                clickedRect.setNumber(Long.valueOf(numberTF.getText()));
                clickedRect.setSeats(Integer.valueOf(seatsTF.getText()));
                if(updateAddCanvas != null) updateAddCanvas.update();
                else if(updateEditCanvas != null) updateEditCanvas.update();
                thisStage.close();
            }
        } catch(NumberFormatException e) {
            showErrorDialog("Invalid value", "Only valid numbers are allowed!");
        }
    }

    private static void showErrorDialog(String head, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(head);
        alert.setContentText(content);

        alert.showAndWait();
    }
}

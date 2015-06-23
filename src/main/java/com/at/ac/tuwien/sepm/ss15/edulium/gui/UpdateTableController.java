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
 * Controller used for the table update View
 */
@Component
public class UpdateTableController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(UpdateTableController.class);

    private static Stage thisStage;
    private static ArrayList<Rect> rects = new ArrayList<Rect>();
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

    public void cancelButtonClicked(ActionEvent actionEvent) {
        LOGGER.info("Cancel Update Table Button Click");
        thisStage.close();
    }

    public void updateButtonClicked(ActionEvent actionEvent) {
        LOGGER.info("Update Table Button Click");
        try {
            if (numberTF.getText().isEmpty()) {
                showErrorDialog("Error", "Number missing", "Please insert a number for the table!");
            } else if (Long.valueOf(numberTF.getText()) < 1) {
                showErrorDialog("Error", "Number invalid", "The table number must be >= 1!");
            } else if (seatsTF.getText().isEmpty()) {
                showErrorDialog("Error", "Seats missing", "Please insert the number of seats for the table!");
            } else if (Integer.valueOf(seatsTF.getText()) < 0) {
                showErrorDialog("Error", "Seats invalid", "The number of seats must be >= 0!");
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
            showErrorDialog("Error", "Invalid value", "Only valid numbers are allowed!");
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

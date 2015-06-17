package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InteriorService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;

import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by phili on 6/16/15.
 */
public class TableViewController implements Initializable, Controller {
    private static final Logger LOGGER = LogManager.getLogger(TableViewController.class);

    @FXML
    private ScrollPane scrollPane;

    @Autowired
    private TaskScheduler taskScheduler;
    @Autowired
    private InteriorService interiorService;

    private PollingList<Table> tables;
    private PollingList<Section> sections;
    private Consumer<Table> tableClickedConsumer = null;

    private Map<Section, GridView<Table>> sectionsMap = new HashMap<>();

    private class SectionListCell extends ListCell<Section> {
        @Override
        public void updateItem(Section item, boolean empty) {
            super.updateItem(item, empty);

            if(empty) {
                setGraphic(null);
            }

            GridView<Table> gridView = new GridView<>();
            gridView.setCellFactory(table -> {
                Button button = new Button();
                button.setText(table.getNumber().toString());
                button.setFont(new Font(20.0));
                button.setOnAction(e -> onTableClicked(table));
                return new GridView.GridCell(button, table.getColumn(), table.getRow());
            });

            gridView.setItems(tables.filtered(table -> table.getSection().equals(item)));

            if (item != null) {
                sectionsMap.put(item, gridView);

                Label titleLabel = new Label();
                titleLabel.setFont(new Font(20.0));
                titleLabel.setText(item.getName());

                VBox vBox = new VBox(20);
                vBox.getChildren().addAll(titleLabel, gridView);

                setGraphic(vBox);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tables = new PollingList<>(taskScheduler);
        tables.setInterval(1000);
        tables.setSupplier(() -> {
            try {
                return interiorService.getAllTables();
            } catch (ServiceException e) {
                LOGGER.error("Getting all tables via user supplier has failed", e);
                return null;
            }
        });

        sections = new PollingList<>(taskScheduler);
        sections.setInterval(1000);
        sections.setSupplier(() -> {
            try {
                return interiorService.getAllSections();
            } catch (ServiceException e) {
                LOGGER.error("Getting all sections via user supplier has failed", e);
                return null;
            }
        });

        ListView<Section> listView = new ListView<>(sections);
        listView.setCellFactory(param -> new SectionListCell());

        scrollPane.setContent(listView);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-font-size: 40px;");
    }

    public void setOnTableClicked(Consumer<Table> tableConsumer) {
        this.tableClickedConsumer = tableConsumer;
    }

    public void setTableColor(Table t, Color c) {
        Button btn = (Button) sectionsMap.get(t.getSection()).getNode(t);
        btn.setTextFill(c);
    }

    public void setTableDisable(Table t, boolean disabled) {
        Button btn = (Button) sectionsMap.get(t.getSection()).getNode(t);
        btn.setDisable(disabled);
    }

    @Override
    public void disable(boolean disabled) {
        if (disabled) {
            tables.stopPolling();
            sections.stopPolling();
        } else {
            tables.startPolling();
            sections.startPolling();
        }
    }

    private void onTableClicked(Table t) {
        if(tableClickedConsumer != null) {
            tableClickedConsumer.accept(t);
        }
    }
}

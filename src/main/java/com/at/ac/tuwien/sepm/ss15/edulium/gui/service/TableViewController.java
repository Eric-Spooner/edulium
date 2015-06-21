package com.at.ac.tuwien.sepm.ss15.edulium.gui.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.util.GridView;
import com.at.ac.tuwien.sepm.ss15.edulium.gui.util.PollingList;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InteriorService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;

@Controller
public class TableViewController implements Initializable {
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
    private final Map<Section, GridView<Table>> sectionsMap = new HashMap<>();
    private Boolean showSeats = false;

    private class TableGridCell extends GridView.GridCell<Table> {
        private final Button button;
        private Table table;

        public TableGridCell() {
            button = new Button();
            button.setFont(new Font(25.0));
            button.setMinSize(65, 65);
            button.setOnAction(e -> onTableClicked(table));

            setNode(button);
        }

        @Override
        protected void updateItem(Table table) {
            this.table = table;

            if (table != null) {
                if(showSeats) {
                    button.setText(table.getSeats().toString());
                } else {
                    button.setText(table.getNumber().toString());
                }

                setX(table.getColumn());
                setY(table.getRow());

                button.setVisible(true);
            } else {
                button.setVisible(false);
            }
        }
    }

    private class SectionListCell extends ListCell<Section> {
        @Override
        public void updateItem(Section item, boolean empty) {
            super.updateItem(item, empty);

            if(empty) {
                setGraphic(null);
            }

            GridView<Table> gridView = new GridView<>();
            gridView.setCellFactory(view -> new TableGridCell());
            gridView.setAlignment(Pos.CENTER);
            gridView.setItems(tables.filtered(table -> table.getSection().equals(item)));
            gridView.setStyle("-fx-border-color: rgb(0, 0, 0);\n" +
                    "    -fx-border-radius: 5;\n" +
                    "    -fx-border-width: 2; \n" +
                    "    -fx-padding: 10 10 10 10;");

            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.getChildren().add(gridView);

            if (item != null) {
                sectionsMap.put(item, gridView);

                Label titleLabel = new Label();
                titleLabel.setFont(new Font(20.0));
                titleLabel.setText(item.getName());

                VBox vBox = new VBox(15.0);
                vBox.setAlignment(Pos.CENTER);
                vBox.getChildren().addAll(titleLabel, hBox);

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
        tables.startPolling();

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
        sections.startPolling();

        ListView<Section> listView = new ListView<>(sections);
        listView.setCellFactory(param -> new SectionListCell());

        scrollPane.setContent(listView);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-font-size: 40px;");
    }

    public void showSeats(boolean showSeats) {
        this.showSeats = showSeats;
    }

    public void setOnTableClicked(Consumer<Table> tableConsumer) {
        this.tableClickedConsumer = tableConsumer;
    }

    public void setTableColor(Table t, Color c) {
        if(!sectionsMap.containsKey(t.getSection())) {
            return;
        }

        Button btn = (Button) sectionsMap.get(t.getSection()).getNode(t);
        btn.setTextFill(c);
    }

    public void setTableDisable(Table t, boolean disabled) {
        if(!sectionsMap.containsKey(t.getSection())) {
            return;
        }

        Button btn = (Button) sectionsMap.get(t.getSection()).getNode(t);
        btn.setDisable(disabled);
    }

    public void clear() {
        for(Table t : tables) {
            setTableColor(t, Color.BLACK);
            setTableDisable(t, false);
        }
    }

    private void onTableClicked(Table t) {
        if(tableClickedConsumer != null) {
            tableClickedConsumer.accept(t);
        }
    }
}

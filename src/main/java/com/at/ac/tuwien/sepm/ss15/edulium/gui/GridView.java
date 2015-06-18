package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by phili on 6/16/15.
 */
public class GridView<T> extends GridPane {

    private Callback<GridView, GridCell> cellFactory;
    private Map<T, Node> nodeMap = new HashMap<>();

    public static abstract class GridCell<ItemType> {
        private Node node;
        private int xPos;
        private int yPos;

        protected int getY() {
            return yPos;
        }

        protected void setY(int yPos) {
            this.yPos = yPos;
        }

        protected int getX() {
            return xPos;
        }

        protected void setX(int xPos) {
            this.xPos = xPos;
        }

        protected Node getNode() {
            return node;
        }

        protected void setNode(Node node) {
            this.node = node;
        }

        protected abstract void updateItem(ItemType item);
    }

    public void setItems(ObservableList<T> items) {
        items.addListener(new ListChangeListener<T>() {
            @Override
            public void onChanged(Change<? extends T> c) {
                while(c.next()) {
                    for (T item : c.getAddedSubList()) {
                        addCellItem(item);
                    }

                    for (T item : c.getRemoved()) {
                        removeCellItem(item);
                    }
                }
            }
        });

        for(T item : items) {
            addCellItem(item);
        }
    }

    public final void setCellFactory(Callback<GridView, GridCell> cellFactory) {
        this.cellFactory = cellFactory;
    }

    public Node getNode(T item) {
        return nodeMap.get(item);
    }

    private void addCellItem(T item) {
        if (cellFactory == null) {
            return;
        }

        GridCell cellItem = cellFactory.call(this);
        cellItem.updateItem(item);
        nodeMap.put(item, cellItem.getNode());

        add(cellItem.getNode(), cellItem.getX(), cellItem.getY());

        /*
        // add dummy elements to add rows
        for(; rows < cellItem.getY(); rows++) {
            final Button dummy = new Button();
            dummy.minHeightProperty().bind(((Button) cellItem.getNode()).heightProperty());
            dummy.minWidthProperty().bind(((Button) cellItem.getNode()).widthProperty());
            dummy.setVisible(false);
            add(dummy, 0, rows);
        }

        // add dummy elements to add columns
        for(; cols < cellItem.getX(); cols++) {
            final Pane dummy = new Pane();
            dummy.minWidthProperty().bind(((Button) cellItem.getNode()).widthProperty());
            dummy.minHeightProperty().bind(((Button) cellItem.getNode()).heightProperty());
            dummy.setVisible(false);
            add(dummy, cols, 0);
        }

        for(ColumnConstraints c : getColumnConstraints()) {
            c.setPercentWidth(100.0/ (double) getColumnConstraints().size());
        }
        */
    }

    private void removeCellItem(T item) {
        getChildren().remove(nodeMap.get(item));
        nodeMap.remove(item);
    }
}

package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
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

    private int rows = 1;
    private int cols = 1;

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

        // add dummy elements to add rows
        for (; rows < cellItem.getY(); rows++) {
            GridCell dummyCellItem = cellFactory.call(this);
            dummyCellItem.updateItem(null);
            dummyCellItem.setX(1);
            dummyCellItem.setY(rows);
            add(dummyCellItem.getNode(), dummyCellItem.getX(), dummyCellItem.getY());
        }

        // add dummy elements to add columns
        for (; cols < cellItem.getX(); cols++) {
            GridCell dummyCellItem = cellFactory.call(this);
            dummyCellItem.updateItem(null);
            dummyCellItem.setX(cols);
            dummyCellItem.setY(1);
            add(dummyCellItem.getNode(), dummyCellItem.getX(), dummyCellItem.getY());
        }

        add(cellItem.getNode(), cellItem.getX(), cellItem.getY());

        for (ColumnConstraints c : getColumnConstraints()) {
            c.setPercentWidth(100.0 / (double)getColumnConstraints().size());
        }
    }

    private void removeCellItem(T item) {
        getChildren().remove(nodeMap.get(item));
        nodeMap.remove(item);
    }
}

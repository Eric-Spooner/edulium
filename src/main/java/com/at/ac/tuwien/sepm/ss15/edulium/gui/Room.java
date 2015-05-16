package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 01.05.2015.
 */
public class Room {
    private List<Table> tables;
    private String name;

    public Room(String name) {
        this.name = name;
        tables = new ArrayList<Table>();
    }

    public void add(Table table) {
        tables.add(table);
    }

    public int getHeight() {
        int max = -1;
        for(Table table : tables) {
            if(table.getY()+table.getHeight() > max) {
                max = table.getY()+table.getHeight();
            }
        }
        return max;
    }

    public int getWidth() {
        int max = -1;
        for(Table table : tables) {
            if(table.getX()+table.getWidth() > max) {
                max = table.getX()+table.getWidth();
            }
        }
        return max;
    }

    public List<Table> getTables() {
        return tables;
    }

    public String getName() {
        return name;
    }

    public Table getTable(int x, int y) {
        for(Table table : tables) {
            if(table.getX() <= x && table.getX()+table.getWidth() >= x
                    &&table.getY() <= y && table.getY()+table.getHeight() >= y)
                return table;
        }
        return null;
    }
}

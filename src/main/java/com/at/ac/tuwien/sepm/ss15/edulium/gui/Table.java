package com.at.ac.tuwien.sepm.ss15.edulium.gui;

/**
 * Created by Administrator on 01.05.2015.
 */
public class Table {
    private int x;
    private int y;
    private int width;
    private int height;
    private int id;
    private boolean occupied = false;

    public Table(int id, int x, int y, int width, int height) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public int getId() {
        return id;
    }
}

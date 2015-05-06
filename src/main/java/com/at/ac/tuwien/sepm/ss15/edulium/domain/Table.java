package com.at.ac.tuwien.sepm.ss15.edulium.domain;

/**
 * Created by Administrator on 06.05.2015.
 */
public class Table {
    private int seats;
    private Long number;
    private int row;
    private int col;
    private boolean disabled;

    public Table() {

    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public String toString() {
        return "Table{" +
                "seats=" + seats +
                ", number=" + number +
                ", row=" + row +
                ", col=" + col +
                ", disabled=" + disabled +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Table table = (Table) o;

        if (col != table.col) return false;
        if (disabled != table.disabled) return false;
        if (number != table.number) return false;
        if (row != table.row) return false;
        if (seats != table.seats) return false;

        return true;
    }
}

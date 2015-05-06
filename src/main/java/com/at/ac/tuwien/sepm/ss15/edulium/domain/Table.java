package com.at.ac.tuwien.sepm.ss15.edulium.domain;

/**
 * Created by Administrator on 06.05.2015.
 */
public class Table {
    private int seats;
    private Long number;
    private int row;
    private int column;

    public Table() {

    }

    /**
     * @return the number of seats on this table
     */
    public int getSeats() {
        return seats;
    }

    /**
     * @param seats sets the number of seats on this table
     */
    public void setSeats(int seats) {
        this.seats = seats;
    }

    /**
     * @return the unique number of this table
     */
    public Long getNumber() {
        return number;
    }

    /**
     * @param number sets the unique number of this table
     */
    public void setNumber(Long number) {
        this.number = number;
    }

    /**
     * @return the row of this table
     */
    public int getRow() {
        return row;
    }

    /**
     * @param row sets the row of this table
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * @return the column of this table
     */
    public int getColumn() {
        return column;
    }

    /**
     * @param column sets the column of this table
     */
    public void setCol(int column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return "Table{" +
                "seats=" + seats +
                ", number=" + number +
                ", row=" + row +
                ", col=" + column +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Table table = (Table) o;

        if (column != table.column) return false;
        if (number != table.number) return false;
        if (row != table.row) return false;
        if (seats != table.seats) return false;

        return true;
    }
}

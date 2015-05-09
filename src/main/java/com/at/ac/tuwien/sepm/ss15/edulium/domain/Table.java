package com.at.ac.tuwien.sepm.ss15.edulium.domain;

/**
 * domain object which represents a table in the restaurant
 */
public class Table {
    private int seats;
    private Long number;
    private Long section_id;
    private Long user_id;
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
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * @return the id of the section the table is located
     */
    public Long getSection_id() {
        return section_id;
    }

    /**
     * @param section_id sets the id of the section the table is located
     */
    public void setSection_id(Long section_id) {
        this.section_id = section_id;
    }

    /**
     * @return the id of the user responsible for the table
     */
    public Long getUser_id() {
        return user_id;
    }

    /**
     * @param user_id sets the id of the user responsible for the table
     */
    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Table{" +
                "seats=" + seats +
                ", number=" + number +
                ", section_id=" + section_id +
                ", user_id=" + user_id +
                ", row=" + row +
                ", column=" + column +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Table table = (Table) o;

        if (column != table.column) return false;
        if (row != table.row) return false;
        if (seats != table.seats) return false;
        if (number != null ? !number.equals(table.number) : table.number != null) return false;
        if (section_id != null ? !section_id.equals(table.section_id) : table.section_id != null) return false;
        if (user_id != null ? !user_id.equals(table.user_id) : table.user_id != null) return false;

        return true;
    }
}

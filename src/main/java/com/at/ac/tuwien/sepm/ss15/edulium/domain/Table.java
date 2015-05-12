package com.at.ac.tuwien.sepm.ss15.edulium.domain;

/**
 * domain object which represents a table in the restaurant
 */
public class Table {
    private Integer seats;
    private Long number;
    private Section section;
    private User user;
    private Integer row;
    private Integer column;

    public Table() {

    }

    /**
     * @return the number of seats on this table
     */
    public Integer getSeats() {
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
    public Integer getRow() {
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
    public Integer getColumn() {
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
    public Section getSection() {
        return section;
    }

    /**
     * @param section sets the id of the section the table is located
     */
    public void setSection(Section section) {
        this.section = section;
    }

    /**
     * @return the id of the user responsible for the table
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user sets the id of the user responsible for the table
     */
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Table{" +
                "seats=" + seats +
                ", number=" + number +
                ", section=" + section +
                ", user=" + user +
                ", row=" + row +
                ", column=" + column +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Table table = (Table) o;

        if (column != null ? !column.equals(table.column) : table.column != null) return false;
        if (number != null ? !number.equals(table.number) : table.number != null) return false;
        if (row != null ? !row.equals(table.row) : table.row != null) return false;
        if (seats != null ? !seats.equals(table.seats) : table.seats != null) return false;
        if (section != null ? !section.equals(table.section) : table.section != null) return false;
        if (user != null ? !user.equals(table.user) : table.user != null) return false;

        return true;
    }
}

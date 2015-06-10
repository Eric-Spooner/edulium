package com.at.ac.tuwien.sepm.ss15.edulium.domain;

/**
 * domain object which represents a table in the restaurant
 */
public class Table {
    private Integer seats;
    private Long number;
    private Section section;
    private User user; // optional
    private Integer row;
    private Integer column;

    /**
     * Creates a new table object and assigns the given number and section to it.
     *
     * @param section section the table is located
     * @param number  number of the table
     * @return table with assigned identity parameters
     */
    public static Table withIdentity(Section section, long number) {
        Table table = new Table();
        table.setNumber(number);
        table.setSection(section);
        return table;
    }

    /**
     * @return the number of seats on this table
     * can be null
     */
    public Integer getSeats() {
        return seats;
    }

    /**
     * @param seats sets the number of seats on this table
     */
    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    /**
     * @return the unique number of this table
     * can be null (if this instance does not represent
     * a persistent dataset)
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
     * can be null
     */
    public Integer getRow() {
        return row;
    }

    /**
     * @param row sets the row of this table
     */
    public void setRow(Integer row) {
        this.row = row;
    }

    /**
     * @return the column of this table
     * can be null
     */
    public Integer getColumn() {
        return column;
    }

    /**
     * @param column sets the column of this table
     */
    public void setColumn(Integer column) {
        this.column = column;
    }

    /**
     * @return the id of the section the table is located
     * can be null (if this instance does not represent
     * a persistent dataset)
     */
    public Section getSection() {
        return section;
    }

    /**
     * @param section sets the unique id of the section the table is located
     */
    public void setSection(Section section) {
        this.section = section;
    }

    /**
     * @return the id of the user responsible for the table
     * can be null
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user sets the id of the user responsible for the table (optional)
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

        return true;
    }

    @Override
    public int hashCode() {
        int result = seats != null ? seats.hashCode() : 0;
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (section != null ? section.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (row != null ? row.hashCode() : 0);
        result = 31 * result + (column != null ? column.hashCode() : 0);
        return result;
    }
}

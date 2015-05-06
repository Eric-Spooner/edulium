package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import java.util.ArrayList;

/**
 * domain object which represents a section in the restaurant
 */
public class Section {
    private Long id;
    private String name;
    private boolean deleted;
    private ArrayList<Table> tables;

    public Section() {
        tables = new ArrayList<Table>();
    }

    /**
     * @return the unique id of the section
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id sets the unique id of the section
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name of the section
     */
    public String getName() {
        return name;
    }

    /**
     * @param name sets the name of the section
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return true if section is deleted, false otherwise
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * @param deleted sets the section to deleted (true) or false otherwise
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * @return list of all tables in this section
     */
    public ArrayList<Table> getTables() {
        return tables;
    }

    /**
     * @param table adds specified table to this section,
     *              already existing tables are not changed
     */
    public void addTable(Table table) {
        tables.add(table);
    }

    /**
     * @param tables adds multiple tables to this section,
     *               already existing tables are not changed
     */
    public void addTables(ArrayList<Table> tables) {
        tables.addAll(tables);
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", deleted=" + deleted +
                ", tables=" + tables +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Section section = (Section) o;

        if (deleted != section.deleted) return false;
        if (id != section.id) return false;
        if (name != null ? !name.equals(section.name) : section.name != null) return false;
        if (tables != null ? !tables.equals(section.tables) : section.tables != null) return false;

        return true;
    }
}

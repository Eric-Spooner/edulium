package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import java.util.ArrayList;

/**
 * domain object which represents a section in the restaurant
 */
public class Section {
    private Long identity;
    private String name;

    public Section() {

    }

    /**
     * Creates a new section object and assigns the given identity to it.
     * @param identity the identity of the section
     * @return section object with the given identity
     */
    public static Section withIdentity(long identity) {
        Section section = new Section();
        section.setIdentity(identity);
        return section;
    }

    /**
     * @return the unique identity of the section
     */
    public Long getIdentity() {
        return identity;
    }

    /**
     * @param id sets the unique identity of the section
     */
    public void setIdentity(Long id) {
        this.identity = id;
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
     * @param tables adds multiple tables to this section,
     *               already existing tables are not changed
     */
    public void addTables(ArrayList<Table> tables) {
        tables.addAll(tables);
    }

    @Override
    public String toString() {
        return "Section{" +
                "identity=" + identity +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Section section = (Section) o;

        if (identity != section.identity) return false;
        if (name != null ? !name.equals(section.name) : section.name != null) return false;

        return true;
    }
}

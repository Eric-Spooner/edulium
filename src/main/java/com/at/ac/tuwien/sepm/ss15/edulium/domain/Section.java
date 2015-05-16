package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * domain object which represents a section in the restaurant
 */
public class Section {
    private Long identity;
    private String name;
    @Autowired
    private DAO<Table> tableDAO;

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
     *         can be null (if this instance does not represent
     *         a persistent dataset)
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
     *         can be null
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
     * @return a list of tables in this section
     *         can be empty if there are no tables in this section
     */
    public List getTables() throws DAOException {
        Table matcher = new Table();
        matcher.setSection(this);
        List tableList;

        try {
            tableList = tableDAO.find(matcher);
        }
        catch(DAOException e) {
            throw new DAOException("cannot find table", e);
        }

        return tableList;
    }

    /**
     * @param table adds one table to this section,
     *               already existing tables are not changed
     */
    public void setTable(Table table) {
        table.setSection(this);
    }

    /**
     * @param tables adds multiple tables to this section,
     *               already existing tables are not changed
     */
    public void setTables(ArrayList<Table> tables) {
        for(Table table : tables) {
            table.setSection(this);
        }
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

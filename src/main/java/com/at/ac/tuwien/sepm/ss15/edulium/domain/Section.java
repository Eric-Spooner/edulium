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

    /**
     * Creates a new section object and assigns the given identity to it.
     *
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
     * can be null (if this instance does not represent
     * a persistent dataset)
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
     * can be null
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

        if (identity != null ? !identity.equals(section.identity) : section.identity != null) return false;
        if (name != null ? !name.equals(section.name) : section.name != null) return false;

        return true;
    }
}
package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Abstract domain object representing a Sale
 */
public abstract class Sale {
    protected Long identity = null;
    protected String name = null;
    protected List<MenuEntry> entries = null;

    /**
     * @return the identity of the sale (can be null)
     */
    public Long getIdentity() {
        return identity;
    }

    /**
     * Sets the identity of the sale
     * @param identity identity of the sale
     */
    public void setIdentity(Long identity) {
        this.identity = identity;
    }

    /**
     * @return the name of the sale (can be null)
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the sale
     * @param name name of the sale
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param time the time that has to be checked
     * @return true if the Sale is active at the given time
     */
    public abstract boolean isAt(LocalDateTime time);


    /**
     *
     * @return the entries of the sale with the new price
     */
    public List<MenuEntry> getEntries() {
        return entries;
    }


    /**
     * sets the list of the sale, where the entries have the sale price set
     * @param entries the entries list
     */
    public void setEntries(List<MenuEntry> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "identity=" + identity +
                ", name='" + name + '\'' +
                ", entries=" + entries +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sale sale = (Sale) o;

        if (identity != null ? !identity.equals(sale.identity) : sale.identity != null) return false;
        if (name != null ? !name.equals(sale.name) : sale.name != null) return false;
        return !(entries != null ? !entries.equals(sale.entries) : sale.entries != null);
    }
}
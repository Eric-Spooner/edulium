package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Hashtable;
import java.util.List;

/**
 * Abstract domain object representing a Sale
 */
public abstract class Sale {
    protected Long identity = null;
    protected String name = null;
    protected Hashtable<MenuEntry, BigDecimal> entries = null;

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
    public Hashtable<MenuEntry, BigDecimal> getEntries() {
        return entries;
    }


    /**
     * sets the entries/price list of the sale
     * @param entries
     */
    public void setEntries(Hashtable<MenuEntry, BigDecimal> entries) {
        this.entries = entries;
    }
}

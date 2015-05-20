package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import java.time.LocalDateTime;

/**
 * Domain object representing a Sale
 */
public abstract class Sale {
    protected Long identity = null;
    protected String name = null;

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
}

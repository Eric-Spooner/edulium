package com.at.ac.tuwien.sepm.ss15.edulium.domain.statistics;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;

import java.math.BigDecimal;

/**
 * domain object representing a entry in the menu, together with the number of sold items and revenue
 */
public class MenuEntryRevenue {
    private MenuEntry entry;
    private Long soldNumber;

    /**
     * @return the menuEntry of the object (can be null)
     */
    public MenuEntry getMenuEntry() {
        return entry;
    }

    /**
     * sets the menuEntry of the object
     * @param entry entry
     */
    public void setMenuEntry(MenuEntry entry) {
        this.entry = entry;
    }

    /**
     * @return the menuEntry ID
     */
    public Long getMenuEntryId() {
        return entry.getIdentity();
    }

    /**
     * @return the menuEntry name
     */
    public String getMenuEntryName() {
        return entry.getName();
    }

    /**
     * @return the menuEntry price
     */
    public BigDecimal getMenuEntryPrice() {
        return entry.getPrice();
    }

    /**
     * @return the number of items sold
     */
    public Long getSoldNumber() {
        return soldNumber;
    }

    /**
     * sets the number of items sold
     * @param soldNumber soldNumber
     */
    public void setSoldNumber(Long soldNumber) {
        this.soldNumber = soldNumber;
    }

    /**
     * @return the revenue that was created by selling the MenuEntry
     */
    public BigDecimal getRevenue() {
        BigDecimal sold = BigDecimal.valueOf(soldNumber);
        return entry.getPrice().multiply(sold);
    }

}

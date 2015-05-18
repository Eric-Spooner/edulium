package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import java.math.BigDecimal;

/**
 * domain object representing a entry in the menu
 * (e.g. a drink, a dish)
 */
public class MenuEntry {
    private Long identity;
    private String name;
    private BigDecimal price;
    private Boolean available;
    private String description;
    private TaxRate taxRate;
    private MenuCategory category;

    /**
     * Creates a new MenuEntry object and assigns the given identity to it.
     * @param identity the identity of the category
     * @return MenuEntry object with the given identity
     */
    public static MenuEntry withIdentity(long identity) {
        MenuEntry menuEntry = new MenuEntry();
        menuEntry.setIdentity(identity);
        return menuEntry;
    }

    /**
     * @return returns the identity of the object (can be null)
     */
    public Long getIdentity() {
        return identity;
    }

    /**
     * sets the identity of the object
     * @param identity identity
     */
    public void setIdentity(Long identity) {
        this.identity = identity;
    }

    /**
     * @return returns the name of the menuEntry (can be null)
     */
    public String getName() {
        return name;
    }

    /**
     * sets the name of the menuEntry
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return returns the gross-price (price with tax) of the menuEntry (can be null)
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * sets the gross-price of the menu entry
     * @param price gross-price (price with tax)
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return true if the menuEntry is currently available for ordering (can be null)
     */
    public Boolean getAvailable() {
        return available;
    }

    /**
     * sets if the menuEntry is currently available for ordering
     * @param available available for ordering
     */
    public void setAvailable(Boolean available) {
        this.available = available;
    }

    /**
     * @return returns the description of the menuEntry (can be null)
     */
    public String getDescription() {
        return description;
    }

    /**
     * sets the description of the menuEntry
     * @param description description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return returns the category of dishes/drinks the menuEntry is in (can be null)
     */
    public MenuCategory getCategory() {
        return category;
    }

    /**
     * sets the category of dishes/drinks the menuEntry is in
     * @param category category
     */
    public void setCategory(MenuCategory category) {
        this.category = category;
    }

    /**
     * @return returns the tax rate of this menuEntry (can be null)
     */
    public TaxRate getTaxRate() {
        return taxRate;
    }

    /**
     * sets the tax rate
     * @param taxRate tax rate
     */
    public void setTaxRate(TaxRate taxRate) {
        this.taxRate = taxRate;
    }

    @Override
    public String toString() {
        return "MenuEntry{" +
                "identity=" + identity +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", available=" + available +
                ", description='" + description + '\'' +
                ", taxRate=" + taxRate +
                ", category=" + category +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuEntry menuEntry = (MenuEntry) o;

        if (identity != null ? !identity.equals(menuEntry.identity) : menuEntry.identity != null) return false;
        if (name != null ? !name.equals(menuEntry.name) : menuEntry.name != null) return false;
        if (price != null ? price.compareTo(menuEntry.price) != 0 : menuEntry.price != null) return false;
        if (available != null ? !available.equals(menuEntry.available) : menuEntry.available != null) return false;
        if (description != null ? !description.equals(menuEntry.description) : menuEntry.description != null) return false;
        if (taxRate != null ? !taxRate.equals(menuEntry.taxRate) : menuEntry.taxRate != null) return false;
        return !(category != null ? !category.equals(menuEntry.category) : menuEntry.category != null);

    }

}

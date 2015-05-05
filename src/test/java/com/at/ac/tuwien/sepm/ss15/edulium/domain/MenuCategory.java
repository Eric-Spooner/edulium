package com.at.ac.tuwien.sepm.ss15.edulium.domain;

/**
 * domain object which represents a category in the menu
 */
public class MenuCategory {
    private Long identity = null;
    private String name = null;

    public MenuCategory() {
    }

    /**
     * @param name name of the category
     */
    public MenuCategory(String name) {
        this.name = name;
    }

    /**
     * @return  returns the category name
     *          can be null (if this instance does not represent
     *          a persistent dataset)
     */
    public String getName() {
        return name;
    }

    /**
     * @param name sets the name of the category
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return  returns the identity if the object is persistent;
     *          otherwise returns null
     */
    public Long getIdentity() {
        return identity;
    }

    /**
     * @param identity sets the identity of this object
     *                 should only be called if the object is persistent
     */
    public void setIdentity(Long identity) {
        this.identity = identity;
    }
}

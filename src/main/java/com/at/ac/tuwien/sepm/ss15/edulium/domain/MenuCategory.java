package com.at.ac.tuwien.sepm.ss15.edulium.domain;

/**
 * domain object which represents a category in the menu
 */
public class MenuCategory {
    private Long identity = null;
    private String name = null;

    /**
     * Creates a new category object and assigns the given identity to it.
     * @param identity the identity of the category
     * @return MenuCategory object with the given identity
     */
    public static MenuCategory withIdentity(long identity) {
        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setIdentity(identity);
        return menuCategory;
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
     * @return  returns the identity of the object;
     *          can be null
     */
    public Long getIdentity() {
        return identity;
    }

    /**
     * @param identity sets the identity of this object
     */
    public void setIdentity(Long identity) {
        this.identity = identity;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuCategory that = (MenuCategory) o;

        if (identity != null ? !identity.equals(that.identity) : that.identity != null) return false;
        return !(name != null ? !name.equals(that.name) : that.name != null);
    }

    @Override
    public int hashCode() {
        int result = identity != null ? identity.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}

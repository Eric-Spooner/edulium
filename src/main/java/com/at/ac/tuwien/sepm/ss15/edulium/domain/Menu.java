package com.at.ac.tuwien.sepm.ss15.edulium.domain;

/**
 * domain object which represents a Menu
 */
public class Menu {
    private Long identity = null;
    private String name = null;
    private Boolean deleted = null;


    /**
     *
     * @param identity
     * @return a menu with the given identity
     */
    private static Menu withIdentity(long identity){
        Menu menu = new Menu();
        menu.setIdentity(identity);
        return menu;
    }

    /**
     *
     * @return the identity of the menu
     */
    public Long getIdentity() {
        return identity;
    }

    /**
     * sets the identity of the menu
     * @param identity
     */
    public void setIdentity(Long identity) {
        this.identity = identity;
    }

    /**
     *
      * @return the name of the menu
     */
    public String getName() {
        return name;
    }

    /**
     * sets the name of the menu
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return the condition deleted of the menu
     */
    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * sets the deleted parameter
     * @param deleted
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Menu menu = (Menu) o;

        if (deleted != null ? !deleted.equals(menu.deleted) : menu.deleted != null) return false;
        if (identity != null ? !identity.equals(menu.identity) : menu.identity != null) return false;
        if (name != null ? !name.equals(menu.name) : menu.name != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "identity=" + identity +
                ", name='" + name + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}

package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import java.util.LinkedList;
import java.util.List;

/**
 * domain object which represents a Menu
 */
public class Menu{
    private Long identity = null;
    private String name = null;
    private List<MenuEntry> entries = null;

    /**
     *
     * @param identity
     * @return a menu with the given identity
     */
    public static Menu withIdentity(long identity){
        Menu menu = new Menu();
        menu.setIdentity(identity);
        return menu;
    }

    /**
     *
     * @return the entries of the menu
     */
    public List<MenuEntry> getEntries() {
        return entries;
    }


    /**
     * sets the entries list of the menue
     * @param entries
     */
    public void setEntries(List<MenuEntry> entries) {
        this.entries = entries;
    }

    /**
     * Initialise the list, if its null and add the given entry
     * @param entry (should not be null)
     */
    public void addEntry(MenuEntry entry){
        if(this.entries == null){
            this.entries = new LinkedList<MenuEntry>();
        }
        this.entries.add(entry);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Menu menu = (Menu) o;

        if (entries != null ? !entries.equals(menu.entries) : menu.entries != null) return false;
        if (identity != null ? !identity.equals(menu.identity) : menu.identity != null) return false;
        if (name != null ? !name.equals(menu.name) : menu.name != null) return false;

        return true;
    }


    @Override
    public String toString() {
        return "Menu{" +
                "identity=" + identity +
                ", name='" + name + '\'' +
                ", entries=" + entries +
                '}';
    }
}

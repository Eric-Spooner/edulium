package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import java.math.BigDecimal;

/**
 * domain object which represents a user
 */
public class User {
    private String identity = null;
    private String name = null;
    private String role = null;
    private BigDecimal tip = null;

    /**
     * Creates a new user object and assigns the given identity to it.
     * @param identity the identity of the user
     * @return User object with the given identity
     */
    public static User withIdentity(String identity) {
        User user = new User();
        user.setIdentity(identity);
        return user;
    }

    /**
     * @return the identity of the user (can be null)
     */
    public String getIdentity() {
        return identity;
    }

    /**
     * Sets the identity of the user
     * @param identity identity of the user
     */
    public void setIdentity(String identity) {
        this.identity = identity;
    }

    /**
     * @return the name of the user (can be null)
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user
     * @param name name of the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the role of the user (can be null)
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the user
     * @param role role of the user
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     *
     * @return the tip of the User
     */
    public BigDecimal getTip() {
        return tip;
    }

    /**
     * set the tip of the User
     * @param tip
     */
    public void setTip(BigDecimal tip) {
        this.tip = tip;
    }

    @Override
    public String toString() {
        return "User{" +
                "identity=" + identity +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", tip='" + tip + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (identity != null ? !identity.equals(user.identity) : user.identity != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (role != null ? !role.equals(user.role) : user.role != null) return false;
        return !(tip != null ? (user.tip != null ? tip.compareTo(user.tip) != 0 : tip.compareTo(BigDecimal.ZERO) != 0) :
                               (user.tip != null ? user.tip.compareTo(BigDecimal.ZERO) != 0 : false));
    }

    @Override
    public int hashCode() {
        int result = identity != null ? identity.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (tip != null ? tip.hashCode() : BigDecimal.ZERO.hashCode());
        return result;
    }

    @Override
    public User clone() {
        User user = new User();
        user.setIdentity(identity);
        user.setName(name);
        user.setRole(role);
        user.setTip(tip);
        return user;
    }
}

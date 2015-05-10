package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import java.math.BigDecimal;

/**
 * domain object which represents a tax rate
 */
public class TaxRate {
    private Long identity = null;
    private BigDecimal value = null;

    /**
     * Creates a new user object and assigns the given identity to it.
     * @param identity the identity of the user
     * @return User object with the given identity
     */
    public static TaxRate withIdentity(long identity) {
        TaxRate taxRate = new TaxRate();
        taxRate.setIdentity(identity);
        return taxRate;
    }

    /**
     * @return the identity of the tax rate (can be null)
     */
    public Long getIdentity() {
        return identity;
    }

    /**
     * Sets the identity of the tax rate
     * @param identity identity of the tax rate
     */
    public void setIdentity(Long identity) {
        this.identity = identity;
    }

    /**
     * @return the tax rate value in percent (can be null)
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * Sets the tax rate value of the tax rate
     * @param value tax rate value in percent
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TaxRate{" +
                "identity=" + identity +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaxRate)) return false;

        TaxRate taxRate = (TaxRate) o;

        if (identity != null ? !identity.equals(taxRate.identity) : taxRate.identity != null) return false;
        return !(value != null ? !value.equals(taxRate.value) : taxRate.value != null);

    }
}

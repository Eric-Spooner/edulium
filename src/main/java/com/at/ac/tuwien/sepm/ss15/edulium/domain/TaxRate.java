package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import java.math.BigDecimal;

/**
 * domain object which represents a tax rate
 */
public class TaxRate {
    private Long identity = null;
    private BigDecimal value = null;

    /**
     * Creates a new tax rate object and assigns the given identity to it.
     * @param identity the identity of the tax rate
     * @return TaxRate object with the given identity
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
     * @return the tax rate value which is between 0.0 and 1.0
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * Sets the tax rate value of the tax rate
     * @param value tax rate value which is between 0.0 and 1.0
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
        return !(value != null ? (value.compareTo(taxRate.value) != 0) : taxRate.value != null);

    }

    @Override
    public int hashCode() {
        int result = identity != null ? identity.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public TaxRate clone() {
        TaxRate tax = new TaxRate();
        tax.setValue(value);
        tax.setIdentity(identity);
        return tax;
    }

}

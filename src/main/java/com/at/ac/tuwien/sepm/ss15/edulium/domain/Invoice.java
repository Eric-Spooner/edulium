package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * Domain object representing an invoice
 */
public class Invoice {
    private Long identity;
    private LocalDateTime time;
    private BigDecimal gross;
    private User creator;

    /**
     * Creates a new invoice object and assigns the given identity to it
     * @param identity The identity of the invoice
     * @return New invoice object with the provided identity
     */
    public static Invoice withIdentity(long identity) {
        Invoice invoice = new Invoice();
        invoice.setIdentity(identity);
        return invoice;
    }

    /**
     * @return Returns the unique identity of the invoice (can be null)
     */
    public Long getIdentity() {
        return identity;
    }

    /**
     * @param identity Sets the unique identity of the object.
     */
    public void setIdentity(Long identity) {
        this.identity = identity;
    }

    /**
     * @return Returns the date when the invoice was created (can be null)
     */
    public LocalDateTime getTime() {
        return time;
    }

    /**
     * @param time Sets the date when the invoice was created
     */
    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    /**
     * @return Returns the total gross amount of the invoice (can be null)
     */
    public BigDecimal getGross() {
        return gross;
    }

    /**
     * @param gross Sets the total gross amount of the invoice
     */
    public void setGross(BigDecimal gross) {
        this.gross = gross;
    }

    /**
     * @return Returns the creator of this invoice (can be null)
     */
    public User getCreator() {
        return creator;
    }

    /**
     * @param creator The creator of this invoice
     */
    public void setCreator(User creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "identity=" + identity +
                ", time=" + time +
                ", gross=" + gross +
                ", creator=" + creator +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Invoice invoice = (Invoice) o;

        if (identity != null ? !identity.equals(invoice.identity) : invoice.identity != null) return false;
        if (time != null ? !time.equals(invoice.time) : invoice.time != null) return false;
        if (gross != null ? gross.compareTo(invoice.gross) != 0 : invoice.gross != null) return false;
        return !(creator != null ? !creator.equals(invoice.creator) : invoice.creator != null);
    }
}
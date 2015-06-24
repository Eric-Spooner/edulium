package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Domain object representing an invoice
 */
public class Invoice {
    private Long identity;
    private LocalDateTime time;
    private BigDecimal gross;
    private User creator;
    private Boolean closed;
    private List<Order> orders;

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

    /**
     * @return Returns whether the invoice was paid or not (can be null)
     */
    public Boolean getClosed() {
        return closed;
    }

    /**
     * @param closed True if invoice was paid, or false otherwise
     */
    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    /**
     * @return Returns the orders of the invoice (can be null)
     */
    public List<Order> getOrders() {
        return orders;
    }

    /**
     * @param orders The orders of the invoice
     */
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "identity=" + identity +
                ", time=" + time +
                ", gross=" + gross +
                ", creator=" + creator +
                ", orders=" + orders +
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
        if (creator != null ? !creator.equals(invoice.creator) : invoice.creator != null) return false;
        if (closed != null ? !closed.equals(invoice.closed) : invoice.closed != null) return false;
        return !(orders != null ? !orders.equals(invoice.orders) : invoice.orders != null);

    }

    @Override
    public int hashCode() {
        int result = identity != null ? identity.hashCode() : 0;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (gross != null ? gross.hashCode() : 0);
        result = 31 * result + (creator != null ? creator.hashCode() : 0);
        result = 31 * result + (closed != null ? closed.hashCode() : 0);
        result = 31 * result + (orders != null ? orders.hashCode() : 0);
        return result;
    }
}

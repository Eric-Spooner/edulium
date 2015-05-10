package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import java.util.Date;

/**
 * Domain object representing an invoice
 */
public class Invoice {
    private Long identity;
    private Date time;
    private Double gross;
    private Double paid;
    private Boolean canceled;

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
     * @return Returns the unique identity of the invoice
     */
    public Long getIdentity() {
        return identity;
    }

    /**
     * @param identity Sets the unique identity of the object.
     *                 This method should only be used when creating
     *                 a new entry in the datasource, where the identity
     *                 is being generated
     */
    public void setIdentity(Long identity) {
        this.identity = identity;
    }

    /**
     * @return Returns the date when the invoice was created
     */
    public Date getTime() {
        return time;
    }

    /**
     * @param time Sets the date when the invoice was created
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * @return Returns the total gross amount of the invoice
     */
    public Double getGross() {
        return gross;
    }

    /**
     * @param gross Sets the total gross amount of the invoice
     */
    public void setGross(Double gross) {
        this.gross = gross;
    }

    /**
     * @return Returns the paid amount
     */
    public Double getPaid() {
        return paid;
    }

    /**
     * @param paid Returns the paid amount
     */
    public void setPaid(Double paid) {
        this.paid = paid;
    }

    /**
     * @return Returns true if the invoice has been cancelled
     * and false otherwise.
     */
    public Boolean getCanceled() {
        return canceled;
    }

    /**
     * @param canceled Set to true if the invoice was cancelled,
     *                 otherwise to false.
     */
    public void setCanceled(Boolean canceled) {
        this.canceled = canceled;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "identity=" + identity +
                ", time=" + time +
                ", gross=" + gross +
                ", paid=" + paid +
                ", canceled=" + canceled +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || this.getClass() != o.getClass())
            return false;

        Invoice that = (Invoice) o;

        return (this.identity == that.getIdentity() &&
                this.time.equals(that.getTime()) &&
                this.gross == that.getGross() &&
                this.paid == that.getPaid() &&
                this.canceled == that.canceled);
    }
}

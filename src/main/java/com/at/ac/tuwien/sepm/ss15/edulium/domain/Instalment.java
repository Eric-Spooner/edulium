package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain object representing an instalment
 */
public class Instalment {
    private Long identity;
    private LocalDateTime time;
    private String paymentInfo;
    private String type;
    private BigDecimal amount;
    private Invoice invoice;

    /**
     * Creates a new instalment object and assigns the given identity to it
     * @param identity The identity of the instalment
     * @return New instalment object with the provided identity
     */
    public static Instalment withIdentity(Long identity) {
        Instalment instalment = new Instalment();
        instalment.setIdentity(identity);
        return instalment;
    }

    /**
     * @return Returns the unique identity of the instalment (can be null)
     */
    public Long getIdentity() {
        return identity;
    }

    /**
     * Sets the unique identity of the instalment object.
     * @param identity The unique identity we want to assign to the
     *                 instalment object.
     */
    public void setIdentity(Long identity) {
        this.identity = identity;
    }

    /**
     * @return Returns the date-time when the payment took place (can be null)
     */
    public LocalDateTime getTime() {
        return time;
    }

    /**
     * @param time The time we want to assign to the payment being made in this
     *             instalment.
     */
    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    /**
     * @return Returns payment information
     */
    public String getPaymentInfo() {
        return paymentInfo;
    }

    /**
     * @param paymentInfo The payment info we want to provide to the object
     */
    public void setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    /**
     * @return Returns the type of payment (can be null)
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type of payment we want to assign
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return Returns the amount that was payed (can be null)
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount The payed amount we want to assign
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return Returns the Invoice that it was assigned to (can be null)
     */
    public Invoice getInvoice() {
        return invoice;
    }

    /**
     * @param invoice The assigned invoice object
     */
    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @Override
    public String toString() {
        return "Instalment{" +
                "identity=" + identity +
                ", time=" + time +
                ", paymentInfo=" + paymentInfo +
                ", type=" + type +
                ", amount=" + amount +
                ", invoice= " + invoice +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Instalment that = (Instalment) o;

        if (identity != null ? !identity.equals(that.identity) : that.identity != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (paymentInfo != null ? !paymentInfo.equals(that.paymentInfo) : that.paymentInfo != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (amount != null ? amount.compareTo(that.amount) != 0 : that.amount != null) return false;
        return !(invoice != null ? !invoice.equals(that.invoice) : that.invoice != null);
    }

    @Override
    public int hashCode() {
        int result = identity != null ? identity.hashCode() : 0;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (paymentInfo != null ? paymentInfo.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (invoice != null ? invoice.hashCode() : 0);
        return result;
    }
}

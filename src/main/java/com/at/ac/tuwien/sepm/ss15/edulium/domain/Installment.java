package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain object representing an installment
 */
public class Installment {
    Long identity;
    LocalDateTime time;
    String paymentInfo;
    String type;
    BigDecimal amount;

    /**
     * Creates a new installment object and assigns the given identity to it
     * @param identity The identity of the installment
     * @return New installment object with the provided identity
     */
    public static Installment withIdentity(Long identity) {
        Installment installment = new Installment();
        installment.setIdentity(identity);
        return installment;
    }

    /**
     * @return Returns the unique identity of the installment (can be null)
     */
    public Long getIdentity() {
        return identity;
    }

    /**
     * Sets the unique identity of the installment object.
     * @param identity The unique identity we want to assign to the
     *                 installment object.
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
     *             installment.
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

    @Override
    public String toString() {
        return "Installment{" +
                "identity=" + identity +
                ", time=" + time +
                ", paymentInfo=" + paymentInfo +
                ", type=" + type +
                ", amount" + amount +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Installment that = (Installment) o;

        if (identity != null ? !identity.equals(that.identity) : that.identity != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (paymentInfo != null ? !paymentInfo.equals(that.paymentInfo) : that.paymentInfo != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return !(amount != null ? amount.compareTo(that.amount) != 0 : that.amount != null);
    }
}

package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import java.util.Date;

/**
 * domain object which represents a reservation
 */
public class Reservation {
    private Long identity = null;
    private Date time = null;
    private String name = null;
    private Integer quantity = null;
    private Integer duration = null;

    /**
     * @return the identity of the reservation (can be null)
     */
    public Long getIdentity() {
        return identity;
    }

    /**
     * Sets the identity of the reservation
     * @param identity sets the identity of the reservation
     */
    public void setIdentity(Long identity) {
        this.identity = identity;
    }

    /**
     * @return the start time of the reservation (can be null)
     */
    public Date getTime() { return time; }

    /**
     * Sets the start time of the reservation
     * @param time start time of the reservation
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * @return the name of the guest who made the reservation (can be null)
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the guest who made the reservation
     * @param name name of the guest who made the reservation
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the number of people for the reservation (can be null)
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Sets the number of people for the reservation
     * @param quantity number of people for the reservation
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * @return duration of the reservation in minutes (can be null)
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the reservation
     * @param duration duration of the reservation in minutes
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "identity=" + identity +
                ", time=" + time +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;

        Reservation that = (Reservation) o;

        if (identity != null ? !identity.equals(that.identity) : that.identity != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (quantity != null ? !quantity.equals(that.quantity) : that.quantity != null) return false;
        return !(duration != null ? !duration.equals(that.duration) : that.duration != null);

    }
}

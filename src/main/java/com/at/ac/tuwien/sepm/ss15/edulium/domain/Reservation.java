package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * domain object which represents a reservation
 */
public class Reservation {
    private Long identity = null;
    private LocalDateTime time = null;
    private String name = null;
    private Integer quantity = null;
    private Duration duration = null;
    private List<Table> tables = null;

    /**
     * Creates a new reservation object and assigns the given identity to it.
     * @param identity the identity of the reservation
     * @return Reservation object with the given identity
     */
    public static Reservation withIdentity(long identity) {
        Reservation reservation = new Reservation();
        reservation.setIdentity(identity);
        return reservation;
    }

    /**
     * @return the identity of the reservation (can be null)
     */
    public Long getIdentity() {
        return identity;
    }

    /**
     * Sets the identity of the reservation
     * @param identity identity of the reservation
     */
    public void setIdentity(Long identity) {
        this.identity = identity;
    }

    /**
     * @return the start time of the reservation (can be null)
     */
    public LocalDateTime getTime() { return time; }

    /**
     * Sets the start time of the reservation
     * @param time start time of the reservation
     */
    public void setTime(LocalDateTime time) {
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
     * @return duration of the reservation (can be null)
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the reservation
     * @param duration duration of the reservation
     */
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    /**
     * @return the reserved tables for the reservation (can be null)
     */
    public List<Table> getTables() {
        return tables;
    }

    /**
     * Sets the reserved tables for the reservation
     * @param tables reserved tables for the reservation
     */
    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "identity=" + identity +
                ", time=" + time +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", duration=" + duration +
                ", tables=" + tables +
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
        if (duration != null ? !duration.equals(that.duration) : that.duration != null) return false;
        return !(tables != null ? !tables.containsAll(that.tables) : that.tables != null);

    }

    @Override
    public int hashCode() {
        int result = identity != null ? identity.hashCode() : 0;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (tables != null ? tables.hashCode() : 0);
        return result;
    }
}

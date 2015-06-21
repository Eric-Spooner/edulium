package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Domain object representing an intermittentSale
 */
public class IntermittentSale extends Sale {

    private Set<DayOfWeek> daysOfSale = null;
    private LocalTime fromDayTime = null;
    private Duration duration = null;
    private Boolean enabled = null;

    /**
     * Creates a new intermittentSale object and assigns the given identity to it.
     * @param identity the identity of the intermittentSale
     * @return IntermittentSale object with the given identity
     */
    public static IntermittentSale withIdentity(long identity) {
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(identity);
        return intermittentSale;
    }

    /**
     * @return returns a set of days on which this sale is active
     */
    public Set<DayOfWeek> getDaysOfSale() {
        return daysOfSale;
    }

    /**
     * sets the days on which the sale is active
     * @param days days
     */
    public void setDaysOfSale(Set<DayOfWeek> days) {
        this.daysOfSale = days;
    }

    /**
     * @return Returns the dateTime when the intermittentSale starts.
     */
    public LocalTime getFromDayTime() {
        return fromDayTime;
    }

    /**
     * @param fromDayTime Sets the dateTime when the intermittentSale starts.
     */
    public void setFromDayTime(LocalTime fromDayTime) {
        this.fromDayTime = fromDayTime;
    }

    /**
     * @return Returns the duration of the intermittentSale
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     * @param duration Sets the duration of the intermittentSale
     */
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    /**
     * @return Returns true if the intermittentSale is enabled.
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * Sets whether or not the intermittentSale is enabled.
     * @param enabled Set to true if the intermittentSale is enabled and false otherwise.
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "IntermittentSale{" +
                "daysOfSale=" + daysOfSale +
                ", fromDayTime=" + fromDayTime +
                ", duration=" + duration +
                ", enabled=" + enabled +
                '}' + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        IntermittentSale that = (IntermittentSale) o;

        if (daysOfSale != null ? !daysOfSale.equals(that.daysOfSale) : that.daysOfSale != null) return false;
        if (fromDayTime != null ? !fromDayTime.equals(that.fromDayTime) : that.fromDayTime != null) return false;
        if (duration != null ? !duration.equals(that.duration) : that.duration != null) return false;
        return !(enabled != null ? !enabled.equals(that.enabled) : that.enabled != null);
    }

    @Override
    public boolean isAt(LocalDateTime time) {
        // Same day of the week?
        if(daysOfSale == null || !daysOfSale.contains(time.getDayOfWeek())) {
            return false;
        }

        // check time
        return fromDayTime.isBefore(time.toLocalTime()) && fromDayTime.plus(duration).isAfter(time.toLocalTime());
    }
}
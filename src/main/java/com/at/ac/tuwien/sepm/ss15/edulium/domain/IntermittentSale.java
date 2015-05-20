package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import java.time.LocalDateTime;

/**
 * Domain object representing an intermittentSale
 */
public class IntermittentSale extends Sale {
    private Boolean monday = null;
    private Boolean tuesday = null;
    private Boolean wednesday = null;
    private Boolean thursday = null;
    private Boolean friday = null;
    private Boolean saturday = null;
    private Boolean sunday = null;
    private LocalDateTime fromDayTime = null;
    private Integer duration = null;
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
     * @return Returns true if the intermittentSale is active on mondays.
     */
    public Boolean getMonday() {
        return monday;
    }

    /**
     * Sets whether or not the intermittentSale is active on mondays.
     * @param monday Set to true if the intermittentSale is active on mondays and false otherwise.
     */
    public void setMonday(Boolean monday) {
        this.monday = monday;
    }

    /**
     * @return Returns true if the intermittentSale is active on tuesdays.
     */
    public Boolean getTuesday() {
        return tuesday;
    }

    /**
     * Sets whether or not the intermittentSale is active on tuesdays.
     * @param tuesday Set to true if the intermittentSale is active on tuesdays and false otherwise.
     */
    public void setTuesday(Boolean tuesday) {
        this.tuesday = tuesday;
    }

    /**
     * @return Returns true if the intermittentSale is active on wednesdays.
     */
    public Boolean getWednesday() {
        return wednesday;
    }

    /**
     * Sets whether or not the intermittentSale is active on wednesdays.
     * @param wednesday Set to true if the intermittentSale is active on wednesdays and false otherwise.
     */
    public void setWednesday(Boolean wednesday) {
        this.wednesday = wednesday;
    }

    /**
     * @return Returns true if the intermittentSale is active on thursdays.
     */
    public Boolean getThursday() {
        return thursday;
    }

    /**
     * Sets whether or not the intermittentSale is active on thursdays.
     * @param thursday Set to true if the intermittentSale is active on thursdays and false otherwise.
     */
    public void setThursday(Boolean thursday) {
        this.thursday = thursday;
    }

    /**
     * @return Returns true if the intermittentSale is active on fridays.
     */
    public Boolean getFriday() {
        return friday;
    }

    /**
     * Sets whether or not the intermittentSale is active on fridays.
     * @param friday Set to true if the intermittentSale is active on fridays and false otherwise.
     */
    public void setFriday(Boolean friday) {
        this.friday = friday;
    }

    /**
     * @return Returns true if the intermittentSale is active on saturdays.
     */
    public Boolean getSaturday() {
        return saturday;
    }

    /**
     * Sets whether or not the intermittentSale is active on saturdays.
     * @param saturday Set to true if the intermittentSale is active on saturdays and false otherwise.
     */
    public void setSaturday(Boolean saturday) {
        this.saturday = saturday;
    }

    /**
     * @return Returns true if the intermittentSale is active on sundays.
     */
    public Boolean getSunday() {
        return sunday;
    }

    /**
     * Sets whether or not the intermittentSale is active on sundays.
     * @param sunday Set to true if the intermittentSale is active on sundays and false otherwise.
     */
    public void setSunday(Boolean sunday) {
        this.sunday = sunday;
    }

    /**
     * @return Returns the dateTime when the intermittentSale starts.
     */
    public LocalDateTime getFromDayTime() {
        return fromDayTime;
    }

    /**
     * @param fromDayTime Sets the dateTime when the intermittentSale starts.
     */
    public void setFromDayTime(LocalDateTime fromDayTime) {
        this.fromDayTime = fromDayTime;
    }

    /**
     * @return Returns the duration of the intermittentSale in minutes.
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * @param duration Sets the duration of the intermittentSale in minutes.
     */
    public void setDuration(Integer duration) {
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
                "identity=" + identity +
                ", name=" + name +
                ", monday=" + monday +
                ", tuesday=" + tuesday +
                ", wednesday=" + wednesday +
                ", thursday=" + thursday +
                ", friday=" + friday +
                ", saturday=" + saturday +
                ", sunday=" + sunday +
                ", fromDayTime=" + fromDayTime +
                ", duration=" + duration +
                ", enabled=" + enabled +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntermittentSale)) return false;

        IntermittentSale iSale = (IntermittentSale) o;

        if (identity != null ? !identity.equals(iSale.identity) : iSale.identity != null) return false;
        if (name != null ? !name.equals(iSale.name) : iSale.name != null) return false;
        if (monday != null ? !monday.equals(iSale.monday) : iSale.monday != null) return false;
        if (tuesday != null ? !tuesday.equals(iSale.tuesday) : iSale.tuesday != null) return false;
        if (wednesday != null ? !wednesday.equals(iSale.wednesday) : iSale.wednesday != null) return false;
        if (thursday != null ? !thursday.equals(iSale.thursday) : iSale.thursday != null) return false;
        if (friday != null ? !friday.equals(iSale.friday) : iSale.friday != null) return false;
        if (saturday != null ? !saturday.equals(iSale.saturday) : iSale.saturday != null) return false;
        if (sunday != null ? !sunday.equals(iSale.sunday) : iSale.sunday != null) return false;
        if (fromDayTime != null ? !fromDayTime.equals(iSale.fromDayTime) : iSale.fromDayTime != null) return false;
        if (duration != null ? !duration.equals(iSale.duration) : iSale.duration != null) return false;
        return !(enabled != null ? !enabled.equals(iSale.enabled) : iSale.enabled != null);

    }

    @Override
    public boolean isAt(LocalDateTime time) {
        return false; //TODO
    }
}

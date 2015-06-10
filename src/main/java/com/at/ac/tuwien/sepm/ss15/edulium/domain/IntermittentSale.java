package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import java.time.DayOfWeek;
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
                ", entries=" + entries +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        IntermittentSale that = (IntermittentSale) o;

        if (duration != null ? !duration.equals(that.duration) : that.duration != null) return false;
        if (enabled != null ? !enabled.equals(that.enabled) : that.enabled != null) return false;
        if (friday != null ? !friday.equals(that.friday) : that.friday != null) return false;
        if (fromDayTime != null ? !fromDayTime.equals(that.fromDayTime) : that.fromDayTime != null) return false;
        if (monday != null ? !monday.equals(that.monday) : that.monday != null) return false;
        if (saturday != null ? !saturday.equals(that.saturday) : that.saturday != null) return false;
        if (sunday != null ? !sunday.equals(that.sunday) : that.sunday != null) return false;
        if (thursday != null ? !thursday.equals(that.thursday) : that.thursday != null) return false;
        if (tuesday != null ? !tuesday.equals(that.tuesday) : that.tuesday != null) return false;
        if (wednesday != null ? !wednesday.equals(that.wednesday) : that.wednesday != null) return false;
        if (entries == null) {
            return that.entries==null;
        } else {
            if (that.entries==null) {
                return false;
            }
            boolean sameKeySet = true;
            if (entries.keySet().size() != that.entries.keySet().size()) {
                sameKeySet = false;
            } else {
                for (MenuEntry e : entries.keySet()) {
                    boolean equality = false;
                    for (MenuEntry e2 : that.entries.keySet()) {
                        if (e.equals(e2)) {
                            equality = true;
                        }
                    }
                    if (!equality) {
                        sameKeySet=false;
                    }
                }
            }
            if (sameKeySet) {
                for (MenuEntry e : entries.keySet()) {
                    MenuEntry e2 = null;
                    for (MenuEntry et : that.entries.keySet()) {
                        if (e.equals(et)) {
                            e2 = et;
                        }
                    }
                    if (entries.get(e).compareTo(that.entries.get(e2)) != 0) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        if (identity != null ? !identity.equals(that.identity) : that.identity != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public boolean isAt(LocalDateTime time) {
        // Same day of the week?
        boolean dayMatch = false;
        if (time.getDayOfWeek().equals(DayOfWeek.MONDAY) && this.monday!=null && this.monday == true) {
            dayMatch = true;
        } else if (time.getDayOfWeek().equals(DayOfWeek.TUESDAY) && this.tuesday!=null && this.tuesday == true) {
            dayMatch = true;
        } else if (time.getDayOfWeek().equals(DayOfWeek.WEDNESDAY) && this.wednesday!=null && this.wednesday == true) {
            dayMatch = true;
        } else if (time.getDayOfWeek().equals(DayOfWeek.THURSDAY) && this.thursday!=null && this.thursday == true) {
            dayMatch = true;
        } else if (time.getDayOfWeek().equals(DayOfWeek.FRIDAY) && this.friday!=null && this.friday == true) {
            dayMatch = true;
        } else if (time.getDayOfWeek().equals(DayOfWeek.SATURDAY) && this.saturday!=null && this.saturday == true) {
            dayMatch = true;
        } else if (time.getDayOfWeek().equals(DayOfWeek.SUNDAY) && this.sunday!=null && this.sunday == true) {
            dayMatch = true;
        }
        if (!dayMatch) {
            return false;
        }
        // Same day time?
        // Count minutes since midnight
        int timeMinutes = time.getHour()*60+time.getMinute();
        int begin = this.fromDayTime.getHour()*60+this.fromDayTime.getMinute();
        int end = begin+duration;
        return (begin <= timeMinutes && timeMinutes <= end);
    }
}

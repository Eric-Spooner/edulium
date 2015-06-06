package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import java.time.LocalDateTime;

/**
 * Domain object representing a OneTimeSale
 */
public class OnetimeSale extends Sale {
    private LocalDateTime fromTime = null;
    private LocalDateTime toTime = null;

    /**
     * Creates a new onetimeSale object and assigns the given identity to it.
     * @param identity the identity of the onetimeSale
     * @return OnetimeSale object with the given identity
     */
    public static OnetimeSale withIdentity(long identity) {
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(identity);
        return onetimeSale;
    }

    /**
     * @return Returns the date when the onetimeSale started.
     */
    public LocalDateTime getFromTime() {
        return fromTime;
    }

    /**
     * @param fromTime Sets the date when the onetimeSale started.
     */
    public void setFromTime(LocalDateTime fromTime) {
        this.fromTime = fromTime;
    }

    /**
     * @return Returns the date when the the onetimeSale ended.
     */
    public LocalDateTime getToTime() {
        return toTime;
    }

    /**
     * @param toTime Sets the date when the onetimeSale ended.
     */
    public void setToTime(LocalDateTime toTime) {
        this.toTime = toTime;
    }

    @Override
    public String toString() {
        return "OnetimeSale{" +
                "identity=" + identity +
                ", name=" + name +
                ", fromTime=" + fromTime +
                ", toTime=" + toTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OnetimeSale)) return false;

        OnetimeSale onetimeSale = (OnetimeSale) o;

        if (identity != null ? !identity.equals(onetimeSale.identity) : onetimeSale.identity != null) return false;
        if (name != null ? !name.equals(onetimeSale.name) : onetimeSale.name != null) return false;
        if (fromTime != null ? !fromTime.equals(onetimeSale.fromTime) : onetimeSale.fromTime != null) return false;
        return !(toTime != null ? !toTime.equals(onetimeSale.toTime) : onetimeSale.toTime != null);

    }

    @Override
    public boolean isAt(LocalDateTime time) {
        if (fromTime.isAfter(time)) {
            return false;
        }
        if (toTime.isBefore(time)) {
            return false;
        }
        return true;
    }
}

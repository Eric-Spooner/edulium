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
                ", entries=" + entries +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OnetimeSale that = (OnetimeSale) o;

        if (fromTime != null ? !fromTime.equals(that.fromTime) : that.fromTime != null) return false;
        if (toTime != null ? !toTime.equals(that.toTime) : that.toTime != null) return false;
        if (entries == null) {
            return that.entries==null;
        } else {
            boolean sameKeySet = true;
            if (entries.keySet().size() != that.entries.keySet().size()) {
                sameKeySet = false;
            } else {
                for (MenuEntry e : entries.keySet()) {
                    boolean equality = false;
                    for (MenuEntry e2 : that.entries.keySet()) {
                        if (e.equals(e2)) {
                            equality = true;
                        } else {
                            System.out.println("A: "+e);
                            System.out.println("B: "+e2);
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
                System.out.println(entries.keySet());
                System.out.println(that.entries.keySet());
                return false;
            }
        }
        if (identity != null ? !identity.equals(that.identity) : that.identity != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
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

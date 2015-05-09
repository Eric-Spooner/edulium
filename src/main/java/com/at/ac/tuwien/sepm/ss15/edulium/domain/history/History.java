package com.at.ac.tuwien.sepm.ss15.edulium.domain.history;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;

import java.util.Date;

/**
 * represents a snapshot of the change history of
 * a domain object.
 */
public class History<T> {
    private User user;
    private Date timeOfChange;
    private Long changeNumber;
    private boolean deleted;
    private T data;

    /**
     * @return if the dataset is deleted
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * sets if the dataset is deleted
     * @param deleted dataset deleted
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * @return returns the user which made the changes
     */
    public User getUser() {
        return user;
    }

    /**
     * sets the user which made the changes
     * @param user user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return returns the date and time when the changes
     *         were made
     */
    public Date getTimeOfChange() {
        return timeOfChange;
    }

    /**
     * sets the date and time when the changes were made
     * @param timeOfChange date and time
     */
    public void setTimeOfChange(Date timeOfChange) {
        this.timeOfChange = timeOfChange;
    }

    /**
     * @return returns the number of change of this domain object
     */
    public Long getChangeNumber() {
        return changeNumber;
    }

    /**
     * sets the number of change of this domain object
     * @param changeNumber number of change
     */
    public void setChangeNumber(Long changeNumber) {
        this.changeNumber = changeNumber;
    }

    /**
     * @return returns the through the user changed data
     */
    public T getData() {
        return data;
    }

    /**
     * sets the changed data
     * @param data changed data
     */
    public void setData(T data) {
        this.data = data;
    }
}

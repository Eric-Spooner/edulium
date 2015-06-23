package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InteriorService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;

/**
 * Created by Administrator on 23.05.2015.
 */
public class Rect {
    private double x, y, w, h;
    private Section section;
    private long number;
    private long identity;
    private InteriorService interiorService;
    private int seats; //optional for creation

    public Rect(double x, double y, double w, double h, InteriorService interiorService) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.section = section;
        this.number = number;
        this.interiorService = interiorService;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public long getIdentity() {
        return identity;
    }

    public void setIdentity(long identity) {
        this.identity = identity;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getW() {
        return w;
    }

    public double getH() {
        return h;
    }

    public int getSeats() {
        return seats;
    }

    public long getNumber() {
        return number;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public Rect getRect(double x, double y) {
        if(x >= this.x && x <= this.x+this.w && y >= this.y && y <= this.y+this.h) {
            return this;
        }

        return null;
    }

    public com.at.ac.tuwien.sepm.ss15.edulium.domain.Table getTable(double x, double y) throws ServiceException {
        if(x >= this.x && x <= this.x+this.w && y >= this.y && y <= this.y+this.h) {
            com.at.ac.tuwien.sepm.ss15.edulium.domain.Table matcher = new com.at.ac.tuwien.sepm.ss15.edulium.domain.Table();
            matcher.setNumber(number);
            matcher.setSection(section);
            if(!interiorService.findTables(matcher).isEmpty())
                return interiorService.findTables(matcher).get(0);
        }

        return null;
    }

    public com.at.ac.tuwien.sepm.ss15.edulium.domain.Section getSection(double x, double y) throws ServiceException {
        if(x >= this.x && x <= this.x+this.w && y >= this.y && y <= this.y+this.h) {
            com.at.ac.tuwien.sepm.ss15.edulium.domain.Section matcher = new com.at.ac.tuwien.sepm.ss15.edulium.domain.Section();
            matcher.setIdentity(identity);
            if(!interiorService.findSections(matcher).isEmpty())
                return interiorService.findSections(matcher).get(0);
        }

        return null;
    }
}
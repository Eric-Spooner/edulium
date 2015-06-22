package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.*;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InteriorService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Rect {
    private double x, y, w, h;
    private Section section;
    private long number;
    private long identity;
    private int seats; //optional for creation

    @Autowired
    private InteriorService interiorService;

    public Rect(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
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

    public Table getTable(double x, double y) throws ServiceException {
        if (x >= this.x && x <= this.x+this.w && y >= this.y && y <= this.y+this.h) {
            List<Table> tables = interiorService.findTables(Table.withIdentity(section, number));
            if (!tables.isEmpty()) {
                return tables.get(0);
            }
        }

        return null;
    }

    public Section getSection(double x, double y) throws ServiceException {
        if (x >= this.x && x <= this.x+this.w && y >= this.y && y <= this.y+this.h) {
            List<Section> sections = interiorService.findSections(Section.withIdentity(identity));
            if (!sections.isEmpty()) {
                return sections.get(0);
            }
        }

        return null;
    }
}
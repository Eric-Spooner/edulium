package com.at.ac.tuwien.sepm.ss15.edulium.gui;

public class Rect {
    private double x, y, w, h;
    private long number;
    private long identity;
    private int seats; //optional for creation

    public Rect(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
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

    public boolean contains(double x, double y) {
        return x >= this.x && x <= this.x+this.w && y >= this.y && y <= this.y+this.h;
    }
}
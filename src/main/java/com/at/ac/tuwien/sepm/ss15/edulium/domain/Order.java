package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import java.sql.Timestamp;
import java.util.stream.LongStream;

/**
 * domain object, which represents orders
 */
public class Order {
    private Long identity = null;
    private Timestamp time = null;
    private Boolean canceled = null;
    private Double brutto = null;
    private Double tax = null;
    private String info = null;
    private Table table = null;
    private MenuEntry menueEntry = null;


    /**
     *
     * @param identity
     * @return Order with the given identity
     */
    public static Order withIdentity(long identity){
        Order order = new Order();
        order.setIdentity(identity);
        return order;
    }

    /**
     * @return the identity of the Order
     */
    public Long getIdentity() {
        return identity;
    }

    /**
     * sets the identity of the order
     *
     * @param identity
     */
    public void setIdentity(Long identity) {
        this.identity = identity;
    }

    /**
     * @return the table, the order is for
     */
    public Table getTable() {
        return table;
    }

    /**
     * sets the table, the order belongs to
     *
     * @param table
     */
    public void setTable(Table table) {
        this.table = table;
    }


    /**
     * @return the time, the order has been placed
     */
    public Timestamp getTime() {
        return time;
    }

    /**
     * sets the time, the order has been placed
     *
     * @param time
     */
    public void setTime(Timestamp time) {
        this.time = time;
    }

    /**
     * @return the condition, if the order has been canceled
     */
    public boolean isCanceled() {
        return canceled;
    }

    /**
     * sets the canceled condition of the order
     *
     * @param canceled
     */
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    /**
     * @return the brutto amount of the order
     */
    public double getBrutto() {
        return brutto;
    }

    /**
     * sets the brutto amount of the order
     *
     * @param brutto
     */
    public void setBrutto(double brutto) {
        this.brutto = brutto;
    }

    /**
     * @return the tax amount of the order
     */
    public double getTax() {
        return tax;
    }

    /**
     * sets the tax amount of the order
     *
     * @param tax
     */
    public void setTax(double tax) {
        this.tax = tax;
    }

    /**
     * @return the info of the order
     */
    public String getInfo() {
        return info;
    }

    /**
     * sets the info of the order
     *
     * @param info
     */
    public void setInfo(String info) {
        this.info = info;
    }

    /**
     *
     * @return the menu entry of the order
     */
    public MenuEntry getMenueEntry() {
        return menueEntry;
    }

    /**
     * sets the menu Entry of the order
     * @param menueEntry
     */
    public void setMenueEntry(MenuEntry menueEntry) {
        this.menueEntry = menueEntry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (Double.compare(order.brutto, brutto) != 0) return false;
        if (canceled != order.canceled) return false;
        if (Double.compare(order.tax, tax) != 0) return false;
        if (!identity.equals(order.identity)) return false;
        if (!info.equals(order.info)) return false;
        if (!time.equals(order.time)) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Order{" +
                "identity=" + identity +
                ", time=" + time +
                ", canceled=" + canceled +
                ", brutto=" + brutto +
                ", tax=" + tax +
                ", info='" + info + '\'' +
                ", table=" + table +
                ", menueEntry=" + menueEntry +
                '}';
    }
}

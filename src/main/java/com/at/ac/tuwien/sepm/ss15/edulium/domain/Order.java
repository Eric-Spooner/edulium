package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * domain object, which represents orders
 */
public class Order {
    private Long identity = null;
    private LocalDateTime time = null;
    private BigDecimal brutto = null;
    private BigDecimal tax = null;
    private String additionalInformation = null;
    private Table table = null;
    private Invoice invoice = null;
    private MenuEntry menuEntry = null;
    private State state = null;

    public enum State {
        QUEUED,
        IN_PROGRESS,
        READY_FOR_DELIVERY,
        DELIVERED
    }

    /**
     * The method is used to generate an Order with the given identity
     * @param identity
     * @return Order with the given identity
     */
    public static Order withIdentity(long identity) {
        Order order = new Order();
        order.setIdentity(identity);
        return order;
    }

    /**
     * @return the identity of the Order (can be null)
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
     * @return the table, the order is for (can be null)
     */
    public Table getTable() {
        return table;
    }

    /**
     * sets the table, the order belongs to (can be null)
     *
     * @param table
     */
    public void setTable(Table table) {
        this.table = table;
    }


    /**
     * @return the time, the order has been placed
     */
    public LocalDateTime getTime() {
        return time;
    }

    /**
     * sets the time, the order has been placed
     *
     * @param time
     */
    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    /**
     * @return the brutto amount of the order (can be null)
     */
    public BigDecimal getBrutto() {
        return brutto;
    }

    /**
     * sets the brutto amount of the order
     *
     * @param brutto
     */
    public void setBrutto(BigDecimal brutto) {
        this.brutto = brutto;
    }

    /**
     * @return the tax amount of the order
     */
    public BigDecimal getTax() {
        return tax;
    }

    /**
     * sets the tax amount of the order
     *
     * @param tax
     */
    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    /**
     * @return the additional Information of the order (can be null)
     */
    public String getAdditionalInformation() {
        return additionalInformation;
    }

    /**
     * sets the info of the order
     *
     * @param additionalInformation
     */
    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    /**
     *
     * @return the menu entry of the order (can be null)
     */
    public MenuEntry getMenuEntry() {
        return menuEntry;
    }

    /**
     * sets the menu Entry of the order
     * @param menueEntry
     */
    public void setMenuEntry(MenuEntry menueEntry) {
        this.menuEntry = menueEntry;
    }

    /**
     *
     * @return the invoice of the order
     */
    public Invoice getInvoice() {
        return invoice;
    }

    /**
     * sets the invoice of the order
     * @param invoice
     */
    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    /*
     * @return the state of the order
     */
    public State getState() {
        return state;
    }

    /**
     * sets the state of the order
     * @param state
     */
    public void setState(State state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (additionalInformation != null ? !additionalInformation.equals(order.additionalInformation) : order.additionalInformation != null)
            return false;
        if (brutto != null ? (brutto.compareTo(order.getBrutto())!=0) : order.brutto != null) return false;
        if (identity != null ? !identity.equals(order.identity) : order.identity != null) return false;
        if (invoice != null ? !invoice.equals(order.invoice) : order.invoice != null) return false;
        if (menuEntry != null ? !menuEntry.equals(order.menuEntry) : order.menuEntry != null) return false;
        if (table != null ? !table.equals(order.table) : order.table != null) return false;
        if (tax != null ? (tax.compareTo(order.getTax())!=0) : order.brutto != null) return false;
        if (time != null ? !time.equals(order.time) : order.time != null) return false;
        if (state != null ? !state.equals(order.state) : order.state != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Order{" +
                "identity=" + identity +
                ", time=" + time +
                ", brutto=" + brutto +
                ", tax=" + tax +
                ", additionalInformation='" + additionalInformation + '\'' +
                ", table=" + table +
                ", invoice=" + invoice +
                ", menuEntry=" + menuEntry +
                ", state=" + state +
                '}';
    }
}

package com.at.ac.tuwien.sepm.ss15.edulium.domain;

/**
 * Created by constantin on 16.05.15.
 */
public class Sale {
    private Long identity = null;
    private String name = null;

    /**
     * Creates a new sale object and assigns the given identity to it.
     * @param identity the identity of the sale
     * @return Sale object with the given identity
     */
    public static Sale withIdentity(long identity) {
        Sale sale = new Sale();
        sale.setIdentity(identity);
        return sale;
    }

    /**
     * @return the identity of the sale (can be null)
     */
    public Long getIdentity() {
        return identity;
    }

    /**
     * Sets the identity of the sale
     * @param identity identity of the sale
     */
    public void setIdentity(Long identity) {
        this.identity = identity;
    }

    /**
     * @return the name of the sale (can be null)
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the sale
     * @param name name of the sale
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "identity=" + identity +
                ", name=" + name +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sale)) return false;

        Sale sale = (Sale) o;

        if (identity != null ? !identity.equals(sale.identity) : sale.identity != null) return false;
        return !(name != null ? !name.equals(sale.name) : sale.name != null);
        
    }
}

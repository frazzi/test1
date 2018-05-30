package com.generic.retailer;

import java.util.List;

public final class CD implements IProduct {
    private static final String type = "CD";
    private static final Double price = 10d;

    @Override
    public String getItemType() {
        return this.type;
    }

    @Override
    public Double getItemPrice() {
        return this.price;
    }

    @Override
    public List<String> getOffers() {
        return null;
    }

}

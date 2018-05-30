package com.generic.retailer;

import java.util.List;

public final class Book implements IProduct {
    private static final String type = "BOOK";
    private static final Double price = 5d;

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

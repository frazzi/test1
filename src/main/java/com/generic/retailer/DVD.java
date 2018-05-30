package com.generic.retailer;

import java.util.Arrays;
import java.util.List;

public class DVD implements IProduct {
    private static final String type = "DVD";
    private static final Double price = 15d;
    private static final String[] Offers = new String[]{"2 FOR 1", "THURS"};

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
        return Arrays.asList(Offers);
    }
}

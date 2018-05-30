package com.generic.retailer;

import java.util.List;

public interface IProduct {
    String getItemType();

    Double getItemPrice();

    List<String> getOffers();
}

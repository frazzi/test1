package com.generic.retailer;

import java.io.BufferedWriter;
import java.time.LocalDate;

public interface ITrolley {
    public void generateReceipt(BufferedWriter writer, LocalDate date);

    public void clearTrolley();

    public void addItem(IProduct IProduct);
}

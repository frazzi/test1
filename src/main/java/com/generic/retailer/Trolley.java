package com.generic.retailer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class Trolley implements ITrolley {

    private static List<IProduct> itemsInTrolley = new ArrayList<>();
    private static Double discountApplied = 0d;

    public void addItem(IProduct IProduct) {
        itemsInTrolley.add(IProduct);
    }

    private static String getReceiptLineForItem(IProduct IProduct) {

        List<IProduct> result = getItemsByItemType(IProduct);

        StringBuilder sb = new StringBuilder();
        String aggrString = result.size() > 1 ? " (x" + result.size() + ") " : "";
        Double aggrPrice = result.stream().mapToDouble(i -> i.getItemPrice()).sum();

        sb.append(padRight(IProduct.getItemType() + aggrString, 10) + padLeft("£" + String.format("%.2f", aggrPrice), 10));
        return sb.toString() + System.lineSeparator();
    }

    private static List<IProduct> getItemsByItemType(IProduct IProduct) {
        return itemsInTrolley.stream()
                .filter(line -> line.getItemType().equals(IProduct.getItemType()))
                .collect(Collectors.toList());
    }

    public void generateReceipt(BufferedWriter writer, LocalDate date) {
        try {
            List<String> items = new ArrayList<>();
            Double aggrPrice = itemsInTrolley.stream().mapToDouble(i -> i.getItemPrice()).sum();

            boolean chk1 = true;
            for (IProduct IProduct : itemsInTrolley) {
                items.add(getReceiptLineForItem(IProduct));
                if (IProduct.getOffers() != null && IProduct.getItemType().equals("DVD") && chk1) {
                    items.add(getDiscountLineForDvditem(IProduct, date, aggrPrice));
                    chk1 = false;
                }
            }

            Set<String> hs = new HashSet<>();
            hs.addAll(items);
            items.clear();
            items.addAll(hs);

            writer.write("===== RECEIPT ======");
            writer.write(System.lineSeparator());

            items.forEach(item -> {
                try {
                    writer.write(item);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            writer.write("====================" + System.lineSeparator());
            writer.write(padRight("TOTAL", 10) + padLeft("£" + String.format("%.2f", aggrPrice - discountApplied), 10));

            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getDiscountLineForDvditem(IProduct dvdIProduct, LocalDate date, Double aggrPrice) {
        List<IProduct> IProducts = getItemsByItemType(dvdIProduct);
        Double discount = 0d;
        boolean chk1 = false;
        if (dvdIProduct.getOffers() != null && dvdIProduct.getItemType().equals("DVD") && dvdIProduct.getOffers().contains("2 FOR 1")) {
            if (IProducts.size() > 1 && IProducts.size() % 2 == 0) {
                discount = (dvdIProduct.getItemPrice() * IProducts.size()) / 2;//Two for one offer
                chk1 = true;
            } else {
                if (IProducts.size() > 1) {
                    discount = (dvdIProduct.getItemPrice() * (IProducts.size() - 1)) / 2;//Two for one offer
                    chk1 = true;
                }
            }
        }

        discountApplied = discount;
        StringBuilder sb = new StringBuilder();

        if (date.getDayOfWeek().equals(DayOfWeek.THURSDAY)) {
            double thursdayDiscount = (aggrPrice - (discount * 2)) * 20 / 100;
            discountApplied += thursdayDiscount;
            sb.append(padRight("THURS", 10) + padLeft("-£" + String.format("%.2f", thursdayDiscount), 10));
            sb.append(System.lineSeparator());
        }
        if (chk1) {
            sb.append(padRight("2 FOR 1", 10) + padLeft("-£" + String.format("%.2f", discount), 10));
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    public void clearTrolley() {
        itemsInTrolley.clear();
    }

    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }
}

package com.trendyol.shipment;

import java.util.*;
import java.util.stream.Collectors;

public class Basket {
    private List<Product> products;

    public ShipmentSize getShipmentSize() {
        if (products.size() < 3) {
            return findMaxSize();
        } else {
            return determineShipmentSize();
        }
    }

    private ShipmentSize findMaxSize() {
        if (products.isEmpty()) {
            return null;
        }

        return products.stream()
                .map(Product::getSize)
                .max(Comparator.naturalOrder())
                .orElse(null);
    }

    private ShipmentSize determineShipmentSize() {
        Map<ShipmentSize, Long> sizeCountMap = products.stream()
                .collect(Collectors.groupingBy(Product::getSize, Collectors.counting()));

        if (sizeCountMap.containsKey(ShipmentSize.X_LARGE) && sizeCountMap.get(ShipmentSize.X_LARGE) >= 3) {
            return ShipmentSize.X_LARGE;
        }

        Optional<Map.Entry<ShipmentSize, Long>> mostFrequentSize = sizeCountMap.entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue));

        if (mostFrequentSize.isPresent()) {
            Map.Entry<ShipmentSize, Long> entry = mostFrequentSize.get();
            if (entry.getValue() >= 3) {
                return getNextSize(entry.getKey());
            }else{
                return findMaxSize();
            }

        } else {
            // If there are no products, return null
            return null;
        }
    }


    private ShipmentSize getNextSize(ShipmentSize currentSize) {
        if (currentSize == ShipmentSize.SMALL) {
            return ShipmentSize.MEDIUM;
        } else if (currentSize == ShipmentSize.MEDIUM) {
            return ShipmentSize.LARGE;
        } else if (currentSize == ShipmentSize.LARGE) {
            return ShipmentSize.X_LARGE;
        } else {
            return null;
        }
    }



    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}

package com.crypfy.core.entity;

import java.util.List;

public class Asset {
    private String name;
    private double quantity;
    private double price;
    private double weightMetric;
    private double weight;
    private List<Double> attributes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWeightMetric() {
        return weightMetric;
    }

    public void setWeightMetric(double weightMetric) {
        this.weightMetric = weightMetric;
    }

    public List<Double> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Double> attributes) {
        this.attributes = attributes;
    }
}

package com.example.electricbillestimate;

import java.io.Serializable;

public class Bill implements Serializable {
    private int id;
    private String month;
    private int unitUsed;
    private double totalCharges;
    private double rebate;
    private double finalCost;

    public Bill(int id, String month, int unitUsed, double totalCharges, double rebate, double finalCost) {
        this.id = id;
        this.month = month;
        this.unitUsed = unitUsed;
        this.totalCharges = totalCharges;
        this.rebate = rebate;
        this.finalCost = finalCost;
    }

    public int getId() { return id; }
    public String getMonth() { return month; }
    public int getUnitUsed() { return unitUsed; }
    public double getTotalCharges() { return totalCharges; }
    public double getRebate() { return rebate; }
    public double getFinalCost() { return finalCost; }
}
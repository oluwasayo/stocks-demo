package com.vaadin.demo.stockdata.ui.newstuff;

import com.vaadin.flow.component.charts.Chart;

public class StockItem extends Chart {

    private final String nasdaqCode;
    private final int numOfShares;
    private final double price;
    private final double[] historicalData;

    public StockItem(String nasdaqCode, int numOfShares, double price, double[] historicalData) {
        super();
        this.nasdaqCode = nasdaqCode;
        this.numOfShares = numOfShares;
        this.price = price;
        this.historicalData = historicalData;
    }

    public String getNasdaqCode() {
        return nasdaqCode;
    }

    public int getNumOfShares() {
        return numOfShares;
    }

    public double getPrice() {
        return price;
    }

    public double[] getHistoricalData() {
        return historicalData;
    }
}

package com.vaadin.demo.stockdata.ui.newstuff;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.dependency.HtmlImport;

@Tag("sparkline-stuff")
@HtmlImport("frontend://newstuff/sparkline-chart.html")
public class SparklineChart extends Chart {

    private final String nasdaqCode;
    private final int numOfShares;
    private final double price;

    public SparklineChart(String nasdaqCode, int numOfShares, double price) {
        super();
        this.nasdaqCode = nasdaqCode;
        this.numOfShares = numOfShares;
        this.price = price;
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
}

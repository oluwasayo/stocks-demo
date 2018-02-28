package com.vaadin.demo.stockdata.ui.newstuff;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.charts.Chart;
import elemental.json.impl.JreJsonFactory;

public class SparklineChart extends Chart {

    private static final String sparklineConfig = "{\n" +
            "          chart: {\n" +
            "            backgroundColor: null,\n" +
            "            borderWidth: 0,\n" +
            "            type: 'line',\n" +
            "            margin: [2, 0, 2, 0],\n" +
            "            width: 120,\n" +
            "            height: 20,\n" +
            "            style: {\n" +
            "              overflow: 'visible'\n" +
            "            },\n" +
            "            skipClone: true\n" +
            "          },\n" +
            "          title: {\n" +
            "            text: ''\n" +
            "          },\n" +
            "          credits: {\n" +
            "            enabled: false\n" +
            "          },\n" +
            "          xAxis: {\n" +
            "            labels: {\n" +
            "              enabled: false\n" +
            "            },\n" +
            "            title: {\n" +
            "              text: null\n" +
            "            },\n" +
            "            startOnTick: false,\n" +
            "            endOnTick: false,\n" +
            "            tickPositions: []\n" +
            "          },\n" +
            "          yAxis: {\n" +
            "            endOnTick: false,\n" +
            "            startOnTick: false,\n" +
            "            labels: {\n" +
            "              enabled: false\n" +
            "            },\n" +
            "            title: {\n" +
            "              text: null\n" +
            "            },\n" +
            "            tickPositions: [0]\n" +
            "          },\n" +
            "          legend: {\n" +
            "            enabled: false\n" +
            "          },\n" +
            "          exporting: {\n" +
            "            enabled: false\n" +
            "          },\n" +
            "          tooltip: {\n" +
            "            enabled: false\n" +
            "          },\n" +
            "          plotOptions: {\n" +
            "            series: {\n" +
            "              animation: false,\n" +
            "              lineWidth: 1,\n" +
            "              shadow: false,\n" +
            "              fillOpacity: 0.25,\n" +
            "              marker: {\n" +
            "                enabled: false\n" +
            "              }\n" +
            "            }\n" +
            "          }\n" +
            "        });";

    private final String nasdaqCode;
    private final int numOfShares;
    private final double price;

    public SparklineChart(String nasdaqCode, int numOfShares, double price) {
        super();
        this.nasdaqCode = nasdaqCode;
        this.numOfShares = numOfShares;
        this.price = price;
    }

    @Override
    public void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        UI.getCurrent().getPage().executeJavaScript("$0.update($1)", this.getElement(),
                new JreJsonFactory().parse(sparklineConfig));
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

package com.vaadin.demo.stockdata.ui.newstuff;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.dependency.HtmlImport;
import elemental.json.JsonValue;
import elemental.json.impl.JreJsonFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;

@HtmlImport("frontend://styles.html")
public class SparklineChart extends Chart {

    private static final Logger L = LoggerFactory.getLogger(SparklineChart.class);

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

        attachEvent.getUI().beforeClientResponse(this, () -> {
            try {
                final JsonValue sparklineConfig = new JreJsonFactory().parse(
                        IOUtils.toString(getClass().getResourceAsStream(
                                "/sparkline-config.json"), Charset.forName("UTF-8")));
                UI.getCurrent().getPage().executeJavaScript("$0.update($1)", this.getElement(), sparklineConfig);
            } catch (IOException e) {
                L.warn("Unable to find sparkline config. This chart will not look/feel like a sparkline.");
            }
        });

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

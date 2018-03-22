package com.vaadin.demo.stockdata.ui.newstuff;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientDelegate;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.charts.Chart;
import elemental.json.JsonValue;
import elemental.json.impl.JreJsonFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;

public class StockChart extends Chart {

    private static final Logger L = LoggerFactory.getLogger(StockChart.class);

    // TODO(oluwasayo): Consider debouncing this callback.
    @Override
    public void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        attachEvent.getUI().beforeClientResponse(this, (context) -> {
            try {
                final JsonValue extremesHook = new JreJsonFactory().parse(
                        IOUtils.toString(getClass().getResourceAsStream(
                                "/extremes-hook.json"), Charset.forName("UTF-8")));
                UI.getCurrent().getPage().executeJavaScript("$0.update($1)", this.getElement(), extremesHook);
            } catch (IOException e) {
                L.warn("Unable to find extremes hook. This stock chart will not listen to extremes events.");
            }
        });
    }

    @ClientDelegate
    public void setExtremes(Double min, Double max) {
        System.out.println("EXTREMES SET TO: " + min + ", " + max);
    }
}

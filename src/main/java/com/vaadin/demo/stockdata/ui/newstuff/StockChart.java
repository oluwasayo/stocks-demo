package com.vaadin.demo.stockdata.ui.newstuff;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientDelegate;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.charts.Chart;
import elemental.json.JsonValue;
import elemental.json.impl.JreJsonFactory;

public class StockChart extends Chart {

    @Override
    public void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        JsonValue extremesHook = new JreJsonFactory().parse(
                "{\"xAxis\": {\"events\": {\"_fn_setExtremes\": \"function(event) { console.error(event.DOMEvent); " +
                        "var valid = event.DOMEvent.path && event.DOMEvent.path[6] && event.DOMEvent.path[6].localName === 'vaadin-chart'; " +
                        "if (valid) { event.DOMEvent.path[6].$server.setExtremes(event.min, event.max); }" +
                        "}\" } } }");

        UI.getCurrent().getPage().executeJavaScript("$0.update($1)", this.getElement(), extremesHook);
    }

    @ClientDelegate
    public void setExtremes(Double min, Double max) {
        System.out.println("EXTREMES SET TO: " + min + ", " + max);
    }
}

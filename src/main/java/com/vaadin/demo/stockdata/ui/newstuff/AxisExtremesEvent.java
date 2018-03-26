package com.vaadin.demo.stockdata.ui.newstuff;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import com.vaadin.flow.component.charts.Chart;

@DomEvent("axis-extremes")
public class AxisExtremesEvent extends ComponentEvent<Chart> {

    private Double min;
    private Double max;

    /**
     * Creates a new event using the given source and indicator whether the
     * event originated from the client side or the server side.
     *
     * @param source     the source component
     * @param fromClient <code>true</code> if the event originated from the client
     */
    public AxisExtremesEvent(Chart source, boolean fromClient,
                             @EventData("event.detail.originalEvent.min") Double min,
                             @EventData("event.detail.originalEvent.max") Double max) {
        super(source, fromClient);
        this.min = min;
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }
}

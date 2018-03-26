package com.vaadin.demo.stockdata.ui.newstuff;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.Labels;
import com.vaadin.flow.component.charts.model.Navigator;
import com.vaadin.flow.component.charts.model.PlotLine;
import com.vaadin.flow.component.charts.model.PlotOptionsSeries;
import com.vaadin.flow.component.charts.model.RangeSelector;
import com.vaadin.flow.component.charts.model.Tooltip;
import com.vaadin.flow.component.charts.model.YAxis;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.BodySize;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.util.Calendar;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Route("")
@Theme(Lumo.class)
@StyleSheet("frontend://newstuff/default.css")
@HtmlImport("frontend://newstuff/sparkline-chart.html")
@BodySize(height = "100vh", width = "100vw")
public class MainView extends HorizontalLayout {

    private final Random randomGen = new Random();

    private Grid<StockItem> grid;

    public MainView() {
        setSizeFull();
        grid = new Grid<>();
        grid.addColumn(StockItem::getNasdaqCode).setWidth("55px");
        grid.addColumn(TemplateRenderer.<StockItem> of(
                "<sparkline-chart>" +
                    "<vaadin-chart-series values=[[item.historicalData]]></vaadin-chart-series>" +
                "</sparkline-chart>")
                .withProperty("historicalData", StockItem::getHistoricalData));
        grid.addColumn(e -> String.format("%.2f", e.getPrice())).setWidth("55px");
        grid.setHeight("100%");
        grid.setWidth("300px");
        grid.getStyle().set("max-width", "350px");
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        grid.setItems(
                Stream.generate(() -> new StockItem(randomName(), randomShares(), randomPrice(),
                        Stream.generate(this::randomPrice).limit(10).mapToDouble(Double::new).toArray()))
                .limit(1000)
                .collect(Collectors.toList()));

        Chart detailChart = newDetailChart();
        detailChart.setWidth("100%");
        detailChart.addListener(AxisExtremesEvent.class,
                event -> System.out.printf("LAMBDA: [min=%.2f, max=%.2f]\n", event.getMin(), event.getMax()));

        super.add(grid, detailChart);
    }

    private Chart newDetailChart() {
        final Chart chart = new Chart();
        chart.setTimeline(true);

        Configuration configuration = chart.getConfiguration();
        configuration.getChart().setType(ChartType.SPLINE);
        configuration.getTitle().setText("Speedment Stock Price");

        YAxis yAxis = new YAxis();
        Labels label = new Labels();
        label.setFormatter("function() { return (this.value > 0 ? ' + ' : '') + this.value + '%'; }");
        yAxis.setLabels(label);

        PlotLine plotLine = new PlotLine();
        plotLine.setValue(2);
        yAxis.setPlotLines(plotLine);
        configuration.addyAxis(yAxis);

        Tooltip tooltip = new Tooltip();
        tooltip.setPointFormat("<span>{series.name}</span>: <b>{point.y}</b> ({point.change}%)<br/>");
        tooltip.setValueDecimals(2);
        configuration.setTooltip(tooltip);

        DataSeries aaplSeries = new DataSeries();
        aaplSeries.setName("Speedment");
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(2018, 01, 01, 0, 0, 0);
        for (int a = 0; a < 100; a++) {
            DataSeriesItem item = new DataSeriesItem();
            item.setX(currentDate.getTime());
            item.setY(randomPrice());
            aaplSeries.add(item);
            currentDate.add(Calendar.DAY_OF_YEAR, 1);
        }

        configuration.setSeries(aaplSeries);

        PlotOptionsSeries plotOptionsSeries = new PlotOptionsSeries();
        configuration.setPlotOptions(plotOptionsSeries);

        RangeSelector rangeSelector = new RangeSelector();
        rangeSelector.setSelected(4);
        configuration.setRangeSelector(rangeSelector);

        Navigator navigator = new Navigator();
        navigator.setAdaptToUpdatedData(false);
        configuration.setNavigator(navigator);

        return chart;
    }

    private double randomPrice() {
        return 75 + (25 * Math.random());
    }

    private int randomShares() {
        return randomGen.nextInt(200);
    }

    private String randomName() {
        final StringBuilder result = new StringBuilder();
        final char A = 'A';
        for (int a = 0; a <= 2 + randomGen.nextInt(2); a++) {
            result.append((char) (A + randomGen.nextInt(26)));
        }
        return result.toString();
    }
}

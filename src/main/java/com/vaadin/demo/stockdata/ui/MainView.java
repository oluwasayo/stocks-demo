package com.vaadin.demo.stockdata.ui;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.Labels;
import com.vaadin.flow.component.charts.model.Marker;
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
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparingDouble;

@Route("")
@Theme(Lumo.class)
@StyleSheet("frontend://style.css")
@HtmlImport("frontend://speedment-chart-theme.html")
@HtmlImport("frontend://sparkline-chart.html")
@BodySize(height = "100vh", width = "100vw")
public class MainView extends HorizontalLayout {

    private final Random randomGen = new Random();

    private Chart detailChart;

    public MainView() {
        setSizeFull();
        Grid<StockItem> grid = new Grid<>();

        // Render the stock item rows. Using templates instead of full components aid performance, a lot!

        grid.addColumn(TemplateRenderer.<StockItem> of(
                "<small><b>[[item.nasdaq]]</b><br>[[item.shares]] shares</small>")
                .withProperty("nasdaq", StockItem::getNasdaqCode)
                .withProperty("shares", StockItem::getNumOfShares))
                .setWidth("55px");

        grid.addColumn(TemplateRenderer.<StockItem> of(
                "<sparkline-chart class$=[[item.trend]]>" +
                    "<vaadin-chart-series values=[[item.historicalData]]></vaadin-chart-series>" +
                "</sparkline-chart>")
                .withProperty("historicalData", StockItem::getHistoricalData)
                .withProperty("trend", StockItem::getTrend));

        grid.addColumn(TemplateRenderer.<StockItem> of(
                "<div style='border-radius=5px;background-color: [[item.trendColor]];border-radius: 5px;color: white;font-weight: bold;padding: 5px 10px 5px 10px;'>$[[item.price]]</div>")
                .withProperty("price", StockItem::getPrice)
                .withProperty("trendColor", StockItem::getTrendColor))
                .setWidth("65px");

        grid.setHeight("100%");
        grid.setWidth("300px");
        grid.getStyle().set("max-width", "370px");
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        // Make random stock items for the side bar. Ideally this should contain symbol data fetched from SERVICE.
        List<StockItem> stockItems = Stream.generate(() -> {
            double[] data = Stream.generate(this::randomPrice).limit(10).mapToDouble(Double::new).toArray();
            return new StockItem(randomName(), randomShares(), randomPrice(), data);
        }).limit(10000).collect(Collectors.toList());

        grid.setItems(stockItems);

        detailChart = newDetailChart();

        grid.select(stockItems.get(0));

        //  Make new chart with random data. Ideally this new chart should have data points fetched from SERVICE.
        grid.addSelectionListener(event -> {
            this.remove(detailChart);
            detailChart = newDetailChart();
            this.add(detailChart);
        });

        super.add(grid, detailChart);
    }

    private Chart newDetailChart() {
        final Chart chart = new Chart();
        chart.setTimeline(true);

        Configuration configuration = chart.getConfiguration();
        configuration.getChart().setType(ChartType.LINE);
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
        tooltip.setPointFormat("<span>Stock value</span>: <b>{point.y}</b> ({point.change}%)<br/>");
        tooltip.setValueDecimals(2);
        configuration.setTooltip(tooltip);

        DataSeries aaplSeries = new DataSeries();
        aaplSeries.setName("Speedment");

        List<DataSeriesItem> dataItems = randomDataSeriesItems();
        aaplSeries.setData(dataItems);
        configuration.setSeries(aaplSeries);

        // Zoom the chart according to data. Not necessary, charts does this automatically.
        Pair<Number, Number> minMax = findMinMax(aaplSeries);
        yAxis.setExtremes(minMax.getLeft(), minMax.getRight());

        // Color the chart based on trend.
        int size = aaplSeries.getData().size();
        String trend = aaplSeries.getData().get(size - 2).getY().doubleValue() >
                aaplSeries.getData().get(size - 1).getY().doubleValue() ? "declining" : "appreciating";
        chart.setClassName(trend);

        PlotOptionsSeries plotOptionsSeries = new PlotOptionsSeries();
        Marker marker = new Marker();
        marker.setEnabled(true);
        plotOptionsSeries.setMarker(marker);
        configuration.setPlotOptions(plotOptionsSeries);

        RangeSelector rangeSelector = new RangeSelector();
        // Enable this to have a range selector and style with CSS or use a vaadin-date-picker.
        rangeSelector.setEnabled(false);
        configuration.setRangeSelector(rangeSelector);

        Navigator navigator = new Navigator();
        navigator.setAdaptToUpdatedData(false);
        configuration.setNavigator(navigator);

        chart.setWidth("100%");

        // Listen to extremes event and send random data within range. Ideally this should be fetched from SERVICE.
        // This could also be done with a vaadin-date-picker for start and end because currently lots of this
        // extremes events get generated for a simple drag so not very efficient (unless debounced).
        chart.addListener(AxisExtremesEvent.class, event -> {
            List<DataSeriesItem> randomDataWithinRange = randomDataSeriesItems().stream()
                    .filter(e -> e.getX().doubleValue() >= event.getMin() && e.getX().doubleValue() <= event.getMax())
                    .collect(Collectors.toList());
            aaplSeries.setData(randomDataWithinRange);
            Pair<Number, Number> newMinMax = findMinMax(aaplSeries);
            configuration.fireAxesRescaled(yAxis, newMinMax.getLeft(), newMinMax.getRight(), false, false);
            aaplSeries.updateSeries();
        });

        return chart;
    }

    private Pair<Number, Number> findMinMax(DataSeries aaplSeries) {
        Number min = aaplSeries.getData().stream().map(DataSeriesItem::getY).min(comparingDouble(Number::intValue)).get();
        Number max = aaplSeries.getData().stream().map(DataSeriesItem::getY).max(comparingDouble(Number::intValue)).get();
        return Pair.of(Math.max(0, min.intValue() - 15), Math.min(100, max.intValue() + 15));
    }

    private List<DataSeriesItem> randomDataSeriesItems() {
        List<DataSeriesItem> dataItems = new ArrayList<>(100);

        Calendar currentDate = Calendar.getInstance();
        currentDate.set(2018, 01, 01, 0, 0, 0);

        // Make random data points.
        DataSeriesItem firstItem = new DataSeriesItem();
        firstItem.setX(currentDate.getTime());
        firstItem.setY(randomPrice());
        dataItems.add(firstItem);
        currentDate.add(Calendar.DAY_OF_YEAR, 1);

        Double lastPrice = (Double) firstItem.getY();

        for (int a = 1; a < 100; a++) {
            DataSeriesItem item = new DataSeriesItem();
            item.setX(currentDate.getTime());
            item.setY(lastPrice + randomVariation(3));
            dataItems.add(item);
            currentDate.add(Calendar.DAY_OF_YEAR, 1);
        }
        return dataItems;
    }

    private double randomPrice() {
        return 35 + (25 * Math.random());
    }

    private double randomVariation(int bounds) {
        double value = bounds * Math.random();
        return randomGen.nextBoolean() ? Math.min(100, 0 + value) : Math.max(0, 0 - value);
    }

    private int randomShares() {
        return randomGen.nextInt(200);
    }

    private String randomName() {
        final StringBuilder result = new StringBuilder();
        final char A = 'A';
        for (int a = 0; a <= 1 + randomGen.nextInt(3); a++) {
            result.append((char) (A + randomGen.nextInt(26)));
        }
        return result.toString();
    }
}
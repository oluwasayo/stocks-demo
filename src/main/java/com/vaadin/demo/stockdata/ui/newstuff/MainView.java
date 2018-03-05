package com.vaadin.demo.stockdata.ui.newstuff;

import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Route("")
@Theme(Lumo.class)
public class MainView extends HorizontalLayout {

    private final Random randomGen = new Random();

    private Grid<SparklineChart> grid;

    public MainView() {
        grid = new Grid<>();
        grid.addColumn(SparklineChart::getNasdaqCode).setWidth("55px");
        grid.addColumn(new ComponentRenderer<>(e -> e));
        grid.addColumn(e -> String.format("%.2f", e.getPrice())).setWidth("55px");
        grid.setSizeFull();
        grid.setWidth("300px");

        grid.setItems(Stream.generate(() -> {
            SparklineChart chart = new SparklineChart(randomName(), randomShares(), randomPrice());
            DataSeries dataSeries = new DataSeries();
            dataSeries.setData(Stream.generate(this::randomPrice).limit(10).toArray(Number[]::new));
            chart.getConfiguration().addSeries(dataSeries);
            return chart;
        }).limit(1000).collect(Collectors.toList()));

        super.add(grid);
    }

    private double randomPrice() {
        return 100 * Math.random();
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

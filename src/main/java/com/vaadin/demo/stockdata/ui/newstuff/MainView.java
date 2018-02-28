package com.vaadin.demo.stockdata.ui.newstuff;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

import java.util.Random;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Route("")
public class MainView extends HorizontalLayout {

    private final Random randomGen = new Random();

    public MainView() {
        final Grid<SparklineChart> grid = new Grid<>();
        grid.addColumn(SparklineChart::getNasdaqCode);
        grid.addColumn(SparklineChart::getElement);
        grid.addColumn(SparklineChart::getPrice);

        grid.setItems(Stream.generate(() -> {
            SparklineChart chart = new SparklineChart(randomName(), randomShares(), randomPrice());

            DataSeries dataSeries = new DataSeries();
            dataSeries.setData(Stream.generate(this::randomPrice).limit(10).toArray(Number[]::new));
            chart.getConfiguration().addSeries(dataSeries);

            return chart;
        }).limit(10).collect(toList()));

//        Grid<String> grid = new Grid<>();
//        grid.setItems(Stream.generate(this::randomName).limit(10).collect(toList()));

        final Div detailView = new Div(new Button("Some random stuff"));
        super.add(grid, detailView);
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
        for (int a = 0; a <= randomGen.nextInt(4); a++) {
            result.append((char) (A + randomGen.nextInt(26)));
        }
        return result.toString();
    }
}

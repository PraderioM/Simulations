package com.simbarra;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class AnaliticFix {
    private static XYSeriesCollection dataset = new XYSeriesCollection();
    private static JFrame frame = new JFrame();
    private static JPanel jPanel1 = new JPanel();
    private static int numchart = 1;
    //we define the stefan boltzmann constant.
    private static double sigma = 5.670367E-8;

    //define the array that is actually going to simulate the heating barr.
    private static int N = 300; //decide in how many parts we will divide the barr.

    private static double[] Points = new double[N]; //define a vector with a component for each part.
    private static JFreeChart chart;


    public static void start (Element material, double Ta, double Tm, double r, double lambda, double A, double period, double L) {
        //we give a name to the new plot.
        XYSeries series = new XYSeries(numchart+material.getName());

        //we make the simulation to plot it.
        Simul(material, Ta, Tm, r, lambda, A, period, L, series);

        //add the simulation to the existing dataset.
        dataset.addSeries(series);

        //plot and show it.
        chart = ChartFactory.createXYLineChart("Temps fix solució analítica.",
                "posició (m)",
                "temperatura (ºC)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                true);

        ChartPanel chartpanel = new ChartPanel(chart);
        chartpanel.setDomainZoomable(true);

        jPanel1.setLayout(new BorderLayout());
        jPanel1.add(chartpanel, BorderLayout.NORTH);

        frame.add(jPanel1);
        frame.pack();
        frame.setVisible(true);

        numchart++;
    }

    public static void clean(){
        try {
            dataset.removeAllSeries();
        }catch(Exception e){ System.out.println("No hi ha grafiques");}
        numchart = 1;
    }

    public static void save(){
        if(dataset.getSeriesCount()!=0) {
            try {
                ChartUtilities.saveChartAsPNG(new File("Analiticposicio" + SimBarra.numsaved), chart, SimBarra.FrameWidth, SimBarra.FrameHeight);
                SimBarra.numsaved++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //We define a function which will run the simulation
    private static void Simul(Element material, double Ta, double Tm, double r, double lambda, double A, double period, double L, XYSeries series) {
        //we calculate some parameters which are going to be usefull to solve a differential equation numerically
        double a = 1 / (1000 * material.density * material.specificHeat);
        double freq = 2 * Math.PI / period;
        double dx = L / N;

        int i;
        //we set an initial approximation of the barr's temperature.
        double K = material.getThermCond((Tm+Ta)/2);
        double m = Math.sqrt(lambda / (K * r) + Math.sqrt(Math.pow(lambda / r, 2) + Math.pow(freq / (2 * a), 2)) / K);
        double h = Math.sqrt(-lambda / (K * r) + Math.sqrt(Math.pow(lambda / r, 2) + Math.pow(freq / (2 * a), 2)) / K);
        double p = Math.sqrt(2 * lambda / (r * K));
        //we set the initial value of the barr's temperature to the theoretical solution.
        for (i = 0; i < N; i++) {
            Points[i] = Ta + (Tm - Ta) * Math.exp(-i * dx * p) + A * Math.exp(-i * dx * m) * Math.sin(-h * i * dx);
            series.add(i * L / N, Points[i]);
        }
    }
}

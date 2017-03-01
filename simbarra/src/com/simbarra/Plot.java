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

public class Plot {
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

    public static void start(Element material, double Ta, double Tm, double r, double lambda, double A, double period, double pos, double L, double Time) {
        //we give a name to the new plot.
        XYSeries series = new XYSeries(numchart+material.getName());

        //we make the simulation to plot it.
        Simul(material, Ta, Tm, r, lambda, A, period, pos, L, Time, series);

        //add the simulation to the existing dataset.
        dataset.addSeries(series);

        //plot and show it.
        chart = ChartFactory.createXYLineChart("Posició fixa",
                "temps (s)",
                "temperatura (ºC)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                true);

        /*int i;
        for (i=0; i<Time; i++){
            System.out.println(dataset.getY(0, i));
        }*/

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
                ChartUtilities.saveChartAsPNG(new File("Simulaciotemps" + SimBarra.numsaved), chart, SimBarra.FrameWidth, SimBarra.FrameHeight);
                SimBarra.numsaved++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //We define a function which will run the simulation
    private static void Simul(Element material, double Ta, double Tm, double r, double lambda, double A, double period, double pos, double L, double Time, XYSeries series){
        //we calculate some parameters which are going to be usefull to solve a differential equation numerically
        double dx = L / N; //distance between two consecutive cells.
        double dt = 0.001;
        double a = 1 / (1000 * material.density * material.specificHeat);
        double b = 2 * a * dt / r;
        a*= dt/(5 * dx * dx);
        double c = material.emissivity * sigma;
        double freq = 2 * Math.PI / period;
        double t, t0, t1, aux, aux1;

        int i, j, k;
        //we set an initial approximation of the barr's temperature.
        double K = material.getThermCond((Tm+Ta)/2);
        double m = Math.sqrt(lambda / (K * r) + Math.sqrt(Math.pow(lambda / r, 2) + Math.pow(freq / (2 * a), 2)) / K);
        double h = Math.sqrt(-lambda / (K * r) + Math.sqrt(Math.pow(lambda / r, 2) + Math.pow(freq / (2 * a), 2)) / K);
        double p = Math.sqrt(2 * lambda / (r * K));
        //we set the initial value of the barr's temperature to the theoretical solution.
        for (i = 0; i < N; i++) {
            Points[i] = Ta + (Tm - Ta) * Math.exp(-i * dx * p) + A * Math.exp(-i * dx * m) * Math.sin(-h * i * dx);
        }

        //We start our simulation, first we let some time go by to stabilize the barr's temperature
        int T = (int) (10/dt);
        int x = (int) Math.max(Math.min(N - 2, pos * N / L), 1);

        for (i = 0; i < T; i++) {
            t0 = Points[0];
            t1 = Points[1];
            Points[0] = Tm + A * Math.sin(freq * i * dt);
            Points[1] = Tm + A * Math.sin(freq * i * dt);
            for (j = 2; j < N-2; j++) { //we calculate approximating the second derivative with second degree centered approximation
                t = Points[j];
                //influence of conductivity
                aux1 = material.getThermCond(t);
                aux = aux1 * a * (t0 + t1 - 4 * t + Points[j + 1]+Points[j + 2]);
                //influence of radiation and convection.
                aux -= b * (t - Ta) * (lambda + c * (t * t + 546.30 * (t + Ta) + Ta * Ta + 149221.845) * (t + Ta + 546.30));
                //if (Math.abs(aux)>1){ aux/=10; System.out.println("shit1");} //We avoid overflows.
                t0 = t1;
                t1 = t;
                Points[j] += aux ;
            }
            t = Points[j];
            t0 = Points[N-1];
            //influence of conductivity
            aux = 5 * material.getThermCond(t) * a * (t1 - 2 * t + t0);
            //influence of radiation and convection.
            aux -= b * (t - Ta) * (lambda + c * (t * t + 546.30 * (t + Ta) + Ta * Ta + 149221.845) * (t + Ta + 546.30));
            Points[j] += aux;
            //influence of conductivity
            aux = 5 * material.getThermCond(t0) * a * (t1 - 2 * t + t0);
            //influence of radiation and convection.
            aux -= b * (t0 - Ta) * (lambda + c * (t * t + 546.30 * (t + Ta) + Ta * Ta + 149221.845) * (t + Ta + 546.30));
            Points[N-1] += aux;
        }

        //we load the data set that we are going to plot.
        for (i = T; i < T + Time; i++) {
            for (k = 0; k < 1 / dt; k++) {
                t0 = Points[0];
                t1 = Points[1];
                Points[0] = Tm + A * Math.sin(freq * i + freq * k * dt);
                Points[1] = Tm + A * Math.sin(freq * i + freq * k * dt);
                for (j = 2; j < N-2; j++) { //we calculate approximating the second derivative with second degree centered approximation
                    t = Points[j];
                    //influence of conductivity
                    aux1 = material.getThermCond(t);
                    aux = aux1 * a * (t0 + t1 - 4 * t + Points[j + 1]+Points[j + 2]);
                    //influence of radiation and convection.
                    aux -= b * (t - Ta) * (lambda + c * (t * t + 546.30 * (t + Ta) + Ta * Ta + 149221.845) * (t + Ta + 546.30));
                    //if (Math.abs(aux)>1){ aux/=10; System.out.println("shit1");} //We avoid overflows.
                    t0 = t1;
                    t1 = t;
                    Points[j] += aux ;
                }
                t = Points[j];
                t0 = Points[N-1];
                //influence of conductivity
                aux = 5 * material.getThermCond(t) * a * (t1 - 2 * t + t0);
                //influence of radiation and convection.
                aux -= b * (t - Ta) * (lambda + c * (t * t + 546.30 * (t + Ta) + Ta * Ta + 149221.845) * (t + Ta + 546.30));
                Points[j] += aux;
                //influence of conductivity
                aux = 5 * material.getThermCond(t0) * a * (t1 - 2 * t + t0);
                //influence of radiation and convection.
                aux -= b * (t0 - Ta) * (lambda + c * (t * t + 546.30 * (t + Ta) + Ta * Ta + 149221.845) * (t + Ta + 546.30));
                Points[N-1] += aux;
            }
            series.add(i-T, Points[x-1]);
        }
    }
}
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

/**
 * Created by marco on 19/02/17.
 */
public class PlotL {
    private static XYSeriesCollection dataset = new XYSeriesCollection();
    private static JFrame frame = new JFrame();
    private static JPanel jPanel1 = new JPanel();

    //define the array that is actually going to simulate the heating barr.
    private static JFreeChart chart;

    public PlotL(XYSeries serie){
        //we give a name to the new plot.
        //XYSeries series = new XYSeries("Horizontal projection");

        //add the simulation to the existing dataset.
        dataset.addSeries(serie);

        //plot and show it.
        chart = ChartFactory.createXYLineChart("Lateral projection",
                "Y (m)",
                "Z (m)",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false);

        ChartPanel chartpanel = new ChartPanel(chart);
        chartpanel.setDomainZoomable(true);

        jPanel1.setLayout(new BorderLayout());
        jPanel1.add(chartpanel, BorderLayout.NORTH);

        frame.add(jPanel1);
        frame.pack();
        frame.setVisible(true);
    }
}

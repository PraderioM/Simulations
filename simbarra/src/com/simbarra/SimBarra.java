package com.simbarra;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;


/**
 * Created by marco on 21/01/17.
 */
public class SimBarra extends JFrame{
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    //define a set of constants used to define the dimension of the panels
    protected static int FrameWidth=600;
    protected static int FrameHeight=500;
    private static int ButtonWidth=150;
    private static int ButtonHeight=20;
    private static int FrameCenter=(FrameWidth-ButtonWidth)/2;
    private static int FrameRight = 5*FrameWidth/6-ButtonWidth/2;
    private static int FrameLeft = FrameWidth/6-ButtonWidth/2;


    //define a Jfreechart where are going to be plotted the results of our simulations.
    protected static int numsaved = 1;

    //define an attribute of class Simbarra.
    private JPanel panel = new JPanel();

    //we define a method to show a panel
    private void Initialize() {
        setTitle("Simulació barra");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        panel.setLayout(null);
        getContentPane().add(panel);
        setSize(FrameWidth, FrameHeight);
        setResizable(false);
        setVisible(true);

        InitializeMenu();
        Element.InitializeElements();
        InitializeMaterials();
        InitializeSimulation();
    }

    //buttons that are going to be shown in the menu panel
    private JButton exit = new JButton();
    private JButton material = new JButton();
    private JButton simulation = new JButton();
    private JTextPane author = new JTextPane();

    //we define a method to create menu buttons and define their actions when clicked.
    private void InitializeMenu(){
        //define the exit button and the action it performs when clicked (closes the program)
        exit.setText("Sortir");
        PlaceInPosition(exit, 3, 3, true);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        //define material button which allows the user to access constants on different materials
        //JButton material = new JButton();
        material.setText("Constants");
        PlaceInPosition(material, 2, 3, true);
        //when clicked it must hide buttons of the current frame and show a new frame with materials constants
        material.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HideMenu();
                ShowMaterial();
            }
        });

        //define simulation button which allows the user to simulation page
        simulation.setText("Simulació");
        PlaceInPosition(simulation, 1, 3, true);
        simulation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HideMenu();
                ShowSimulation();
            }
        });

        //write the name of me Marco Praderio, developer of this program.
        author.setEditable(false);
        author.setBounds(FrameCenter-ButtonWidth/2, 4*FrameHeight/5, 2*ButtonWidth, 4*ButtonHeight);
        author.setText("Desenvolupat per\nMarco Praderio\nUAB 2017");
        //set font size
        Font font = author.getFont();
        author.setFont(font.deriveFont(20.0f));
        //set alignment
        StyledDocument doc = author.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        panel.add(author);
        author.setVisible(true);

    }

    //elements that are going to be shown in the materials panel
    private String[] string = {"Llautó", "Alumini", "Ferro", "Plom", "Estany", "Plata", "Or"};
    private JComboBox MaterialName = new JComboBox(string);
    private JLabel Emissivity = new JLabel();
    private JLabel EmissivityValue = new JLabel();
    private JLabel Density = new JLabel();
    private JLabel DensityValue = new JLabel();
    private JLabel SpecificHeat = new JLabel();
    private JLabel SpecificHeatValue = new JLabel();
    private JLabel ThermCond1 = new JLabel();
    private JLabel ThermCond1Value = new JLabel();
    private JLabel ThermCond2Value = new JLabel();
    private JButton MaterialBack = new JButton();

    //We define a method to crete all buttons that exist in the materials panel.
    private void InitializeMaterials(){
        //we define position values.
        int distance = ButtonWidth/4;
        int height=FrameHeight/9;

        //We define and place the labels where are going to be written the constants of the objects.
        PlaceLabelInPosition(Emissivity, 2, 7, true, "Coeficient d'emissivitat");
        PlaceLabelInPosition(EmissivityValue, 2, 7, false, Element.Materials.get(0).getEmissivity());
        
        PlaceLabelInPosition(Density,3, 7, true, "Densitat");
        PlaceLabelInPosition(DensityValue, 3,7, false, Element.Materials.get(0).getDensity());

        PlaceLabelInPosition(SpecificHeat,4, 7, true, "Calor especific");
        PlaceLabelInPosition(SpecificHeatValue, 4,7, false, Element.Materials.get(0).getSpecificHeat());

        PlaceLabelInPosition(ThermCond1,5, 7, true, "Conductivitat tèrmica");
        PlaceLabelInPosition(ThermCond1Value, 5,7, false, Element.Materials.get(0).getThermCond1());
        PlaceLabelInPosition(ThermCond2Value, 6,7, false, Element.Materials.get(0).getThermCond2());

        //we define and place a combo box to select the material we want to observe.
        MaterialName.setBounds(distance, height, ButtonWidth, ButtonHeight);
        //add the material button to the existing panel and show it
        panel.add(MaterialName);
        MaterialName.setVisible(false);
        //when changed the combo box should change the constants displaied depending on the selected object.
        MaterialName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = MaterialName.getSelectedIndex();
                EmissivityValue.setText(Element.Materials.get(index).getEmissivity());
                DensityValue.setText(Element.Materials.get(index).getDensity());
                SpecificHeatValue.setText(Element.Materials.get(index).getSpecificHeat());
                ThermCond1Value.setText(Element.Materials.get(index).getThermCond1());
                ThermCond2Value.setText(Element.Materials.get(index).getThermCond2());
            }
        });

        //define the MaterialBack button.
        MaterialBack.setText("Enrere");
        PlaceInPosition(MaterialBack, 7, 7, false);
        MaterialBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HideMaterial();
                ShowMenu();
            }
        });
    }

    //elements that are going to be shown in the simulation panel
    private JButton clear = new JButton("Neteja");
    private JButton plot1 = new JButton("Simular material");
    private JButton analitic = new JButton("Analític");
    private JButton plot = new JButton("Simular");
    private JButton SimulationBack = new JButton("Enrere");
    private JButton Save = new JButton("Guardar");
    private JComboBox ChooseElement = new JComboBox(string);
    static boolean stop = false;

    private String[] string1 = {"Posició fixa", "Temps fix"};
    private JComboBox PosTemps = new JComboBox(string1);
    private NumberFormat posformat;
    private JFormattedTextField posfield = new JFormattedTextField(posformat);

    private JLabel K1label = new JLabel("K (W/mK) =");
    private NumberFormat K1format;
    private JFormattedTextField K1field = new JFormattedTextField(K1format);

    private JLabel T1label = new JLabel(" a T (\u00b0C) =");
    private NumberFormat T1format;
    private JFormattedTextField T1field = new JFormattedTextField(T1format);

    private JLabel K2label = new JLabel("K (W/mK) =");
    private NumberFormat K2format;
    private JFormattedTextField K2field = new JFormattedTextField(K2format);

    private JLabel T2label = new JLabel(" a T (\u00b0C) =");
    private NumberFormat T2format;
    private JFormattedTextField T2field = new JFormattedTextField(T2format);

    private JLabel elabel = new JLabel("e            =");
    private NumberFormat eformat;
    private JFormattedTextField efield = new JFormattedTextField(eformat);

    private JLabel dlabel = new JLabel("rho(g/cc)=");
    private NumberFormat dformat;
    private JFormattedTextField dfield = new JFormattedTextField(dformat);

    private JLabel clabel = new JLabel("c (J/KgK) =");
    private NumberFormat cformat;
    private JFormattedTextField cfield = new JFormattedTextField(cformat);

    private JLabel tlabel = new JLabel("Temps(s)=");
    private NumberFormat tformat;
    private JFormattedTextField tfield = new JFormattedTextField(tformat);

    private JLabel Llabel = new JLabel("L (m)      =");
    private NumberFormat Lformat;
    private JFormattedTextField Lfield = new JFormattedTextField(Lformat);

    private JLabel Tmlabel = new JLabel("Tm (\u00b0C)   =");
    private NumberFormat Tmformat;
    private JFormattedTextField Tmfield = new JFormattedTextField(Tmformat);
    
    private JLabel Amlabel = new JLabel("Am (\u00b0C)   =");
    private NumberFormat Amformat;
    private JFormattedTextField Amfield = new JFormattedTextField(Amformat);

    private JLabel rlabel = new JLabel("r (m)        =");
    private NumberFormat rformat;
    private JFormattedTextField rfield = new JFormattedTextField(rformat);

    private JLabel taulabel = new JLabel("tau (s)    =");
    private NumberFormat tauformat;
    private JFormattedTextField taufield = new JFormattedTextField(tauformat);
    
    private JLabel lambdalabel = new JLabel("lambda  =");
    private NumberFormat lambdaformat;
    private JFormattedTextField lambdafield = new JFormattedTextField(lambdaformat);

    private JLabel Talabel = new JLabel("Ta (\u00b0C)    =");
    private NumberFormat Taformat;
    private JFormattedTextField Tafield = new JFormattedTextField(Taformat);

    private void InitializeSimulation(){
        //define dimension parameters.
        int height = (3*FrameHeight/8-ButtonHeight-9)/3;

        //we place the formatted text fields and labels.
        clabel.setBounds(FrameLeft, FrameHeight-6*height, ButtonWidth, ButtonHeight);
        dlabel.setBounds(FrameCenter, FrameHeight-6*height, ButtonWidth, ButtonHeight);
        elabel.setBounds(FrameRight, FrameHeight-6*height, ButtonWidth, ButtonHeight);
        cfield.setBounds(FrameLeft+ButtonWidth/2, FrameHeight-6*height, ButtonWidth/2, ButtonHeight);
        dfield.setBounds(FrameCenter+ButtonWidth/2, FrameHeight-6*height, ButtonWidth/2, ButtonHeight);
        efield.setBounds(FrameRight+ButtonWidth/2, FrameHeight-6*height, ButtonWidth/2, ButtonHeight);
        panel.add(clabel);  clabel.setVisible(false);
        panel.add(dlabel);  dlabel.setVisible(false);
        panel.add(elabel);  elabel.setVisible(false);
        panel.add(cfield);  cfield.setVisible(false);  cfield.setValue(380);
        panel.add(dfield);  dfield.setVisible(false);  dfield.setValue(8.565);
        panel.add(efield);  efield.setVisible(false);  efield.setValue(0.06);

        K1label.setBounds(FrameLeft+FrameWidth/6, FrameHeight-8*height, ButtonWidth, ButtonHeight);
        T1label.setBounds(FrameCenter+FrameWidth/6, FrameHeight-8*height, ButtonWidth, ButtonHeight);
        K1field.setBounds(FrameLeft+ButtonWidth/2+FrameWidth/6+10, FrameHeight-8*height, ButtonWidth/2, ButtonHeight);
        T1field.setBounds(FrameCenter+ButtonWidth/2+FrameWidth/6, FrameHeight-8*height, ButtonWidth/2, ButtonHeight);
        panel.add(K1label);  K1label.setVisible(false);
        panel.add(T1label);  T1label.setVisible(false);
        panel.add(K1field);  K1field.setVisible(false);  K1field.setValue(159);
        panel.add(T1field);  T1field.setVisible(false);  T1field.setValue(20);

        K2label.setBounds(FrameLeft+FrameWidth/6, FrameHeight-7*height, ButtonWidth, ButtonHeight);
        T2label.setBounds(FrameCenter+FrameWidth/6, FrameHeight-7*height, ButtonWidth, ButtonHeight);
        K2field.setBounds(FrameLeft+ButtonWidth/2+FrameWidth/6+10, FrameHeight-7*height, ButtonWidth/2, ButtonHeight);
        T2field.setBounds(FrameCenter+ButtonWidth/2+FrameWidth/6, FrameHeight-7*height, ButtonWidth/2, ButtonHeight);
        panel.add(K2label);  K2label.setVisible(false);
        panel.add(T2label);  T2label.setVisible(false);
        panel.add(K2field);  K2field.setVisible(false);  K2field.setValue(159);
        panel.add(T2field);  T2field.setVisible(false);  T2field.setValue(90);
        
        Llabel.setBounds(FrameLeft, FrameHeight-4*height, ButtonWidth, ButtonHeight);
        tlabel.setBounds(FrameCenter, FrameHeight-4*height, ButtonWidth, ButtonHeight);
        Lfield.setBounds(FrameLeft+ButtonWidth/2, FrameHeight-4*height, ButtonWidth/2, ButtonHeight);
        tfield.setBounds(FrameCenter+ButtonWidth/2, FrameHeight-4*height, ButtonWidth/2, ButtonHeight);
        panel.add(Llabel);  Llabel.setVisible(false);
        panel.add(tlabel);  tlabel.setVisible(false);
        panel.add(Lfield);  Lfield.setVisible(false);  Lfield.setValue(0.3);
        panel.add(tfield);  tfield.setVisible(false);  tfield.setValue(300);

        Tmlabel.setBounds(FrameLeft, FrameHeight-3*height, ButtonWidth, ButtonHeight);
        Talabel.setBounds(FrameCenter, FrameHeight-3*height, ButtonWidth, ButtonHeight);
        Amlabel.setBounds(FrameRight, FrameHeight-3*height, ButtonWidth, ButtonHeight);
        Tmfield.setBounds(FrameLeft+ButtonWidth/2, FrameHeight-3*height, ButtonWidth/2, ButtonHeight);
        Tafield.setBounds(FrameCenter+ButtonWidth/2, FrameHeight-3*height, ButtonWidth/2, ButtonHeight);
        Amfield.setBounds(FrameRight+ButtonWidth/2, FrameHeight-3*height, ButtonWidth/2, ButtonHeight);
        panel.add(Tmlabel); Tmlabel.setVisible(false);
        panel.add(Talabel); Talabel.setVisible(false);
        panel.add(Amlabel); Amlabel.setVisible(false);
        panel.add(Tmfield); Tmfield.setVisible(false); Tmfield.setValue(90);
        panel.add(Tafield); Tafield.setVisible(false); Tafield.setValue(20.3);
        panel.add(Amfield); Amfield.setVisible(false); Amfield.setValue(10);

        rlabel.setBounds(FrameLeft, FrameHeight-2*height, ButtonWidth, ButtonHeight);
        taulabel.setBounds(FrameCenter, FrameHeight-2*height, ButtonWidth, ButtonHeight);
        lambdalabel.setBounds(FrameRight, FrameHeight-2*height, ButtonWidth, ButtonHeight);
        rfield.setBounds(FrameLeft+ButtonWidth/2, FrameHeight-2*height, ButtonWidth/2, ButtonHeight);
        taufield.setBounds(FrameCenter+ButtonWidth/2, FrameHeight-2*height, ButtonWidth/2, ButtonHeight);
        lambdafield.setBounds(FrameRight+ButtonWidth/2, FrameHeight-2*height, ButtonWidth/2, ButtonHeight);
        panel.add(rlabel); rlabel.setVisible(false);
        panel.add(taulabel); taulabel.setVisible(false);
        panel.add(lambdalabel); lambdalabel.setVisible(false);
        panel.add(rfield); rfield.setVisible(false); rfield.setValue(0.1);
        panel.add(taufield); taufield.setVisible(false); taufield.setValue(400);
        panel.add(lambdafield); lambdafield.setVisible(false); lambdafield.setValue(10.8);
        
        //we place a combo box that will allow us to select the material of the barr.
        ChooseElement.setBounds(FrameCenter, FrameHeight-5*height, ButtonWidth, ButtonHeight);
        panel.add(ChooseElement);
        ChooseElement.setVisible(false);
        ChooseElement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = ChooseElement.getSelectedIndex();
                K1field.setValue(Element.Materials.get(i).ThermCond1[1]);
                T1field.setValue(Element.Materials.get(i).ThermCond1[0]);
                K2field.setValue(Element.Materials.get(i).ThermCond2[1]);
                T2field.setValue(Element.Materials.get(i).ThermCond2[0]);
                cfield.setValue(Element.Materials.get(i).specificHeat);
                dfield.setValue(Element.Materials.get(i).density);
                efield.setValue(Element.Materials.get(i).emissivity);
            }
        });

        //we place the button that will allow us to clear the plots made.
        clear.setBounds(FrameCenter, FrameHeight-9*height, ButtonWidth, ButtonHeight);
        panel.add(clear);
        clear.setVisible(false);
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Plot.clean();
                PlotFix.clean();
                Analitic.clean();
                AnaliticFix.clean();
            }
        });

        //we place the button that will allow us to draw the simulation results.
        plot.setBounds(FrameRight, FrameHeight-4*height, ButtonWidth, ButtonHeight);
        panel.add(plot);
        plot.setVisible(false);
        plot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double Ta = Double.parseDouble(Tafield.getText());
                    double Tm = Double.parseDouble(Tmfield.getText());
                    double r = Double.parseDouble(rfield.getText());
                    double lambda = Double.parseDouble(lambdafield.getText());
                    double A = Double.parseDouble(Amfield.getText());
                    double period = Double.parseDouble(taufield.getText());
                    double L = Double.parseDouble(Lfield.getText());
                    double time = Double.parseDouble(tfield.getText());
                    if (stop){
                        PlotFix.start(Element.Materials.get(ChooseElement.getSelectedIndex()), Ta, Tm, r, lambda, A, period, L);
                    } else{
                        double x = Math.min(Double.parseDouble(posfield.getText()), L); posfield.setValue(x);
                        Plot.start(Element.Materials.get(ChooseElement.getSelectedIndex()), Ta, Tm, r, lambda, A, period, x, L, time);
                    }
                } catch (Exception ex){ System.out.println("Introdueix números");}
            }
        });

        //we place the button that will allow us to draw the analitic results.
        analitic.setBounds(FrameRight, FrameHeight-5*height, ButtonWidth, ButtonHeight);
        panel.add(analitic);
        analitic.setVisible(false);
        analitic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double Ta = Double.parseDouble(Tafield.getText());
                    double Tm = Double.parseDouble(Tmfield.getText());
                    double r = Double.parseDouble(rfield.getText());
                    double lambda = Double.parseDouble(lambdafield.getText());
                    double A = Double.parseDouble(Amfield.getText());
                    double period = Double.parseDouble(taufield.getText());
                    double L = Double.parseDouble(Lfield.getText());
                    double time = Double.parseDouble(tfield.getText());
                    if (stop){
                        AnaliticFix.start(Element.Materials.get(ChooseElement.getSelectedIndex()), Ta, Tm, r, lambda, A, period, L);
                    } else{
                        double x = Math.min(Double.parseDouble(posfield.getText()), L); posfield.setValue(x);
                        Analitic.start(Element.Materials.get(ChooseElement.getSelectedIndex()), Ta, Tm, r, lambda, A, period, x, L, time);
                    }
                } catch (Exception ex){ System.out.println("Introdueix números");}
            }
        });

        //we place the button that will allow us to draw the simulation results of a new material.
        plot1.setBounds(FrameLeft, FrameHeight-5*height, ButtonWidth, ButtonHeight);
        panel.add(plot1);
        plot1.setVisible(false);
        plot1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Element element = new Element();
                    element.setName("Nou Material");
                    //element.setTagName("NM");
                    element.setEmissivity(Double.parseDouble(efield.getText()));
                    element.setDensity(Double.parseDouble(dfield.getText()));
                    element.setSpecificHeat(Double.parseDouble(cfield.getText()));
                    element.setThermCond1(Double.parseDouble(T1field.getText()), Double.parseDouble(K1field.getText()));
                    element.setThermCond2(Double.parseDouble(T2field.getText()), Double.parseDouble(K2field.getText()));
                    element.setInterceptSlope();
                    double Ta = Double.parseDouble(Tafield.getText());
                    double Tm = Double.parseDouble(Tmfield.getText());
                    double r = Double.parseDouble(rfield.getText());
                    double lambda = Double.parseDouble(lambdafield.getText());
                    double A = Double.parseDouble(Amfield.getText());
                    double period = Double.parseDouble(taufield.getText());
                    double L = Double.parseDouble(Lfield.getText());
                    double time = Double.parseDouble(tfield.getText());
                    if (stop){
                        PlotFix.start(element, Ta, Tm, r, lambda, A, period, L);
                    } else{
                        double x = Math.min(Double.parseDouble(posfield.getText()), L);
                        posfield.setValue(x);
                        Plot.start(element, Ta, Tm, r, lambda, A, period, x, L, time);
                    }
                } catch (Exception ex){ System.out.println("Introdueix números");}
            }
        });

        //we place the combo box that will allow us to chose between different simulation options.
        PosTemps.setBounds(FrameLeft, FrameHeight-height-10, 3*ButtonWidth/4-5, ButtonHeight);
        panel.add(PosTemps);
        PosTemps.setVisible(false);
        PosTemps.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop = PosTemps.getSelectedIndex()==1;
                posfield.setVisible(!stop);
                tfield.setVisible(!stop);
                tlabel.setVisible(!stop);
            }
        });
        posfield.setBounds(FrameLeft+3*ButtonWidth/4, FrameHeight-height-10, ButtonWidth/4, ButtonHeight);
        panel.add(posfield);
        posfield.setVisible(false);
        posfield.setValue(0.15);

        //we place the button that will allow us to save the plots displayed.
        Save.setBounds(FrameCenter, FrameHeight-height-10, ButtonWidth, ButtonHeight);
        panel.add(Save);
        Save.setVisible(false);
        Save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (stop){ PlotFix.save(); AnaliticFix.save(); }
                else{ Plot.save(); Analitic.save(); }
            }
        });

        //we place the button that will allow us to return to the menu section.
        SimulationBack.setBounds(FrameRight, FrameHeight-height-10, ButtonWidth, ButtonHeight);
        //add the material SimulationBack to the existing panel and show it
        panel.add(SimulationBack);
        SimulationBack.setVisible(false);
        SimulationBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HideSimulation();
                ShowMenu();
            }
        });

    }

    //places a button in a centered position of the panel.
    private void PlaceInPosition(JButton button, int pos, int total, boolean Visible) {
        int height=FrameHeight*pos/(total+2);
        button.setBounds(FrameCenter, height, ButtonWidth, ButtonHeight);
        //add the material button to the existing panel and show it
        panel.add(button);
        button.setVisible(Visible);
    }

    //places a label in a left or right position of the panel.
    private void PlaceLabelInPosition(JLabel label, int pos, int total, boolean left, String text) {
        //we define position values.
        int distance = ButtonWidth/4;
        int height=pos*FrameHeight/(total+2);

        label.setText(text);
        if (left){
            label.setBounds(distance, height, 2*ButtonWidth, ButtonHeight);
        }
        else{
            label.setBounds(FrameWidth-(distance+5*ButtonWidth/4), height, 2*ButtonWidth, ButtonHeight);
        }
        panel.add(label);
        label.setVisible(false);
    }

    //this method hides the menu buttons
    private void HideMenu(){
        exit.setVisible(false);
        material.setVisible(false);
        simulation.setVisible(false);
        author.setVisible(false);
    }

    //this method shows the menu buttons
    private void ShowMenu(){
        exit.setVisible(true);
        material.setVisible(true);
        simulation.setVisible(true);
        author.setVisible(true);
    }

    //this method shows the material's frame elements
    private void ShowMaterial(){
        MaterialName.setVisible(true);
        Emissivity.setVisible(true);
        EmissivityValue.setVisible(true);
        Density.setVisible(true);
        DensityValue.setVisible(true);
        SpecificHeat.setVisible(true);
        SpecificHeatValue.setVisible(true);
        ThermCond1.setVisible(true);
        ThermCond1Value.setVisible(true);
        ThermCond2Value.setVisible(true);
        MaterialBack.setVisible(true);
    }

    //this method hides the material's frame elements
    private void HideMaterial(){
        MaterialName.setVisible(false);
        Emissivity.setVisible(false);
        EmissivityValue.setVisible(false);
        Density.setVisible(false);
        DensityValue.setVisible(false);
        SpecificHeat.setVisible(false);
        SpecificHeatValue.setVisible(false);
        ThermCond1.setVisible(false);
        ThermCond1Value.setVisible(false);
        ThermCond2Value.setVisible(false);
        MaterialBack.setVisible(false);
    }

    //this method show's the material's frame elements.
    private void ShowSimulation(){
        clear.setVisible(true);
        K1field.setVisible(true);
        K1label.setVisible(true);
        T1field.setVisible(true);
        T1label.setVisible(true);
        K2field.setVisible(true);
        K2label.setVisible(true);
        T2field.setVisible(true);
        T2label.setVisible(true);
        efield.setVisible(true);
        elabel.setVisible(true);
        dfield.setVisible(true);
        dlabel.setVisible(true);
        cfield.setVisible(true);
        clabel.setVisible(true);
        plot1.setVisible(true);
        analitic.setVisible(true);
        plot.setVisible(true);
        ChooseElement.setVisible(true);
        SimulationBack.setVisible(true);
        Save.setVisible(true);
        Lfield.setVisible(true);
        Llabel.setVisible(true);
        tfield.setVisible(!stop);
        tlabel.setVisible(!stop);
        PosTemps.setVisible(true);
        posfield.setVisible(!stop);
        Tmlabel.setVisible(true);
        Tmfield.setVisible(true);
        Amlabel.setVisible(true);
        Amfield.setVisible(true);
        rlabel.setVisible(true);
        rfield.setVisible(true);
        taulabel.setVisible(true);
        taufield.setVisible(true);
        lambdalabel.setVisible(true);
        lambdafield.setVisible(true);
        Talabel.setVisible(true);
        Tafield.setVisible(true);
    }

    //this method hide's the material's frame elements
    private void HideSimulation(){
        clear.setVisible(false);
        K1field.setVisible(false);
        K1label.setVisible(false);
        T1field.setVisible(false);
        T1label.setVisible(false);
        K2field.setVisible(false);
        K2label.setVisible(false);
        T2field.setVisible(false);
        T2label.setVisible(false);
        efield.setVisible(false);
        elabel.setVisible(false);
        dfield.setVisible(false);
        dlabel.setVisible(false);
        cfield.setVisible(false);
        clabel.setVisible(false);
        plot1.setVisible(false);
        analitic.setVisible(false);
        plot.setVisible(false);
        ChooseElement.setVisible(false);
        SimulationBack.setVisible(false);
        Save.setVisible(false);
        Lfield.setVisible(false);
        Llabel.setVisible(false);
        tfield.setVisible(false);
        tlabel.setVisible(false);
        PosTemps.setVisible(false);
        posfield.setVisible(false);
        Tmlabel.setVisible(false);
        Tmfield.setVisible(false);
        Amlabel.setVisible(false);
        Amfield.setVisible(false);
        rlabel.setVisible(false);
        rfield.setVisible(false);
        taulabel.setVisible(false);
        taufield.setVisible(false);
        lambdalabel.setVisible(false);
        lambdafield.setVisible(false);
        Talabel.setVisible(false);
        Tafield.setVisible(false);
    }

    public static void main(String[] args) {
        //create the Simbarra object.
        SimBarra menu = new SimBarra();
        menu.Initialize();
    }
}
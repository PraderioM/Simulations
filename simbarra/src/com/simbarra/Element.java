package com.simbarra;

import java.util.ArrayList;

public class Element{
    //define some attributes
    private String Name;
    //private String TagName;
    protected double emissivity;
    protected double density;
    protected double specificHeat;
    protected double[] ThermCond1 = new double[2];
    protected double[] ThermCond2 = new double[2];
    private double slope;
    private double intercept;

    //array containing all the materials we are going to be able to simulate
    protected static ArrayList<Element> Materials = new ArrayList<Element>();
    //we define a method to define all the materials constants.
    protected static void InitializeElements() {
        Materials.add(new Element());
        Materials.get(0).setName("Llauto");
        //Materials.get(0).setTagName("Llau");
        Materials.get(0).setEmissivity(0.06);
        Materials.get(0).setDensity(8.565);
        Materials.get(0).setSpecificHeat(380);
        Materials.get(0).setThermCond1(20, 159);
        Materials.get(0).setThermCond2(90, 159);
        Materials.get(0).setInterceptSlope();
        Materials.add(new Element());
        Materials.get(1).setName("Al·lumini");
        //Materials.get(1).setTagName("Al");
        Materials.get(1).setEmissivity(0.09);
        Materials.get(1).setDensity(2.7);
        Materials.get(1).setSpecificHeat(908);
        Materials.get(1).setThermCond1(20, 204);
        Materials.get(1).setThermCond2(93, 215);
        Materials.get(1).setInterceptSlope();
        Materials.add(new Element());
        Materials.get(2).setName("Ferro");
        //Materials.get(2).setTagName("Fe");
        Materials.get(2).setEmissivity(0.26);
        Materials.get(2).setDensity(7.9);
        Materials.get(2).setSpecificHeat(460);
        Materials.get(2).setThermCond1(20, 73);
        Materials.get(2).setThermCond2(300, 55);
        Materials.get(2).setInterceptSlope();
        Materials.add(new Element());
        Materials.get(3).setName("Plom");
        //Materials.get(3).setTagName("Pb");
        Materials.get(3).setEmissivity(0.066);
        Materials.get(3).setDensity(11.3);
        Materials.get(3).setSpecificHeat(127);
        Materials.get(3).setThermCond1(20, 35);
        Materials.get(3).setThermCond2(300, 30);
        Materials.get(3).setInterceptSlope();
        Materials.add(new Element());
        Materials.get(4).setName("Estany");
        //Materials.get(4).setTagName("Sn");
        Materials.get(4).setEmissivity(0.04);
        Materials.get(4).setDensity(7.3);
        Materials.get(4).setSpecificHeat(225);
        Materials.get(4).setThermCond1(0,65);
        Materials.get(4).setThermCond2(90, 63);
        Materials.get(4).setInterceptSlope();
        Materials.add(new Element());
        Materials.get(5).setName("Plata");
        //Materials.get(5).setTagName("Ag");
        Materials.get(5).setEmissivity(0.025);
        Materials.get(5).setDensity(10.5);
        Materials.get(5).setSpecificHeat(234);
        Materials.get(5).setThermCond1(20, 407);
        Materials.get(5).setThermCond2(90, 414);
        Materials.get(5).setInterceptSlope();
        Materials.add(new Element());
        Materials.get(6).setName("Or");
        //Materials.get(6).setTagName("Au");
        Materials.get(6).setEmissivity(0.025);
        Materials.get(6).setDensity(19.3);
        Materials.get(6).setSpecificHeat(128);
        Materials.get(6).setThermCond1(20,315);
        Materials.get(6).setThermCond2(90, 300);
        Materials.get(6).setInterceptSlope();
    }

    //define methods to assign values to those attributes.
    protected void setName(String name){ Name = name; }

    //protected void setTagName(String tagname){ TagName = tagname; }

    protected void setEmissivity(double x){ emissivity = x; }

    protected void setDensity(double x){ density = x; }

    protected void setSpecificHeat(double x){ specificHeat = x; }

    protected void setThermCond1(double Temp, double ThermCond) {
        ThermCond1[0] = Temp;
        ThermCond1[1] = ThermCond;
    }

    protected void setThermCond2(double Temp, double ThermCond) {
        ThermCond2[0] = Temp;
        ThermCond2[1] = ThermCond;
    }

    protected void setInterceptSlope() {
        this.slope = (ThermCond1[1] - ThermCond2[1]) / (ThermCond1[0] - ThermCond2[0]);
        this.intercept = ThermCond1[1] - ThermCond1[0] * slope;
    }

    //we define a method to return the name of the material.
    public String getName(){ return Name;}

    //we define a method to return the tagname of the material.
    //public String getTagName(){ return TagName;}

    //we define a method to return the emissivity coefficient of the material.
    public String getEmissivity(){ return String.format("%.3f ", emissivity); }

    //we define a method to return the density of the material.
    public String getDensity(){
        return String.format("%.3f g/cc", density);
    }

    //we define a method to return the value of the specific heat of this material
    public String getSpecificHeat(){
        return String.format("%.0f J/KgK", specificHeat);
    }

    //we define a method to return a value of thermal conductivity at a given temperature.
    public String getThermCond1(){
        return String.format("%.0f W/mK a temperatura %.0f \u00b0C", ThermCond1[1], ThermCond1[0]);
    }

    //We define a method to return the another value of thermal conductivity at a given temperature.
    public String getThermCond2(){
        return String.format("%.0f W/mK a temperatura %.0f \u00b0C", ThermCond2[1], ThermCond2[0]);
    }

    //We define a method to get the thermal conductivity at a given temperature.
    protected double getThermCond(double temp){ return intercept+slope*temp; }
}

import org.jfree.data.xy.XYSeries;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by marco on 17/02/17.
 */
public class Settings extends JFrame {
    //define a set of constants used to define the dimension of the panels
    private static int FrameWidth=600;
    private static int FrameHeight=500;
    private static int ButtonWidth=150;
    private static int ButtonHeight=20;
    private static int FrameCenter=(FrameWidth-ButtonWidth)/2;

    //declare the panel
    private JPanel panel = new JPanel();

    //declare the objects that are going to be shown in the menu page.
    
    //Speed settings
    private JLabel DoorLabel = new JLabel("Goal parameters");
    
    private JLabel DoorXLabel = new JLabel("x (m)=");//x position
    private NumberFormat DoorXFormat;
    private JFormattedTextField DoorXField = new JFormattedTextField(DoorXFormat);

    private JLabel DoorYLabel = new JLabel("y (m)=");//y position
    private NumberFormat DoorYFormat;
    private JFormattedTextField DoorYField = new JFormattedTextField(DoorYFormat);

    private JLabel DoorHLabel = new JLabel("height (m)");//Door height
    private NumberFormat DoorHFormat;
    private JFormattedTextField DoorHField = new JFormattedTextField(DoorHFormat);

    private JLabel DoorWLabel = new JLabel("width (m)=");//Door width
    private NumberFormat DoorWFormat;
    private JFormattedTextField DoorWField = new JFormattedTextField(DoorWFormat);

    //barrier settings
    private JLabel BarrierLabel = new JLabel("Barrier parameters");

    private JLabel BarrierXLabel = new JLabel("x (m)=");//x position
    private NumberFormat BarrierXFormat;
    private JFormattedTextField BarrierXField = new JFormattedTextField(BarrierXFormat);

    private JLabel BarrierYLabel = new JLabel("y (m)=");//y position
    private NumberFormat BarrierYFormat;
    private JFormattedTextField BarrierYField = new JFormattedTextField(BarrierYFormat);

    private JLabel BarrierHLabel = new JLabel("height (m)");//Barrier height
    private NumberFormat BarrierHFormat;
    private JFormattedTextField BarrierHField = new JFormattedTextField(BarrierHFormat);

    private JLabel BarrierWLabel = new JLabel("width (m)=");//Barrier width
    private NumberFormat BarrierWFormat;
    private JFormattedTextField BarrierWField = new JFormattedTextField(BarrierWFormat);
    
    //Initial speed settings
    private JLabel SpeedLabel = new JLabel("Initial speed");

    private JLabel SpeedXLabel = new JLabel("x (m/s)=");//x speed
    private NumberFormat SpeedXFormat;
    private JFormattedTextField SpeedXField = new JFormattedTextField(SpeedXFormat);

    private JLabel SpeedYLabel = new JLabel("y (m/s)=");//y speed
    private NumberFormat SpeedYFormat;
    private JFormattedTextField SpeedYField = new JFormattedTextField(SpeedYFormat);

    private JLabel SpeedZLabel = new JLabel("z (m/s)=");//z speed
    private NumberFormat SpeedZFormat;
    private JFormattedTextField SpeedZField = new JFormattedTextField(SpeedZFormat);

    //Rotation settings
    private JLabel RotationLabel = new JLabel("Rotation vector");

    private JLabel RotationXLabel = new JLabel("x (rad/s)=");//x Rotation
    private NumberFormat RotationXFormat;
    private JFormattedTextField RotationXField = new JFormattedTextField(RotationXFormat);

    private JLabel RotationYLabel = new JLabel("y (rad/s)=");//y Rotation
    private NumberFormat RotationYFormat;
    private JFormattedTextField RotationYField = new JFormattedTextField(RotationYFormat);

    private JLabel RotationZLabel = new JLabel("z (rad/s)=");//z Rotation
    private NumberFormat RotationZFormat;
    private JFormattedTextField RotationZField = new JFormattedTextField(RotationZFormat);

    //other variables
    private JLabel VariousLabel = new JLabel("Various parameters");

    private JLabel MassLabel = new JLabel("mass (g)=");//ball mass
    private NumberFormat MassFormat;
    private JFormattedTextField MassField = new JFormattedTextField(MassFormat);

    private JLabel RadiusLabel = new JLabel("radi(cm)=");//ball radius
    private NumberFormat RadiusFormat;
    private JFormattedTextField RadiusField = new JFormattedTextField(RadiusFormat);

    private JLabel DensityLabel = new JLabel("d (g/dm³)=");//Air density
    private NumberFormat DensityFormat;
    private JFormattedTextField DensityField = new JFormattedTextField(DensityFormat);

    private JLabel DragLabel = new JLabel("drag =");//Drag coefficient
    private NumberFormat DragFormat;
    private JFormattedTextField DragField = new JFormattedTextField(DragFormat);

    private JButton RunButton = new JButton("Run");
    
    public Settings() {
        //set frame parameters
        setTitle("Simulation parameters");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //set panel parameters
        panel.setLayout(null);
        getContentPane().add(panel);
        setSize(FrameWidth, FrameHeight);
        setResizable(false);
        setVisible(true);

        //place objects in panel
        PlaceAllObjects();
        
        RunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //we copy all the inserted parameters.
                double[] Do = new double[4];
                Do[0] = Double.parseDouble(DoorXField.getText());
                Do[1] = Double.parseDouble(DoorYField.getText());
                Do[2] = Double.parseDouble(DoorHField.getText());
                Do[3] = Double.parseDouble(DoorWField.getText());

                double[] B = new double[4];
                B[0] = Double.parseDouble(BarrierXField.getText());
                B[1] = Double.parseDouble(BarrierYField.getText());
                B[2] = Double.parseDouble(BarrierHField.getText());
                B[3] = Double.parseDouble(BarrierWField.getText());

                double Sx = Double.parseDouble(SpeedXField.getText());
                double Sy = Double.parseDouble(SpeedYField.getText());
                double Sz = Double.parseDouble(SpeedZField.getText());

                double[] R = new double[3];
                R[0] = Double.parseDouble(RotationXField.getText());
                R[1] = Double.parseDouble(RotationYField.getText());
                R[2] = Double.parseDouble(RotationZField.getText());

                double m = Double.parseDouble(MassField.getText())/1000;
                double r = Double.parseDouble(RadiusField.getText())/100;
                double D = Double.parseDouble(DragField.getText());
                double d = Double.parseDouble(DensityField.getText());

                //Solve the differential equation.
                ArrayList vect = new ArrayList<Double>();
                SolveEdos(Do[1], Sx, Sy, Sz, R, m, r, D, d, vect);

                //show the solution.
                //store the data in an XYSeries object.
                int i;
                XYSeries H = new XYSeries("", false), V=new XYSeries("", false), L= new XYSeries("", false);
                for (i=0; i<vect.size()/3; i++){
                    H.add((double) vect.get(3*i), (double) vect.get(3*i+1));
                    V.add((double) vect.get(3*i), (double) vect.get(3*i+2));
                    /*H.add((double) vect.get(3*i+1), (double) vect.get(3*i));
                    V.add((double) vect.get(3*i+2), (double) vect.get(3*i));*/
                    L.add((double) vect.get(3*i+1), (double) vect.get(3*i+2));
                }
                new PlotH(H); //plot the horizontal projection.
                new PlotV(V); //plot the vertical projection.
                new PlotL(L); //plot the lateral projection.

                //draw an animation representing the fault kick
                JFrame frame = new JFrame("fault kick");
                frame.setContentPane(new BallMove(vect, Do, B, r));
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    //this method solves numerically the necessary differential equation
    private void SolveEdos(double Dy, double Sx, double Sy, double Sz, double[] R, double m, double r, double D, double d, ArrayList vect){
        int i = 0;
        double x = 0, y = 0, z = 0, a, b, c;
        a = 3.141592653*3.141592653*r*r*r*d;
        b = 9.81*m;
        c = D*d*3.141592653*r*r/2;
        double[] V;
        while (i<10000){
            if (y>Dy || z<0){i = 9999;}
            i++;
            vect.add(x); vect.add(y); vect.add(z);
            V=RK4(x, y, z, Sx, Sy, Sz, R, a, b, c);
            x += V[0]; y += V[1]; z += V[2]; Sx += V[3]; Sy += V[4]; Sz += V[5];
        }
    }

    private double[] RK4(double x, double y, double z, double Sx, double Sy, double Sz, double[] R, double a, double b, double c){
        double step = 1./1000;
        int i;
        double[] sol = new double[6];
        double[] aux = new double[6];
        double[] aux1;
        aux[0] = x; aux[1] = y; aux[2] = z; aux[3] = Sx; aux[4] = Sy; aux[5] = Sz;

        aux1 = VectorField(aux, R, a, b, c);//first RK4 term.
        for (i=0; i<6; i++){
            sol[i] = aux1[i];
            aux1[i] = aux[i]+step*aux1[i]/2;
        }

        aux1 = VectorField(aux1, R, a, b, c);//second RK4 term
        for (i=0; i<6; i++){
            sol[i] += 2*aux1[i];
            aux1[i] = aux[i]+step*aux1[i]/2;
        }

        aux1 = VectorField(aux1, R, a, b, c);//third RK4 term
        for (i=0; i<6; i++){
            sol[i] += 2*aux1[i];
            aux1[i] = aux[i]+step*aux1[i];
        }

        aux1 = VectorField(aux1, R, a, b, c);//fourth RK4 term
        for (i=0; i<6; i++){
            sol[i] += aux1[i];
            sol[i] *= step/6; //compute de final difference calculated.
        }

        return sol;
    }

    private double[] VectorField(double[] vect, double[] R, double a, double b, double c){
        double[] sol = new double [6];
        double mod;
        mod = Math.sqrt(vect[3]*vect[3]+vect[4]*vect[4]+vect[5]*vect[5]);//we compute the modulus of speed;
        sol[0] = vect[3]; sol[1] = vect[4]; sol [2] = vect[5]; //the derivative of the position is the speed.
        //we use the diff equation dv/dt=pi²r³d wXv-b(0,0,1) -cv|v| in order to calculate the derivative of the speed
        sol[3] = a*(R[1]*vect[5]-R[2]*vect[4])-c*vect[3]*mod;
        sol[4] = a*(R[2]*vect[3]-R[0]*vect[5])-c*vect[4]*mod;
        sol[5] = a*(R[0]*vect[4]-R[1]*vect[3])-b-c*vect[5]*mod;
        return sol;
    }

    //This method's purpose is to place all the objects needed in the panell
    private void PlaceAllObjects(){
        PlaceLabelInPosition(DoorLabel, 13, 1);//door parameter objects
        PlaceLabelTextInPosition(DoorXLabel, DoorXField, 1, 2, 2, 13, 0);
        PlaceLabelTextInPosition(DoorYLabel, DoorYField, 2, 2, 2, 13, 25);
        PlaceLabelTextInPosition(DoorHLabel, DoorHField, 1, 2, 3, 13, 2.44);
        PlaceLabelTextInPosition(DoorWLabel, DoorWField, 2, 2, 3, 13, 7.32);

        PlaceLabelInPosition(BarrierLabel, 13, 4);//barrier parameter objects
        PlaceLabelTextInPosition(BarrierXLabel, BarrierXField, 1, 2, 5, 13, 0);
        PlaceLabelTextInPosition(BarrierYLabel, BarrierYField, 2, 2, 5, 13, 9.15);
        PlaceLabelTextInPosition(BarrierHLabel, BarrierHField, 1, 2, 6, 13, 2);
        PlaceLabelTextInPosition(BarrierWLabel, BarrierWField, 2, 2, 6, 13, 3);

        PlaceLabelInPosition(SpeedLabel, 13, 7);//Initial speed parameter objects
        PlaceLabelTextInPosition(SpeedXLabel, SpeedXField, 1, 3, 8, 13, 2);
        PlaceLabelTextInPosition(SpeedYLabel, SpeedYField, 2, 3, 8, 13, 8.5);
        PlaceLabelTextInPosition(SpeedZLabel, SpeedZField, 3, 3, 8, 13, 8);

        PlaceLabelInPosition(RotationLabel, 13, 9);//Rotation parameter objects
        PlaceLabelTextInPosition(RotationXLabel, RotationXField, 1, 3, 10, 13, 0);
        PlaceLabelTextInPosition(RotationYLabel, RotationYField, 2, 3, 10, 13, 0);
        PlaceLabelTextInPosition(RotationZLabel, RotationZField, 3, 3, 10, 13, 10);

        PlaceLabelInPosition(VariousLabel, 13, 11);//various parameters objects
        PlaceLabelTextInPosition(MassLabel, MassField, 1, 3, 12, 13, 430);
        PlaceLabelTextInPosition(RadiusLabel, RadiusField, 2, 3, 12, 13, 10.98);
        PlaceLabelTextInPosition(DragLabel, DragField, 3, 3, 12, 13, 0.47);

        PlaceLabelTextInPosition(DensityLabel, DensityField, 1, 2, 13, 13, 1.2041);
        PlaceButtonInPosition(RunButton, 2, 2, 13, 13);
        //We refresh the page.
        panel.revalidate();
        panel.repaint();
    }

    //places a button at a specified position of the panel.
    private void PlaceButtonInPosition(JButton button, int Xpos, int Xtot, int Ypos, int Ytot) {
        button.setBounds((2*Xpos-1)*FrameWidth/(2*Xtot)-ButtonWidth/2, FrameHeight*Ypos/(Ytot+2), ButtonWidth, ButtonHeight);
        //add the material button to the existing panel and show it
        panel.add(button);
        button.setVisible(true);
    }

    //places a label in a centered position of the panel.
    private void PlaceLabelInPosition(JLabel label, int Ytot, int Ypos){
        label.setBounds(FrameCenter, FrameHeight*Ypos/(Ytot+2), 2*ButtonWidth, ButtonHeight);
        panel.add(label);
        label.setVisible(true);
    }

    //places a Label and a formatted text at a given position
    private void PlaceLabelTextInPosition(JLabel label, JFormattedTextField field, int Xpos, int Xtot, int Ypos, int Ytot, double value) {
        label.setBounds((2*Xpos-1)*FrameWidth/(2*Xtot)-ButtonWidth/2, FrameHeight*Ypos/(Ytot+2), ButtonWidth, ButtonHeight);
        field.setBounds((2*Xpos-1)*FrameWidth/(2*Xtot), FrameHeight*Ypos/(Ytot+2), ButtonWidth / 2, ButtonHeight);
        panel.add(label);
        panel.add(field);
        field.setValue(value);
        label.setVisible(true);
        field.setVisible(true);
    }

    public static void main(String[] args) {
        //create the Settings object.
        new Settings();
    }
}

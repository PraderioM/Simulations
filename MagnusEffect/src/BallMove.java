import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by marco on 17/02/17.
 */
public class BallMove extends JPanel{
    // Container box's width and height
    private static final int BOX_WIDTH = 360;
    private static final int BOX_HEIGHT = 270;

    // Ball's properties
    private double ballRadius; // Ball's radius

    private double Dx; //goal's position x and y
    private double Dy;
    private double Dh; //goal's dimensions height and width.
    private double Dw;
    private double Dxp;
    private double Dyp1;
    private double Dyp2;
    private int Dxv;
    private int Dyv;
    private int Dhv;
    private int Dwv;
    private int Dxh;
    private int Dyh;
    private int Dhh;
    private int Dwh;
    private int Dxl;
    private int Dyl;
    private int Dhl;
    private int Dwl;

    private double Bx; //barrier's position x and y
    private double By;
    private double Bh; //barrier's dimensions height and width.
    private double Bw;
    private double Bxp;
    private double Byp1;
    private double Byp2;
    private int Bxv;
    private int Byv;
    private int Bhv;
    private int Bwv;
    private int Bxh;
    private int Byh;
    private int Bhh;
    private int Bwh;
    private int Bxl;
    private int Byl;
    private int Bhl;
    private int Bwl;

    private double theta = Math.PI/4; //projected plane inclination
    private double h = 3.; //observer's height.
    private double b = -Math.sin(theta); //projected plane coefficients
    private double c = Math.cos(theta);

    private double[] pos; //position in euclidean space.
    private double[] projPos; //position in projeted plane.
    private boolean goal = false; //goal or no goal.
    private java.lang.String s; //writes goal or no goal.
    private Color color;
    private java.lang.String stringH = "horizontal plane";
    private java.lang.String stringV = "vertical plane";
    private java.lang.String stringL = "lateral plane";
    private int T = 0;
    private boolean first = false;

    private static final int UPDATE_RATE = 30; // Number of refresh per second

    /** Constructor to create the UI components and init game objects. */
    public BallMove(ArrayList<Double> vect, double[] D, double[] B, double r) {
        setPreferredSize(new Dimension(2*BOX_WIDTH+2, 2*BOX_HEIGHT+2)); //set dimensions
        int n, N;
        N=vect.size();

        //we load the parameters regarding the door, the barrier and the balls radius and teel if there was a goal or not.
        Dx = D[0]; Dy = D[1]; Dh = D[2]; Dw = D[3]; //door parameters.
        //Barrier projected parameters.
        /*Byp2= -h/(b*By+c*(Bh-h));
        Bxp = c*Byp2*Bw/2; //x
        Byp1= Byp2*By; //y1
        Byp2= Byp1 + h*By/(b*By-c*h); //y1-y2*/
        //Goal parameters in vertical projection
        Dxv = (int) (BOX_WIDTH*((Dx-Dw/2)/15 + 1)/2);
        Dyv = BOX_HEIGHT/2 - (int) Dh*BOX_WIDTH/30;
        Dwv = (int) Dw*BOX_WIDTH/30;
        Dhv = (int) Dh*BOX_WIDTH/30;
        //Goal parameters in horizontal projection.
        Dxh = (int) ((Dx-Dw/2)*BOX_HEIGHT/(Dy+2)) + 3*BOX_WIDTH/2 + 2;
        Dyh = BOX_HEIGHT-(int) (BOX_HEIGHT*Dy/(Dy+2));
        Dwh = (int) (Dw*BOX_HEIGHT/(Dy+2));
        //Goal parameters in lateral projection
        Dxl = (int) (BOX_WIDTH*Dy/(Dy+2));
        Dyl = 3*BOX_HEIGHT/2 + 2 -(int) (BOX_WIDTH*Dh/(Dy+2));
        Dhl = (int) (BOX_WIDTH*Dh/(Dy+2));

        Bx = B[0]; By = B[1]; Bh = B[2]; Bw = B[3]; //barrier parameters.
        //Door Projected parameters.
        /*Dyp2= -h/(b*Dy+c*(Dh-h));
        Dxp = c*Dyp2*Dw/2; //x
        Dyp1= Dyp2*Dy; //y1
        Dyp2= Dyp1 + h*Dy/(b*Dy-c*h); //y1-y2*/
        //Barrier parameters in vertical projection
        Bxv = (int) (BOX_WIDTH*((Bx-Bw/2)/15+1)/2);
        Byv = BOX_HEIGHT/2-(int) Bh*BOX_WIDTH/30;
        Bwv = (int) Bw*BOX_WIDTH/30;
        Bhv = (int) Bh*BOX_WIDTH/30;
        //Barrier parameters in horizontal projection.
        Bxh = (int) ((Bx-Bw/2)*BOX_HEIGHT/(Dy+2)) + 3*BOX_WIDTH/2 + 2;
        Byh = BOX_HEIGHT-(int) (By*BOX_HEIGHT/(Dy+2));
        Bwh = (int) (Bw*BOX_HEIGHT/(Dy+2));
        //Barrier parameters in lateral projection
        Bxl = (int) (By*BOX_WIDTH/(Dy+2));
        Byl = 3*BOX_HEIGHT/2 + 2- (int) (Bh*BOX_WIDTH/(Dy+2));
        Bhl = (int) (Bh*BOX_WIDTH/(Dy+2));

        ballRadius = 2*r; //ball radius. It is bigger than the real one in order to make the ball visible.
        if (( vect.get(N-2))>Dy) { //condition to reach the goal line
            if (Math.abs(vect.get(N - 3) - Dx) < Dw / 2 &&  vect.get(N - 1) < Dh) {//condition to enter the goal
                goal = true;
            }
        }
        for (n=0; 3*n<N; n++){
            if (vect.get(3*n+1)>By){ //barrier line reached.
                if (vect.get(3*n+2)<(Bh+ballRadius) && Math.abs(vect.get(3*n)-Bx)<(Bw/2+ballRadius)){//condition to hit barrier
                    goal = false; //if the ball hits the barrier there will be no goal.
                }else{ n=N/3;}
                break;
            }
        }

        if (goal){
            s = "GOAL";
            color = Color.BLUE;
        }else{
            s = "MISSED";
            color = Color.RED;
        }

        //System.out.println(N);
        N = 3*n; //we ignore the data following a barrier or ground hit (we checked for ground hit wen solving the ODE).
        //System.out.println(N);
        
        //we load the obtained results in a new vector more easy to handle.
        n= 1000/UPDATE_RATE; //we copy an element every n elements of the array list.
        N/=3;
        N/=n;
        pos = new double[3*N];
        projPos = new double[2*N];
        N*=3*n;
        double aux; //this parameter will help us define the points in the projected plane.
        for (T=0; 3*n*T<N; T++){ //get the adequate elements in order for the program to run at normal speed.
            pos[3*T] = vect.get(3*n*T); //points in euclidean space x, y and z.
            pos[3*T+1] = vect.get(3*n*T+1);
            pos[3*T+2] = vect.get(3*n*T+2);
            
            //points in projected plane.
            aux= -h/(b*pos[3*T+1]+c*(pos[3*T+2]-h));
            projPos[2*T] = c*aux*pos[3*T]; //x
            projPos[2*T+1] = aux*pos[3*T+1]; //y
        }

        // Start the ball bouncing (in its own thread)
        Thread gameThread = new Thread() {
            public void run() {
                for (T=0; T<pos.length/3; T++){
                    // Refresh the display
                    revalidate();
                    repaint(); // Callback paintComponent()

                    // Delay for timing control and give other threads a chance
                    try {
                        Thread.sleep(1000 / UPDATE_RATE);  // milliseconds
                    } catch (InterruptedException ex) { }
                }
                first = true;
                while (true) { // Execute a hole loop
                    for (T=0; T<pos.length/3; T++){
                        // Refresh the display
                        revalidate();
                        repaint(); // Callback paintComponent()

                        // Delay for timing control and give other threads a chance
                        try {
                            Thread.sleep(1000 / UPDATE_RATE);  // milliseconds
                        } catch (InterruptedException ex) { }
                    }
                }
            }
        };
        gameThread.start();  // Callback run()
    }

    /** Custom rendering codes for drawing the JPanel */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);    // Paint background

        //we are going to scale the found trajectories in order for them to fit the panel.
        // Draw the box
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, 2*BOX_WIDTH+2, 2*BOX_HEIGHT+2);
        
        //separate the drawn box in 4 parts.
        g.setColor(Color.WHITE);
        g.fillRect(BOX_WIDTH, 0, 2, 2*BOX_HEIGHT+2); //vertical white line.
        g.fillRect(0, BOX_HEIGHT, 2*BOX_WIDTH, 2); //horizontal white line.

        //draw the goals.
        //vertical cross section. The x component goes from -15 to 15,  we multiply by BOX_WIDTH/30.
        g.fillRect(Dxv, Dyv,Dwv, Dhv);
        //horizontal cross section. The y component goes from 0 to Dy+2, we multiply by BOX_HEIGHT/(Dy+2).
        g.fillRect(Dxh, Dyh, Dwh, 2);
        //lateral cross section. The y component goes from 0 to Dy+2, we multiply by BOX_WIDTH/(Dy+2).
        g.fillRect(Dxl , Dyl,2, Dhl);

        //projection (approximation). the y component goes from 0 to Dyp1+1, we multiply by BOX_HEIGHT/(Dyp1+1).
        /*g.fillRect(3*BOX_WIDTH/2+2- (int) (BOX_HEIGHT*Dxp/(Dyp1+1)),2*BOX_HEIGHT+2-(int) (BOX_HEIGHT*Dyp1/(Dyp1+1)),
                (int) (2*Dxp*BOX_HEIGHT/(Dyp1+1)), (int) (BOX_HEIGHT*Dyp2/(Dyp1+1)));*/


        //draw the barriers. 
        g.setColor(Color.GRAY);
        //vertical cross section. The x component goes from -15 to 15,  we multiply by BOX_WIDTH/30.
        g.fillRect(Bxv, Byv, Bwv, Bhv);

        //horizontal cross section. The y component goes from 0 to Dy+2, we multiply by BOX_HEIGHT/(Dy+2).
        g.fillRect(Bxh, Byh, Bwh,2);

        //lateral cross section. The y component goes from 0 to Dy+2, we multiply by BOX_WIDTH/(Dy+2).
        g.fillRect(Bxl, Byl,2, Bhl);

        //projection (approximation). the y component goes from 0 to Dyp1+1, we multiply by BOX_HEIGHT/(Dyp1+1).
        /*g.fillRect(3*BOX_WIDTH/2+2-(int) (Bxp*BOX_HEIGHT/(Dyp1+1)), 2*BOX_HEIGHT+2-(int) (Byp1*BOX_HEIGHT/(Dyp1+1)),
                (int) (2*Bxp*BOX_HEIGHT/(Dyp1+1)), (int) (Byp2*BOX_HEIGHT/(Dyp1+1)));*/


        //draw the balls.
        g.setColor(Color.BLACK);
        //vertical cross section. The x component goes from -15 to 15,  we multiply by BOX_WIDTH/30.
        g.fillOval((int) (BOX_WIDTH*((pos[3*T]-ballRadius)/15+1)/2), (int) (BOX_HEIGHT/2-(pos[3*T+2]+ballRadius)*BOX_WIDTH/30),
                (int)(ballRadius*BOX_WIDTH/15), (int)(ballRadius*BOX_WIDTH/15));

        //horizontal cross section. The y component goes from 0 to Dy+2, we multiply by BOX_HEIGHT/(Dy+2).
        g.fillOval((int) ((pos[3*T]-ballRadius)*BOX_HEIGHT/(Dy+2) + 3*BOX_WIDTH/2 + 2), (int) (BOX_HEIGHT -(pos[3*T+1]+ ballRadius)*BOX_HEIGHT/(Dy+2)),
                (int)(2*ballRadius*BOX_HEIGHT/(Dy+2)), (int)(2 * ballRadius*BOX_HEIGHT/(Dy+2)));

        //lateral cross section. The y component goes from 0 to Dy+2, we multiply by BOX_WIDTH/(Dy+2).
        g.fillOval((int) ((pos[3*T+1] - ballRadius)*BOX_WIDTH/(Dy+2)), (int) (3*BOX_HEIGHT/2+2 - (pos[3*T+2] + ballRadius)*BOX_WIDTH/(Dy+2)),
                (int)(2 * ballRadius*BOX_WIDTH/(Dy+2)), (int)(2 * ballRadius*BOX_WIDTH/(Dy+2)));

        //projection . the y component goes from 0 to Dyp1+1, we multiply by BOX_HEIGHT/(Dyp1+1).
        /*g.fillOval((int) ((projPos[2*T]-ballRadius)*BOX_HEIGHT/(Dyp1+1) + 3*BOX_WIDTH/2 + 2), (int) (2*BOX_HEIGHT+2 - (projPos[2*T+1] + ballRadius)*BOX_HEIGHT/(Dyp1+1)),
                (int)(2 * ballRadius*BOX_HEIGHT/(Dyp1+1)), (int)(2 * ballRadius*BOX_HEIGHT/(Dyp1+1)));*/

        //drar what s shown in every section of the panel
        g.drawString(stringH, BOX_WIDTH+3, 12);
        g.drawString(stringV, 1, 12);
        g.drawString(stringL, 1, BOX_HEIGHT+12);


        // Display the success of the action performed.
        if (first) {
            g.setColor(color);
            g.drawString(s, BOX_WIDTH - 13, BOX_HEIGHT);
        }
    }
}

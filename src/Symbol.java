import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Symbol extends JPanel {

    List<Double> points = new ArrayList<>();
    double score;
    private static JFrame window;
    double[] firstSymbol;
    double[] secondSymbol;


    public Symbol(double[] first, double[] second) {
        this.firstSymbol = first;
        this.secondSymbol = second;
    }

    private static final long serialVersionUID = 1L;
    private int labelPadding = 12;
    /**change the line color to the best you want;*/
    private Color lineColor = new Color(5,200,150);
    private Color fitnessColor = new Color(100,100,100);
    private Color pointColor = new Color(0,0,255 );
    private static final Stroke GRAPH_STROKE = new BasicStroke(3f);
    float[] dashl = {5,5};
    private   Stroke FITNESS_STROKE = new BasicStroke(0.5f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL,10,dashl,0);
    private static int pointWidth = 4;


    //javaxy3d


    /**
     * Math_Graph is a constructor method
     * @returns List scores;
     */


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Stroke oldStroke = g2.getStroke();
        g2.setStroke(oldStroke);
        g2.setColor(pointColor);




        int x1, y1=50, w, h;

        //рисуем букву
    for (int j = 0 ; j < 7 ; j++) {
        for (int i = 0; i < 5; i++) {

            w = 50;
            h = 50;
            if (firstSymbol[i + j*5] == 1) {
                x1 = 50 + i * 50;
                g2.drawRect(x1, y1, w, h);
            }
        }
        y1+=50;
    }


    int x2, y2 = 50;
        for (int j = 0 ; j < 7 ; j++) {
            for (int i = 0; i < 5; i++) {

                w = 50;
                h = 50;
                if (secondSymbol[i + j*5] == 1) {
                    x2 =400+ 50 + i * 50;
                    g2.drawRect(x2, y2, w, h);
                }
            }
            y2+=50;
        }








//        g2.setFont(new Font("TimesRoman", Font.PLAIN, 30));
//        g2.setColor(Color.BLACK);
//        g2.drawString("Значение ошибки, 300 эпох: ", 800,30);
//        g2.drawString(""+ score, 800,60);

    }



    /* creating the method createAndShowGui in the main method, where we create the frame too and pack it in the panel*/
    public static void createAndShowGui(double[] first, double[] second) {
        /* Main panel */
        Symbol mainPanel = new Symbol(first,second);
        mainPanel.setPreferredSize(new Dimension(800, 600));
        /* creating the frame */
        JFrame frame = new JFrame("Sample Graph");
        //JFrame frame = Singleton.getInstance();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }




}


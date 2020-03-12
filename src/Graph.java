import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Graph extends JPanel {

    List<Double> points = new ArrayList<>();
    double score;
    private static JFrame window;


    public Graph(List<Double> points, double score) {
        this.points = points;
        this.score = score;
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




        //Точки для графика целевой функции и фитнесс функции
        List<Point> graphPoints = new ArrayList<>();
        List<Point> fitnessPoints = new ArrayList<>();

        for (int i = 0; i < 1000; i+=1) {
            double tX = -5 + (i * 1.0 / 100);
            tX = -tX;
            double tY = 0.1*tX - 1.7*Math.abs(Math.sin(5.8*tX))*Math.cos(3.2*tX);
            double fY = 2 - tY;
            tY = -tY;
            fY = -fY;

            int x1 = (int) ( ( Math.abs(-5 - tX) ) * 80);
            int y1 = (int)  (tY * 100);

            int y2 = (int)  (fY * 100); //фитнесс


            graphPoints.add(new Point(x1, y1 + 400));
            fitnessPoints.add(new Point(x1, y2 + 400));
        }

        //Точки особей популяции

        List<Point> populationPoints = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            double tX = i;
            double tY = points.get(i);
            tY = -tY;
            int x1 = (int) (tX * 4);
            int y1 = (int)  (tY * 350);
            populationPoints.add(new Point(x1 , y1 + 700));
        }





        g2.drawLine(600,0,600,800);
        g2.drawLine(0,700,1200,700);

        //Разметка оси Y

            g2.drawLine(590, 350, 610, 350);
            g2.drawLine(590, 2, 610, 2);
            g2.drawLine(590, 630, 610, 630);
            g2.drawLine(590, 560, 610, 560);
            g2.drawLine(590, 490, 610, 490);
            g2.drawLine(590, 420, 610, 420);
            g2.drawLine(590, 665, 610, 665);

        g2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g2.drawString("2",580,10);
        g2.drawString("1",580,350);
        g2.drawString("0.8",570,420);
        g2.drawString("0.6",570,490);
        g2.drawString("0.4",570,560);
        g2.drawString("0.2",570,630);
        g2.drawString("0.1",570,665);

        //Разметка оси X
        for (int i = 0; i <=1200 ; i+=80) {
            g2.drawLine(i, 690, i, 710);

            int temp = i / 4;
            g2.drawString(""+temp,i,720);
        }



        //рисуем график
        Stroke oldStroke = g2.getStroke();
        //Рисуем точки
        g2.setStroke(oldStroke);
        g2.setColor(pointColor);
        for (int i = 0; i < populationPoints.size(); i++) {
            int x = populationPoints.get(i).x - pointWidth / 2;
            int y = populationPoints.get(i).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);
        }

        g2.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        g2.setColor(Color.BLACK);
        g2.drawString("Значение ошибки, 300 эпох: ", 800,30);
        g2.drawString(""+ score, 800,60);

    }



    /* creating the method createAndShowGui in the main method, where we create the frame too and pack it in the panel*/
    public static void createAndShowGui(List<Double> points, double score, String str) {
        /* Main panel */
        Graph mainPanel = new Graph(points,score);
        mainPanel.setPreferredSize(new Dimension(1200, 800));
        /* creating the frame */
        JFrame frame = new JFrame(str);
        //JFrame frame = Singleton.getInstance();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }




}

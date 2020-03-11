import java.awt.*;
import java.util.ArrayList;

public class NeuralNetwork {

    ArrayList<Double> points = new ArrayList<>();
    double score;
    double[] enters;
    double[] inputs;
    double[] hidden;
    double[] outers;
    double nu = 0.6;
    double[][] wEI;
    double[][] wIH;
    double[] a = {0.5,0.7,2.6}; //коэф наклона
    double[][] wHO;
    double[][] patterns  = {
                   {1,1,1,1,1,
                    1,0,0,0,1,
                    1,0,0,0,1,
                    1,0,0,0,1,          //0
                    1,0,0,0,1,
                    1,0,0,0,1,
                    1,1,1,1,1} ,

                   {0,0,0,0,1,
                    0,0,0,1,1,
                    0,0,1,0,1,
                    0,1,0,0,1,          //1
                    0,0,0,0,1,
                    0,0,0,0,1,
                    0,0,0,0,1} ,

                   {1,0,1,0,1,
                    1,0,1,0,1,
                    1,0,1,0,1,
                    1,1,1,1,1,          //Ж
                    1,0,1,0,1,
                    1,0,1,0,1,
                    1,0,1,0,1} ,

                   {1,1,1,1,1,
                    1,0,1,0,1,
                    1,0,1,0,1,
                    1,1,1,1,1,          //Ф
                    0,0,1,0,0,
                    0,0,1,0,0,
                    0,0,1,0,0}
    };
    double[][] answers ={   {1,0,0,0},
                            {0,1,0,0},
                            {0,0,1,0},
                            {0,0,0,1}  };

    NeuralNetwork() {
        enters = new double[35];

        inputs = new double[6];
        hidden = new double[6];
        outers = new double[4];
        wEI = new double[enters.length][inputs.length];
        wHO = new double[hidden.length][outers.length];
        wIH = new double[inputs.length][hidden.length];
    }

    public void init() {

        for (int i = 0; i < wEI.length; i++) {
            for (int j = 0; j < wEI[i].length; j++) {
                wEI[i][j] = Math.random() * 2 - 1;
            }
        }

        for (int i = 0; i < wIH.length; i++) {
            for (int j = 0; j < wIH[i].length; j++) {
                wIH[i][j] = Math.random() * 2 - 1;
            }
        }

        for (int i = 0; i < wHO.length; i++) {
            for (int j = 0 ; j < wHO[i].length; j++) {
                wHO[i][j] = Math.random() * 2 - 1;
            }
        }
    }


    public void countOut() {

        for (int i = 0; i < inputs.length; i++) {
            inputs[i] = 0;
            for (int j = 0; j < enters.length; j++) {
                inputs[i] += enters[j] * wEI[j][i];
            }
            inputs[i] = 1 / (1 + Math.exp(-a[0] * inputs[i]));
        }


        for (int i = 0; i < hidden.length; i++) {
            hidden[i] = 0;
            for (int j = 0; j < inputs.length; j++) {
                hidden[i] += inputs[j] * wIH[j][i];
            }
            hidden[i] = 1 / (1 + Math.exp(-a[1] * hidden[i]));
        }

        for (int i = 0 ; i < outers.length; i++) {
            outers[i] = 0;
            for (int j = 0; j < hidden.length; j++)
                outers[i] += hidden[j] * wHO[j][i];

            outers[i] = 1 / (1 + Math.exp(-a[2] * outers[i]));
        }
    }


    public void study() {
        double[] errorInputs = new double[inputs.length];  //дельты первого слоя
        double[] errorHidden = new double[hidden.length];  // дельты скрытого слоя
        double gError = 0;  //общая ошибка сети
        double[] errorOuters = new double[outers.length];  // дельты выходного слоя
        int counter = 0;  //счетчик эпох
        do {
            gError = 0;
            for (int p = 0 ; p < patterns.length; p ++) {  // patterns - это двумерный массив в котором хранятся наборы входных значений

                for (int i = 0; i < enters.length; i++)  // устанавливаем входной набор
                    enters[i] = patterns[p][i];

                countOut();  // рассчитываем выходы

                double lError = 0;

                for (int i = 0 ; i < outers.length; i ++)  //ошибка сети
                    lError+= (outers[i] - answers[p][i]) * (outers[i] - answers[p][i]);


                gError+=Math.abs(lError);

                for (int i = 0; i < outers.length; i++)   //рассчитываем дельты выходного слоя
                    errorOuters[i] = (outers[i] - answers[p][i]) * a[2]  * outers[i] * (1 - outers[i]);


                for (int i = 0; i < hidden.length; i++) { //рассчитываем дельты скрытого слоя
                    double temp = 0;
                    for (int j = 0; j < outers.length; j++) {
                        temp += errorOuters[j] * wHO[i][j] ;
                    }
                    errorHidden[i] = temp * a[1] *  hidden[i] * (1-hidden[i]);
                }

                for (int i = 0; i < inputs.length; i++) { //рассчитываем дельты входного слоя
                    double temp = 0;
                    for (int j = 0; j < hidden.length; j++) {
                        temp += errorHidden[j] * wIH[i][j] ;
                    }
                    errorInputs[i] = temp * a[0] * inputs[i] * (1-inputs[i]);
                }


                for (int i = 0 ; i < enters.length; i++) {
                    for (int j = 0; j < inputs.length; j++) {
                        wEI[i][j] = wEI[i][j] - nu * errorInputs[j] * enters[i];
                    }
                }

                for (int i = 0 ; i < inputs.length; i++) {
                    for (int j = 0; j < hidden.length; j++) {
                        wIH[i][j] = wIH[i][j] - nu * errorHidden[j] * inputs[i];
                    }
                }

                for (int i = 0 ; i < hidden.length; i ++) {  //wHO - weights hidden output (веса от скрытого к выходному)
                    for (int j = 0 ; j < outers.length; j++) {
                        wHO[i][j] = wHO[i][j] - nu * errorOuters[j] * hidden[i];
                    }
                }
            }

            counter++;
            points.add(gError);
        } //while (gError >0.05);
            while (counter < 300);
        System.out.println("Count = " + counter);
        score = gError;

    }

    public void showResult(double[] inArray) {
        enters = inArray;
        countOut();
        System.out.println(outers[0] + "|" + outers[1] + "|" + outers[2] + "|" + outers[3]);
    }


    public static void main(String[] args) { 

        NeuralNetwork nn = new NeuralNetwork();

        nn.init();
        nn.study();


        //Graph.createAndShowGui(nn.points, nn.score);
        Symbol.createAndShowGui(nn.patterns[0], nn.patterns[1]);


    }
}


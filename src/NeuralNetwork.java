import java.awt.*;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class NeuralNetwork {

    ArrayList<Double> points = new ArrayList<>();
    ArrayList<Double> testPoints = new ArrayList<>();
    double score;
    double testScore;
    double[] enters;
    double[] inputs;
    double[] hidden;
    double[] outers;
    double nu = 0.6;
    double[][] wEI;
    double[][] wIH;
    double[] a = {0.6,0.8,2.6}; //коэф наклона
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
                            {0,0,0,1},
                            {1,0,0,0},
                            {0,1,0,0},
                            {0,0,1,0},
                            {0,0,0,1}   };

    double[][] brokenLetters = new double[8][35]; // массив испорченных символов



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

        int j = 0;
        for (int i = 0 ;i < brokenLetters.length; i++) {
            brokenLetters[i] = getBrokenLetter(patterns[j]);
            j++;
            if (i == 3)
                j = 0;
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
        double gTestError = 0;  //общая ошибка сети
        double[] errorOuters = new double[outers.length];  // дельты выходного слоя
        int counter = 0;  //счетчик эпох
        do {
            gError = 0;
            gTestError = 0;
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


            //считаем ошибку тестирования

            for (int p = 0 ; p < brokenLetters.length; p++) {

                for (int i = 0; i < enters.length; i++)  // устанавливаем входной набор
                    enters[i] = brokenLetters[p][i];

                countOut();  // рассчитываем выходы

                double lTestError = 0;

                for (int i = 0 ; i < outers.length; i ++)  //ошибка сети
                    lTestError+= (outers[i] - answers[p][i]) * (outers[i] - answers[p][i]);

                gTestError+=Math.abs(lTestError);

            }


            counter++;
            points.add(gError);
            testPoints.add(gTestError);
        } //while (gError >0.05);
            while (counter < 300);
        testScore = gTestError;
        score = gError;

    }

    public void showResult(double[] inArray) {
        enters = inArray;
        countOut();
    }

    public double[] calculateAnswer(double[] letter) {
        double[] errors = new double[4];
        errors[0] = 0; errors[1] = 0; errors[2] = 0; errors[3] = 0;
        enters = letter;
        countOut();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                errors[i] += (answers[i][j] - outers[j]) * (answers[i][j] - outers[j]);
            }
        }
        System.out.println(errors[0]);
        System.out.println(errors[1]);
        System.out.println(errors[2]);
        System.out.println(errors[3]);
        System.out.println("______________");
        int min = 0;
        for (int i = 1 ; i < 4 ; i++) {
            if (errors[i] < errors[min]) {
                min = i;
            }

        }
        return patterns[min];
    }

    public double[] getBrokenLetter(double[] letter) {
        double random = 0;
        double[] result = new double[letter.length];
        for (int i = 0 ; i < letter.length; i++) {
            random = Math.random();
            if (random < 0.1) {
                result[i] = letter[i] == 1 ? 0 : 1;
            } else
                result[i] = letter[i];
        }
        return result;
    }


    public static void main(String[] args) { 

        NeuralNetwork nn = new NeuralNetwork(); //создаем объект нейронной сети

        nn.init();  // инициализация весов и кол-ва нейронов в слоях
        nn.study(); //  обучение


        Graph.createAndShowGui(nn.points, nn.score, "Error");  // график ошибки
        Graph.createAndShowGui(nn.testPoints, nn.testScore, "Test Error");  // график ошибки тестирования

        for (int i = 0 ; i < 8; i++)
        Symbol.createAndShowGui(nn.brokenLetters[i], nn.calculateAnswer(nn.brokenLetters[i]));  //отрисовка символов по 2 шт (испорчен - результат)


    }
}


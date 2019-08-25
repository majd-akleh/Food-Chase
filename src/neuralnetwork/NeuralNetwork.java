/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork;

import game.Direction;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcil & Majd
 */
public class NeuralNetwork implements NeuralNetworkInterface,Serializable {

    double w[][][];
    double v[][];
    private int size;
    private final int inputMinValue;
    private final int inputMaxValue;

    public int getSize() {
        return size;
    }
    public static boolean saveNetWork(String fileName,NeuralNetwork network){
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(fileName)));
            out.writeObject(network);
            out.close();
            return true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NeuralNetwork.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NeuralNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    public static NeuralNetwork readNeuralNetworkFromFile(String fileName) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(fileName)));
            return (NeuralNetwork)in.readObject();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NeuralNetwork.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(NeuralNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public NeuralNetwork(int inputMinVaule, int inputMaxValue, int... ns) {
        this.inputMinValue = inputMinVaule;
        this.inputMaxValue = inputMaxValue;
        v = new double[ns.length][];
        size = 0;
        for (int i = 0; i < ns.length; ++i) {
            v[i] = new double[ns[i] + 1];
//            if (i < ns.length - 1) {
//                v[i] = new double[ns[i] + 1];
//                v[i][ns[i]] = 1;
//            } else {
//                v[i] = new double[ns[i]];
//            }
        }

        w = new double[ns.length - 1][][];
        for (int i = 0; i < ns.length - 1; ++i) {
            w[i] = new double[v[i].length][v[i + 1].length - 1];
            size += v[i].length * (v[i + 1].length - 1);
        }
    }

    @Override
    public final void setWheights(double W[]) {
        int x = 0;
        for (int i = 0; i < w.length; ++i) {
            for (int j = 0; j < w[i].length; ++j) {
                for (int k = 0; k < w[i][j].length; ++k) {
                    w[i][j][k] = W[x++];
                }
            }
        }
    }

    @Override
    public final void SetRandomWieghts(long seed, int min, int max) {
        Random random = new Random(seed);
        double[] wieghts = new double[getSize()];
        for (int i = 0; i < wieghts.length; ++i) {
            wieghts[i] = random.nextDouble() * (max - min) + min;
        }
        setWheights(wieghts);
    }

    public NeuralNetwork(double W[], int inputMinValue, int inputMaxValue, int... ns) {
        this(inputMinValue, inputMaxValue, ns);
        setWheights(W);
    }

    //SIGMOID
    double activation(double z) {
        return 1.0 / (1.0 + Math.exp(-z));
    }

    private void clearV() {
        for (int i = 0; i < v.length; ++i) {
            for (int j = 0; j < v[i].length - 1; ++j) {
                v[i][j] = 0;
            }
            v[i][v[i].length - 1] = 1;
        }
    }

    private void setInput(double[] input) {
        clearV();
        for (int i = 0; i < v[0].length - 1; ++i) {
            v[0][i] = input[i];
        }
    }

    public void evaluate() {
        for (int i = 0; i < v.length - 1; ++i) {
            for (int j = 0; j < v[i].length; ++j) {
                for (int k = 0; k < v[i + 1].length - 1; ++k) {
                    v[i + 1][k] += v[i][j] * w[i][j][k];
                }
            }
        }
        for (int i = 0; i < v.length; ++i) {
            for (int j = 0; j < v[i].length - 1; ++j) {
                v[i][j] = activation(v[i][j]);
            }
        }
    }

    private double[] getOutputThresholds() {
        return Arrays.copyOf(v[v.length - 1], v[v.length - 1].length - 1);

    }

    public int indexOfMaximum() {
        double maxv = -Double.MAX_VALUE;
        int idx = -1;
        double[] out = getOutputThresholds();
        for (int i = 0; i < out.length; ++i) {
            if (out[i] > maxv) {
                maxv = out[i];
                idx = i;
            }
        }
        return idx;
    }

    public double normalize(int x) {
        return (double) (x - inputMinValue) / (double) (inputMaxValue - inputMinValue);
    }

    @Override
    public void setInput(int[] input) {
        double in[] = new double[input.length];
        for (int i = 0; i < input.length; ++i) {
            in[i] = normalize(input[i]);
        }
        setInput(in);

    }

    @Override
    public Direction getOutput() {
        evaluate();
        return Direction.values()[indexOfMaximum()];
    }

    @Override
    public int getIntOutput() {
        evaluate();
        return indexOfMaximum();
    }

    @Override
    public void debug() {
        System.out.println("======================================================");

        System.out.printf("thresholds values:\n");
        for (int i = 0; i < v.length; ++i) {
            System.out.printf("layer %d values:\n", i);
            for (int j = 0; j < v[i].length; ++j) {
                System.out.printf("%f%c", v[i][j], j + 1 == v[i].length ? '\n' : ' ');
            }
        }
        System.out.printf("wieghts:\n");
        for (int i = 0; i < w.length; ++i) {
            System.out.printf("layer %d wheights:\n", i);
            for (int j = 0; j < w[i].length; ++j) {
                for (int k = 0; k < w[i][j].length; ++k) {
                    System.out.printf("neuron %d and neuron %d wheight = %f\n", j, k, w[i][j][k]);
                }
            }
        }
        System.out.println("======================================================");
    }

}

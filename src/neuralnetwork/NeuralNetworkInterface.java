/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork;

import game.Direction;

/**
 *
 * @author Marcil & Majd
 */
public interface NeuralNetworkInterface  {

    public void setInput(int input[]);

    public Direction getOutput();
    public void setWheights(double[] w);
    public void SetRandomWieghts(long seed,int min,int max);
    public int getIntOutput();
    public int getSize();
    public void debug();
}

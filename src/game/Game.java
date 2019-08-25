package game;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import neuralnetwork.NeuralNetworkInterface;

/**
 *
 * @author Marcil & Majd
 */
public interface Game {

    public void setGuiEnabled(boolean guiEnabled);

    public boolean isGuiEnabled();

    public double evaluate(NeuralNetworkInterface network);

    public void setSightSize(int size);

    public int getTotalSightSize();

    public double getMaxPossibleScore();
}

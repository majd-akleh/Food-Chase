/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fitness;

import game.Game;
import neuralnetwork.NeuralNetworkInterface;
import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

/**
 *
 * @author Marcil & Majd
 */
public abstract class MyFitnessFunction extends FitnessFunction {

    Game game;
    NeuralNetworkInterface network;
    public final static double EPS = 1e-3;

    /**
     *
     * @param game
     * @param network
     */
    public MyFitnessFunction(Game game, NeuralNetworkInterface network) {
        super();
        this.game = game;
        this.network = network;
    }

    public abstract double[] getWieghts(IChromosome chromosome);
    

    @Override
    protected double evaluate(IChromosome ic) {
        network.setWheights(getWieghts(ic));
        return game.evaluate(network);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fitness;

import game.Game;
import neuralnetwork.NeuralNetworkInterface;
import org.jgap.IChromosome;

/**
 *
 * @author Marcil & Majd
 */
public class DoubleWeightsFitnesFunction extends MyFitnessFunction {

    public DoubleWeightsFitnesFunction(Game game, NeuralNetworkInterface network) {
        super(game, network);
    }

    @Override
    public double[] getWieghts(IChromosome chromosome) {

        int N = chromosome.getGenes().length;
        double w[] = new double[N];
        for (int i = 0; i < N; ++i) {
            w[i] = (double) (chromosome.getGene(i)).getAllele();
        }
        return w;
    }

}

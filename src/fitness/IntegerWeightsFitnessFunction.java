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
public class IntegerWeightsFitnessFunction extends MyFitnessFunction {

    public final double minWeightBound, maxWeightBound;
    public final int minGeneBound,maxGeneBound;

    public IntegerWeightsFitnessFunction(double minWeightBound, double maxWeightBound, int minGeneBound, int maxGeneBound, Game game, NeuralNetworkInterface network) {
        super(game, network);
        this.minWeightBound = minWeightBound;
        this.maxWeightBound = maxWeightBound;
        this.minGeneBound = minGeneBound;
        this.maxGeneBound = maxGeneBound;
    }
 
    
    public double adjust(int x){
        return ((double)(x-minGeneBound)/(double)(maxGeneBound-minGeneBound+1))*(maxWeightBound-minWeightBound) + minWeightBound;
    }
    @Override
    public double[] getWieghts(IChromosome chromosome) {
        
        int N = chromosome.getGenes().length;
        double w[] = new double[N];
        for (int i = 0; i < N; ++i) {
            w[i] = adjust((int)(chromosome.getGene(i)).getAllele());
        }
        return w;
    }

}

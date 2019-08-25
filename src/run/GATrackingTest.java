package run;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import fitness.IntegerWeightsFitnessFunction;
import game.Game;
import neuralnetwork.NeuralNetwork;
import neuralnetwork.NeuralNetworkInterface;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.DefaultFitnessEvaluator;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.event.EventManager;
import org.jgap.impl.BestChromosomesSelector;
import org.jgap.impl.ChromosomePool;
import org.jgap.impl.CrossoverOperator;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.GreedyCrossover;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.StockRandomGenerator;
import org.jgap.impl.SwappingMutationOperator;
import org.jgap.impl.TournamentSelector;

/**
 *
 * @author Marcil & Majd
 */
public class GATrackingTest {

    public final Game game;
    public final NeuralNetworkInterface network;
    public final Genotype population;
    public final double minWeightBound, maxWeightBound;
    public final int minGeneBound, maxGeneBound, populationSize, iterations, simulationLength, guiDelay;
    public final IntegerWeightsFitnessFunction myFunc;

    public GATrackingTest() throws InvalidConfigurationException {

        this.minWeightBound = -10;
        this.maxWeightBound = 10;

        this.minGeneBound = 0;
        this.maxGeneBound = (int) 1 << 20 - 1;

        this.populationSize = 500;
        this.iterations = 1000;
        this.simulationLength = 100;
        this.guiDelay = 10;

        Configuration conf = new Configuration();
        TournamentSelector selector = new TournamentSelector(conf, populationSize, 0.5);
        conf.addNaturalSelector(selector, true);
        conf.setRandomGenerator(new StockRandomGenerator());
        conf.setMinimumPopSizePercent(0);
        conf.setEventManager(new EventManager());
        conf.setFitnessEvaluator(new DefaultFitnessEvaluator());
        conf.setChromosomePool(new ChromosomePool());
        //--------------------------------------------------
        conf.addGeneticOperator(new CrossoverOperator(conf));
        conf.addGeneticOperator(new SwappingMutationOperator(conf, 20));
        //--------------------------------------------------
        conf.setPopulationSize(populationSize);

        game = new TrackingGame(simulationLength, 10, 10);
        network = new NeuralNetwork(0, 10, 4, 5, 5, 4);
        conf.setPreservFittestIndividual(true);
        int chromosomeLenght = network.getSize();
        myFunc = new IntegerWeightsFitnessFunction(minWeightBound, maxWeightBound, minGeneBound, maxGeneBound, game, network);
        conf.setFitnessFunction(myFunc);
        Gene[] sampleGenes = new Gene[network.getSize()];
        for (int i = 0; i < chromosomeLenght; ++i) {
            sampleGenes[i] = new IntegerGene(conf, minGeneBound, maxGeneBound);
        }
        IChromosome sampleChromosome = new Chromosome(conf, sampleGenes);
        conf.setSampleChromosome(sampleChromosome);
        population = Genotype.randomInitialGenotype(conf);
        game.setGuiEnabled(false);
    }

    public void solve() {

        ((TrackingGame) this.game).setSeed(13);
        for (int i = 0; i < iterations; ++i) {
            population.evolve();

            if (i % 1000 == 0) {
                network.setWheights(myFunc.getWieghts(population.getFittestChromosome()));
                NeuralNetwork.saveNetWork("networkPOP" + populationSize + "GEN" + i + "LEN" + simulationLength + ".object", (NeuralNetwork) network);
            }
            if (i % guiDelay == 0) {
                network.setWheights(myFunc.getWieghts(population.getFittestChromosome()));
                game.setGuiEnabled(true);
                System.out.print("epoch = " + i + "\t");
                game.evaluate(network);
                game.setGuiEnabled(false);

            }
        }
        System.out.println(population.getFittestChromosome().getFitnessValue());
        network.setWheights(myFunc.getWieghts(population.getFittestChromosome()));
        NeuralNetwork.saveNetWork("network" + populationSize + "X" + iterations + "X" + simulationLength + ".object", (NeuralNetwork) network);
        game.evaluate(network);
        TrackingVisualizeation trackingVisualizeation = new TrackingVisualizeation(50, new int[10][10], ((TrackingGame) game).getMyMoves(), ((TrackingGame) game).getFoodMoves(), ((TrackingGame) game).getSteps());
        trackingVisualizeation.simulate(300);
    }

    public static void main(String[] args) throws InvalidConfigurationException {
        new GATrackingTest().solve();

    }
}

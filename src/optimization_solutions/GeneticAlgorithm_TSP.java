package optimization_solutions;

import core_algorithms.GeneticAlgorithm;
import core_algorithms.Individual;
import optimization_problems.TSP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Implement elements that are problem specific
 *
 */
public class GeneticAlgorithm_TSP
        extends GeneticAlgorithm<Integer> {
    private final TSP problem;

    public GeneticAlgorithm_TSP(
            int maxGen, double mRate, double elitism, TSP problem){
        super(maxGen, mRate, elitism);
        this.problem = problem;
    }

    public double calcFitnessScore(
            List<Integer> chromosome){
        return 1/problem.cost(chromosome);
    }

    public Individual<Integer> reproduce(
            Individual<Integer> p1, Individual<Integer> p2) {
        System.out.println("Reproducing...");
        List<Integer> child_chromosone = new ArrayList<>(p1.getChromosome().size());
        while(child_chromosone.size() < p1.getChromosome().size()){
            child_chromosone.add(9999999);
        }
        int sequence_first_index = new Random().nextInt(0, p1.getChromosome().size()-1);
        int sequence_last_index = new Random().nextInt(sequence_first_index, p1.getChromosome().size()-1);
        for (int gene=sequence_first_index; gene<=sequence_last_index; gene++){
            child_chromosone.set(gene, p1.getChromosome().get(gene));
        }

        System.out.println(p2.getChromosome());
        System.out.println(child_chromosone);
        for (int gene : p2.getChromosome()){
            if(!child_chromosone.contains(gene)){
                int current_gene = 0;
                while(child_chromosone.get(current_gene) != 9999999){
                    current_gene += 1;
                }
                child_chromosone.set(current_gene, gene);
            }
        }
        System.out.println(child_chromosone);


        return new Individual<>(child_chromosone, calcFitnessScore(child_chromosone));
    }

    public Individual<Integer> mutate(Individual<Integer> i){
        System.out.println("Mutating...");
        Random random = new Random();
        List<Integer> chromosone = i.getChromosome();
        int index_one = random.nextInt(0, chromosone.size()-1);
        int index_two = index_one;
        while(index_two == index_one){
            index_two = random.nextInt(0, chromosone.size()-1);
        }
        int gene_at_position_one = chromosone.get(index_one);
        int gene_at_position_two = chromosone.get(index_two);
        chromosone.set(index_one, gene_at_position_two);
        chromosone.set(index_two, gene_at_position_one);
        return new Individual<>(chromosone, calcFitnessScore(chromosone));
    }

    public List<Individual<Integer>> generateInitPopulation(
            int popSize, int numCities ){
        List<Individual<Integer>> population =
                new ArrayList<>(popSize);
        for(int i=0; i<popSize; i++){
            List<Integer> chromosome = new ArrayList<>(numCities);
            for(int j=0; j<numCities; j++){
                chromosome.add(j);

            }
            Collections.shuffle(chromosome);
            Individual<Integer> indiv = new Individual<>(
                    chromosome, calcFitnessScore(chromosome));
            population.add(indiv);
            System.out.println(indiv);
        }
        return population;
    }

    public static void main(String[] args) {
        int MAX_GEN = 200;
        double MUTATION_RATE = 0.05;
        int POPULATION_SIZE = 1000;
        int NUM_CITES = 5; //choose from 5, 6, 17, 26
        double ELITISM = 0.2;
        System.out.printf("Max Gen: %s%nMutation Rate: %s%nPopulation Size: %s%nNumber of cities: %s%n", MAX_GEN, MUTATION_RATE, POPULATION_SIZE, NUM_CITES);

        TSP problem = new TSP(NUM_CITES);

        GeneticAlgorithm_TSP agent = new GeneticAlgorithm_TSP(
                MAX_GEN, MUTATION_RATE, ELITISM, problem);

        Individual<Integer> best =
                agent.evolve(agent.generateInitPopulation(
                        POPULATION_SIZE, NUM_CITES));

        System.out.println(best);
        System.out.println(problem.cost(best.getChromosome()));
    }

}

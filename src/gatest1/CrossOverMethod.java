/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gatest1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author ravindu kaluarachchi
 */
public class CrossOverMethod {
    private List<Individual> inidividuals;
    private double crossOverRate;
    private int chromosomeLength; 
    private int populationSize;
    private boolean elitist;

    public CrossOverMethod(List<Individual> inidividuals, int populationSize, int chromosomeLength, double crossOverRate, boolean elitist) {
        this.inidividuals = inidividuals;
        this.crossOverRate = crossOverRate;
        this.chromosomeLength = chromosomeLength;
        this.populationSize = populationSize;
        this.elitist = elitist;
    }
            
    public List<Individual> twoPointCrossOver(){
        List<Individual> unSelectedParents = new ArrayList<>();
        //list to keep the new population
        List<Individual> newIndividuals = new ArrayList<>();
        unSelectedParents.addAll(inidividuals);
        int crossOverAmount = new Double(inidividuals.size() * crossOverRate).intValue() / 2;
        Random random = new Random();
        for (int i = 0; i < crossOverAmount; i++) {
            System.out.println("un selected parents size : " + unSelectedParents.size());
            System.out.println("cross over i : " + i + " start ---");
            int parent1Index = random.nextInt(unSelectedParents.size());
            System.out.println("parent 1 index " + parent1Index);
            int parent2Index = random.nextInt(unSelectedParents.size());
            System.out.println("parent 2 index " + parent2Index);
            while (parent1Index == parent2Index) {
                parent2Index = random.nextInt(unSelectedParents.size());
                System.out.println("@@parent 2 index " + parent2Index);
            }
            
            int parent3Index = random.nextInt(unSelectedParents.size());
            System.out.println("parent 3 index " + parent3Index);
            while (parent1Index == parent3Index
                    || parent2Index == parent3Index) {
                parent3Index = random.nextInt(unSelectedParents.size());
                System.out.println("@p@arent 3 index " + parent3Index);
            }
            
            int parent4Index = random.nextInt(unSelectedParents.size());
            System.out.println("parent 4 index " + parent4Index);
            while (parent1Index == parent4Index
                    || parent2Index == parent4Index
                    || parent3Index == parent4Index) {
                parent4Index = random.nextInt(unSelectedParents.size());
                System.out.println("@@parent 4 index " + parent4Index);
            }
            
            Individual candidateParent1 = unSelectedParents.get(parent1Index);
            Individual candidateParent2 = unSelectedParents.get(parent2Index);
            Individual candidateParent3 = unSelectedParents.get(parent3Index);
            Individual candidateParent4 = unSelectedParents.get(parent4Index);
            
            List<Individual> candidateList = new ArrayList<>();
            candidateList.add(candidateParent1);
            candidateList.add(candidateParent2);
            candidateList.add(candidateParent3);
            candidateList.add(candidateParent4);
            
            Individual parent1 = null;
            Individual parent2 = null;
            for (Individual individual : candidateList) {
                if (parent1 == null) {
                    parent1 = individual;
                } else if (parent1.fitness < individual.fitness) {
                    parent1 = individual;
                }
            }
            candidateList.remove(parent1);
            
            for (Individual individual : candidateList) {
                if (parent2 == null) {
                    parent2 = individual;
                } else if (parent2.fitness < individual.fitness) {
                    parent2 = individual;
                }
            }
            
            System.out.println("parent1 >> " + parent1);
            System.out.println("parent2 >> " + parent2);
            
            List<Individual> removeList = new ArrayList<>();
            removeList.add(candidateParent1);
            removeList.add(candidateParent2);
            removeList.add(candidateParent3);
            removeList.add(candidateParent4);
            
            unSelectedParents.removeAll(removeList);
            System.out.println("1");
            //2point cross over
            int point1 = random.nextInt(chromosomeLength - 1);
            int point2;
            do {
                point2 = random.nextInt(chromosomeLength);
                System.out.println("2 >> " + point2 + " >= " + point1);
            } while (point2 < point1);
            System.out.println("3");
            //child1
            int[] newChromosome = new int[chromosomeLength];
            for (int j = 0; j < point1; j++) {
                newChromosome[j] = parent1.genes[j];
            }
            System.out.println("4");
            for (int j = point1; j < point2; j++) {
                newChromosome[j] = parent2.genes[j];
            }
            System.out.println("5");
            for (int j = point2; j < newChromosome.length; j++) {
                newChromosome[j] = parent1.genes[j];
            }
            System.out.println("6");
            System.out.println("7");
            Individual child1 = new Individual(0, 0, newChromosome);
            newIndividuals.add(child1);
            //child2
            newChromosome = new int[chromosomeLength];
            for (int j = 0; j < point1; j++) {
                newChromosome[j] = parent2.genes[j];
            }
            System.out.println("8");
            for (int j = point1; j < point2; j++) {
                newChromosome[j] = parent1.genes[j];
            }
            System.out.println("9");
            for (int j = point2; j < newChromosome.length; j++) {
                newChromosome[j] = parent2.genes[j];
            }
            System.out.println("10");
            Individual child2 = new Individual(0, 0, newChromosome);
            newIndividuals.add(child2);
            System.out.println("cross over i : " + i + " end ---");
        }
        System.out.println("new individual size " + newIndividuals.size());
        for (Individual newIndividual : newIndividuals) {
            System.out.println(newIndividual);
        }
        List<Individual> newPopulation = new ArrayList<>();
        newPopulation.addAll(newIndividuals);

        
        int remainingPopulationSize = populationSize - newIndividuals.size();
        Collections.shuffle(inidividuals);
        for (int i = 0; i < remainingPopulationSize; i++) {
            inidividuals.get(i).x = 0;
            inidividuals.get(i).y = 0;
            //inidividuals.get(i).fitness = 0;
            newPopulation.add(inidividuals.get(i));
        }
        //inidividuals = newPopulation;
        return newPopulation;
    }
}

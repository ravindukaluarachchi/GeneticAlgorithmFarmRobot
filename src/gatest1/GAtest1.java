/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gatest1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author ravindu
 */
class Individual {

    int x;
    int y;
    int[] genes;
    double fitness;

    public Individual(int x, int y, int[] genes) {
        this.x = x;
        this.y = y;
        this.genes = genes;
    }

    @Override
    public String toString() {
        String s = "Individual{" + "x=" + x + ", y=" + y + ", fitness=" + fitness + ", genes=[";
        for (int gene : genes) {
            s += gene + " ";
        }
        s += "']}'";
        return s;
    }

}

class Goal {

    int x;
    int y;

    public Goal(int x, int y) {
        this.x = x;
        this.y = y;
    }

}

class Block {

    int x;
    int y;

    public Block(int x, int y) {
        this.x = x;
        this.y = y;
    }

}

public class GAtest1 extends Application {

    final double CROSSOVER_RATE = 0.5d;
    final int CHROMOSOME_LENGTH = 100;
    final int POPULATION_SIZE = 12;
    final int MUTATION_PERCENTAGE = 10;
    final int NO_OF_ROUNDS = 50;

    final int INIT_X = 100;
    final int INIT_Y = 700;
    final int SLEEP = 50;

    Image craftImage;

    GraphicsContext gc;

    Image appleImage;
    Image treeImage;
    Image stoneImage;
    Image factoryImage;
    Image tileImage;

    double currentX = 200;
    double currentY = 400;
    double movementDelta = 20;
    Integer completion = 0;
    boolean end = false;
    int round = 0;
    List<int[]> genes = new ArrayList<int[]>();

    List<Individual> inidividuals = new ArrayList<>();

    final Goal goal = new Goal(330, 130);

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        Canvas canvas = new Canvas(1024, 768);

        gc = canvas.getGraphicsContext2D();
        File imageFile = new File("robot3.png");
        File appleFile = new File("apple.png");
        File treeFile = new File("tree1.png");
        File stoneFile = new File("Coal-rock.png");
        File factoryFile = new File("center.png");
        File tileFile = new File("tile1.png");

        appleImage = new Image(new FileInputStream(appleFile));
        treeImage = new Image(new FileInputStream(treeFile));
        stoneImage = new Image(new FileInputStream(stoneFile));
        factoryImage = new Image(new FileInputStream(factoryFile));
        tileImage = new Image(new FileInputStream(tileFile));

        StackPane root = new StackPane();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root);

        /*    scene.setOnKeyPressed(e -> {
         switch (e.getCode()) {
         case UP:
         currentY -= movementDelta;
         draw();
         break;
         case DOWN:
         currentY += movementDelta;
         draw();
         break;
         case LEFT:
         currentX -= movementDelta;
         draw();
         break;
         case RIGHT:
         currentX += movementDelta;
         draw();
         break;
         }

         });*/
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println();
        // draw();
        craftImage = new Image(new FileInputStream(imageFile));
        for (int i = 0; i < POPULATION_SIZE; i++) {
            //genes.add(;
            inidividuals.add(new Individual(i * INIT_X, INIT_Y, getRandomlyInitializedChromosome()));
        }
        start();
    }

    private void start() {
        for (int i = 0; i < this.inidividuals.size(); i++) {
            inidividuals.get(i).x = INIT_X;
            inidividuals.get(i).y = INIT_Y;
        }
        completion = 0;
        round++;
        if (round == NO_OF_ROUNDS) {
            System.exit(0);
        }
        System.out.println(round + " ==========================");
        for (Individual inidividual : inidividuals) {
            System.out.println(inidividual);
        }
        for (Individual i : inidividuals) {
            new Thread(() -> {
                move(i);
            }).start();
        }
    }

    private int[] getRandomlyInitializedChromosome() {
        int[] chromosome = new int[CHROMOSOME_LENGTH];
        Random random = new Random();
        for (int i = 0; i < chromosome.length; i++) {
            chromosome[i] = random.nextInt(4) + 1;
            //  System.out.print(chromosome[i] + " ");
        }
        //System.out.println("");
        return chromosome;
    }

    private void move(Individual individual) {
        int[] gene = individual.genes;
        Thread t = new Thread(() -> {
            for (int i : gene) {
                switch (i) {
                    case 1:
                        individual.y -= movementDelta;
                        Platform.runLater(() -> {
                            draw(individual);
                        });

                        break;
                    case 2:
                        individual.y += movementDelta;
                        Platform.runLater(() -> {
                            draw(individual);
                        });
                        break;
                    case 3:
                        individual.x -= movementDelta;
                        Platform.runLater(() -> {
                            draw(individual);
                        });
                        break;
                    case 4:
                        individual.x += movementDelta;
                        Platform.runLater(() -> {
                            draw(individual);
                        });
                        break;
                }
                try {
                    Thread.sleep(SLEEP);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GAtest1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            synchronized (goal) {
                completion++;
                System.out.println("completion" + completion);
                if (completion / inidividuals.size() == 1) {
                    calculateFitness();
                }
            }
        });
        t.start();
    }

    private void draw(Individual individual1) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                gc.drawImage(tileImage, 256 * (j),  256 * i);
            }
        }

        for (int i = 0; i < 5; i++) {
            gc.drawImage(treeImage, 100 * (i + 1), 80, 100, 100);
        }

        for (int i = 0; i < 10; i++) {
            gc.drawImage(stoneImage, 50 + 50 * (i + 1), 300, 50, 50);
        }

        for (int i = 0; i < 10; i++) {
            gc.drawImage(stoneImage, 700, 50 + 50 * (i + 1), 50, 50);
        }

        gc.drawImage(appleImage, goal.x, goal.y, 20, 20);
        gc.drawImage(factoryImage, 800, 400, 160, 100);
        for (Individual inidividual : inidividuals) {
            gc.drawImage(craftImage, inidividual.x, inidividual.y, 50, 50);
        }

    }

    private void calculateFitness() {
        for (Individual inidividual : inidividuals) {
            inidividual.fitness = Math.sqrt(Math.pow(goal.x - inidividual.x, 2) + Math.pow(goal.y - inidividual.y, 2));
            inidividual.fitness = 1/inidividual.fitness ;
            System.out.println(inidividual.fitness);
        }
        crossOver();
    }

    private void crossOver() {
        List<Individual> unSelectedParents = new ArrayList<>();
        List<Individual> newIndividuals = new ArrayList<>();
        unSelectedParents.addAll(inidividuals);
        int crossOverAmount = new Double(inidividuals.size() * CROSSOVER_RATE).intValue() / 2;
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
            int point1 = random.nextInt(CHROMOSOME_LENGTH - 1);
            int point2;
            do {
                point2 = random.nextInt(CHROMOSOME_LENGTH);
                System.out.println("2 >> " + point2 + " >= " + point1);
            } while (point2 < point1);
            System.out.println("3");
            //child1
            int[] newChromosome = new int[CHROMOSOME_LENGTH];
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
            newChromosome = new int[CHROMOSOME_LENGTH];
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
        int remainingPopulationSize = POPULATION_SIZE - newIndividuals.size();
        Collections.shuffle(inidividuals);
        for (int i = 0; i < remainingPopulationSize; i++) {
            inidividuals.get(i).x = 0;
            inidividuals.get(i).y = 0;
            inidividuals.get(i).fitness = 0;
            newPopulation.add(inidividuals.get(i));
        }
        inidividuals = newPopulation;
        mutate();
    }

    private void mutate() {
        int mutationCount = CHROMOSOME_LENGTH * MUTATION_PERCENTAGE / 100;
        Random random = new Random();
        for (Individual inidividual : inidividuals) {
            for (int i = 0; i < mutationCount; i++) {
                int index = random.nextInt(CHROMOSOME_LENGTH);
                inidividual.genes[index] = random.nextInt(4) + 1;
            }
        }
        start();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

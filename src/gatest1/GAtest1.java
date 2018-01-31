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

}

class Goal {

    int x;
    int y;

    public Goal(int x, int y) {
        this.x = x;
        this.y = y;
    }

}

public class GAtest1 extends Application {

    Image craftImage;

    GraphicsContext gc;

    Image appleImage;
    Image treeImage;
    double currentX = 200;
    double currentY = 400;
    double movementDelta = 10;
    Integer completion = 0;
    boolean end = false;
    List<int[]> genes = new ArrayList<int[]>();

    List<Individual> inidividuals = new ArrayList<>();

    final Goal goal = new Goal(300, 130);

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        Canvas canvas = new Canvas(1024, 768);

        gc = canvas.getGraphicsContext2D();
        File imageFile = new File("spaceship.png");
        File appleFile = new File("apple.png");
        File treeFile = new File("tree1.png");

        appleImage = new Image(new FileInputStream(appleFile));
        treeImage = new Image(new FileInputStream(treeFile));

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
        for (int i = 0; i < 10; i++) {
            //genes.add(;
            inidividuals.add(new Individual(i * 10, 400, getRandomlyInitializedGene()));
        }

        for (Individual i : inidividuals) {
            new Thread(() -> {
                move(i);
            }).start();
        }

    }

    private int[] getRandomlyInitializedGene() {
        int[] gene = new int[50];
        Random random = new Random();
        for (int i = 0; i < gene.length; i++) {
            gene[i] = random.nextInt(4) + 1;
            System.out.print(gene[i] + " ");
        }
        System.out.println("");
        return gene;
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
                    Thread.sleep(50);
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
        // gc.
        
        for (int i = 0; i < 5; i++) {
            gc.drawImage(treeImage, 100 * (i + 1), 100, 100, 100);
        }
        gc.drawImage(appleImage, goal.x, goal.y, 20, 20);
        for (Individual inidividual : inidividuals) {
            gc.drawImage(craftImage, inidividual.x, inidividual.y, 50, 50);
        }

    }

    private void calculateFitness() {
        for (Individual inidividual : inidividuals) {
            inidividual.fitness = Math.sqrt(Math.pow(goal.x - inidividual.x, 2) + Math.pow(goal.y - inidividual.y, 2));
            System.out.println(inidividual.fitness);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

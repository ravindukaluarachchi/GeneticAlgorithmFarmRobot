/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gatest1;

import com.sun.javafx.css.FontFace;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author ravindu
 */
public class GAtest1 extends Application {

    double CROSSOVER_RATE = 0.5d;
    int CHROMOSOME_LENGTH = 500;
    int POPULATION_SIZE = 12;
    int MUTATION_PERCENTAGE = 10;
    int NO_OF_ROUNDS = 500;
    String CROSS_OVER_METHOD = "";
    boolean ELITIST = true;

    final int INIT_X = 100;
    final int INIT_Y = 700;
    int SLEEP = 10;

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

    List<Individual> individuals = new ArrayList<>();
    List<Block> blocks = new ArrayList<>();
    Individual solution;
    final Goal goal = new Goal(330, 130);
    final Goal endGoal = new Goal(800, 550);

    List<Thread> movementThreads = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Screen screen3 = Screen.getScreens().get(0);
        Rectangle2D bounds = screen3.getBounds();

        primaryStage.setX(bounds.getMinX() + 100);
        primaryStage.setY(bounds.getMinY() + 100);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/frmStart.fxml"));
        Parent rootConfig = loader.load();

        Scene sceneConfig = new Scene(rootConfig);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        //  scene.getStylesheets().add("/styles/Styles.css");
        primaryStage.setTitle("Enter Init Parameters");
        primaryStage.setScene(sceneConfig);
        primaryStage.show();

        Button btnStart = (Button) loader.getNamespace().get("btnStart");
        /* System.out.println(btnStart);
         System.out.println(scene.lookup("ap"));
         System.out.println("root >> " + loader.getNamespace().get("ap"));*/
        btnStart.setOnAction((e) -> {
            try {

                CheckBox chkElitist = (CheckBox) loader.getNamespace().get("chkElitist");
                ComboBox<String> cmbCrossOver = (ComboBox<String>) loader.getNamespace().get("cmbCrossOver");
                TextField txtPopulationSize = (TextField) loader.getNamespace().get("txtPopulationSize");
                TextField txtNoOfRounds = (TextField) loader.getNamespace().get("txtNoOfRounds");
                TextField txtSpeed = (TextField) loader.getNamespace().get("txtSpeed");
                TextField txtMutationPercentage = (TextField) loader.getNamespace().get("txtMutationPercentage");

                ELITIST = chkElitist.isSelected();
                CROSS_OVER_METHOD = cmbCrossOver.getSelectionModel().getSelectedItem();
                CROSSOVER_RATE = 0.5d;
                CHROMOSOME_LENGTH = 500;
                POPULATION_SIZE = Integer.parseInt(txtPopulationSize.getText());
                MUTATION_PERCENTAGE = Integer.parseInt(txtMutationPercentage.getText());
                NO_OF_ROUNDS = Integer.parseInt(txtNoOfRounds.getText());
                SLEEP = Integer.parseInt(txtSpeed.getText());
                drawApp(primaryStage);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(GAtest1.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        /*Canvas canvas = new Canvas(1024, 768);

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

         for (int i = 0; i < 10; i++) {
         if (i > 3 && i < 6) {
         continue;
         }
         blocks.add(new Block(50 + 50 * (i + 1), 250));

         }
         for (int i = 0; i < 10; i++) {
         if (i < 2 || i > 6) {
         continue;
         }
         blocks.add(new Block(50 + 50 * (i + 1), 400));

         }

         for (int i = 0; i < 14; i++) {
         if (i > 3 && i < 6) {
         continue;
         }
         blocks.add(new Block(300 + 50 * (i + 1), 400));

         }
        
         for (int i = 0; i < 7; i++) {
         if (i > 1 && i < 4) {
         continue;
         }
         blocks.add(new Block(700, 400 + 50 * (i + 1)));

         }

         StackPane root = new StackPane();
         root.getChildren().add(canvas);

         Scene scene = new Scene(root);

         primaryStage.setTitle("Hello World!");
         primaryStage.setScene(scene);
         primaryStage.show();

         System.out.println();
         // draw();
         craftImage = new Image(new FileInputStream(imageFile));
         for (int i = 0; i < POPULATION_SIZE; i++) {
         //genes.add(;
         individuals.add(new Individual(i * INIT_X, INIT_Y, getRandomlyInitializedChromosome()));
         }
         start();*/
    }

    private void drawApp(Stage primaryStage) throws FileNotFoundException {
        System.out.println("hello");
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

        for (int i = 0; i < 10; i++) {
            if (i > 3 && i < 6) {
                continue;
            }
            blocks.add(new Block(50 + 50 * (i + 1), 250));

        }
        for (int i = 0; i < 10; i++) {
            if (i < 2 || i > 6) {
                continue;
            }
            blocks.add(new Block(50 + 50 * (i + 1), 400));

        }

        for (int i = 0; i < 14; i++) {
            if (i > 3 && i < 6) {
                continue;
            }
            blocks.add(new Block(300 + 50 * (i + 1), 400));

        }

        for (int i = 0; i < 7; i++) {
            if (i > 1 && i < 4) {
                continue;
            }
            blocks.add(new Block(700, 400 + 50 * (i + 1)));

        }

        StackPane root = new StackPane();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println();
        // draw();
        craftImage = new Image(new FileInputStream(imageFile));
        for (int i = 0; i < POPULATION_SIZE; i++) {
            //genes.add(;
            individuals.add(new Individual(i * INIT_X, INIT_Y, getRandomlyInitializedChromosome()));
        }
        start();
    }

    private void start() {
        for (int i = 0; i < this.individuals.size(); i++) {
            individuals.get(i).x = INIT_X;
            individuals.get(i).y = INIT_Y;
            individuals.get(i).payload.clear();
        }
        completion = 0;
        round++;
        if (round == NO_OF_ROUNDS) {
            System.exit(0);
        }
        System.out.println(round + " ==========================");
        for (Individual inidividual : individuals) {
            System.out.println(inidividual);
        }
        for (Individual i : individuals) {
            new Thread(() -> {
                move(i);
            }).start();
        }
    }

    private int[] getRandomlyInitializedChromosome() {
        int[] chromosome = new int[CHROMOSOME_LENGTH];
        Random random = new Random();
        for (int i = 0; i < chromosome.length; i++) {
            chromosome[i] = random.nextInt(6) + 1;
            //  System.out.print(chromosome[i] + " ");
        }
        //System.out.println("");
        return chromosome;
    }

    private void move(Individual individual) {
        int[] gene = individual.genes;
        if (solution != null && individual != solution) {
            System.out.println(solution);
            return;
        }
        Thread t = new Thread(() -> {
            for (int i : gene) {
                int originalX = individual.x;
                int originalY = individual.y;
                switch (i) {
                    case 1:
                        individual.y -= movementDelta;
                        if (!validateMove(individual)) {
                            individual.x = originalX;
                            individual.y = originalY;
                        }
                        break;
                    case 2:
                        individual.y += movementDelta;
                        if (!validateMove(individual)) {
                            individual.x = originalX;
                            individual.y = originalY;
                        }
                        break;
                    case 3:
                        individual.x -= movementDelta;
                        if (!validateMove(individual)) {
                            individual.x = originalX;
                            individual.y = originalY;
                        }
                        break;
                    case 4:
                        individual.x += movementDelta;
                        if (!validateMove(individual)) {
                            individual.x = originalX;
                            individual.y = originalY;
                        }
                        break;
                    case 5:
                        if (individual.pick(new Goal[]{goal})) {
                            System.out.println("************************************found goal*******************");
                            //  System.exit(0);
                        }
                        break;
                    case 6:
                        if (individual.payload.size() > 0 && individual.drop(endGoal)) {
                            System.out.println("************************************drop complete*******************");
                            //System.exit(0);
                            if (solution == null) {
                                solution = individual;
                                System.out.println("Best individual ::: ");
                                System.out.println(solution);
                                System.out.println("::::::::::::::::::::::;;");
                                individual.x = INIT_X;
                                individual.y = INIT_Y;
                                move(individual);
                            } else {
                                return;
                            }

                        }
                        break;
                }
                Platform.runLater(() -> {
                    draw(individual);
                });
                try {
                    Thread.sleep(SLEEP);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GAtest1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            synchronized (goal) {
                completion++;
                //  System.out.println("completion" + completion);
                if (completion / individuals.size() == 1) {
                    calculateFitness();
                }
            }
        });
        t.start();
    }

    private void draw(Individual ind) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                gc.drawImage(tileImage, 256 * (j), 256 * i);
            }
        }

        for (int i = 0; i < 5; i++) {
            gc.drawImage(treeImage, 100 * (i + 1), 80, 100, 100);
        }

        /*for (int i = 0; i < 10; i++) {
         gc.drawImage(stoneImage, 50 + 50 * (i + 1), 300, 50, 50);
         }

         for (int i = 0; i < 10; i++) {
         gc.drawImage(stoneImage, 700, 50 + 50 * (i + 1), 50, 50);
         }*/
        for (Block block : blocks) {
            gc.drawImage(stoneImage, block.x, block.y, block.w, block.h);
        }

        gc.drawImage(appleImage, goal.x, goal.y, 20, 20);
        gc.drawImage(factoryImage, endGoal.x, endGoal.y, 160, 100);
        if (solution == null) {

            for (Individual inidividual : individuals) {
                gc.drawImage(craftImage, inidividual.x, inidividual.y, inidividual.w, inidividual.h);
            }

            //draw status
            double consoleX = gc.getCanvas().getWidth() - 250;
            double consoleY = 0;
            double consoleXText = consoleX + 10;
            double consoleYText = consoleY + 50;

            gc.setFill(Color.BLACK);
            gc.fillRect(consoleX, consoleY, 250, 300);
            gc.setStroke(Color.GREEN);
            gc.strokeText("[Console] round : " + round, consoleXText, consoleYText);
            for (int i = 0; i < individuals.size(); i++) {
                //  System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>" );
                //System.out.println( individuals.get(i).fitness);
                String s = String.format("%d | %.8f  %d", i + 1, individuals.get(i).fitness, individuals.get(i).payload.size());
                // gc.strokeText((i + 1) + " | " + new DecimalFormat("#.0000").format(individuals.get(i).fitness) + "  " + individuals.get(i).payload.size(), consoleXText, consoleYText + (20 * (i + 1)));
                gc.strokeText(s, consoleXText, consoleYText + (20 * (i + 1)));
            }

        } else {
            gc.drawImage(craftImage, ind.x, ind.y, 50, 50);
        }
    }

    private void calculateFitness() {
        for (Individual inidividual : individuals) {
            if (inidividual.payload.size() == 0) {
                inidividual.fitness = Math.sqrt(Math.pow(goal.x - inidividual.x, 2) + Math.pow(goal.y - inidividual.y, 2));
                inidividual.fitness = 1 / inidividual.fitness;
            } else {
                inidividual.fitness = Math.sqrt(Math.pow(endGoal.x - inidividual.x, 2) + Math.pow(endGoal.y - inidividual.y, 2));
                inidividual.fitness = 1 / inidividual.fitness;
                inidividual.fitness += inidividual.payload.size();
            }
            System.out.println(inidividual.fitness);
        }
        crossOver();
    }

    private void crossOver() {
        //preserve elitist        
        Individual mostFitted = null;
        if (ELITIST) {

            for (Individual inidividual : individuals) {
                if (mostFitted == null) {
                    mostFitted = inidividual;
                }
                if (mostFitted.fitness < inidividual.fitness) {
                    mostFitted = inidividual;
                }
            }

            //individuals.remove(mostFitted);            
        }
        
        
        CrossOverMethod com = new CrossOverMethod(individuals,individuals.size(), CHROMOSOME_LENGTH, CROSSOVER_RATE, ELITIST);
        switch (CROSS_OVER_METHOD) {
            case "2 Point":
                individuals = com.twoPointCrossOver();
                break;
            case "1 Point":
                break;
            case "Edge":
                break;
        }
        
        //add elitist to the new population
        if (ELITIST && !individuals.contains(mostFitted)) {
            //remove the least fittest individual and add back the most fittest individual
            Individual leastFitted = null;
            for (Individual inidividual : individuals) {
                if (leastFitted == null) {
                    leastFitted = inidividual;
                }
                if (leastFitted.fitness > inidividual.fitness) {
                    leastFitted = inidividual;
                }
            }
            individuals.remove(leastFitted);
            individuals.add(mostFitted);
        }
        mutate(mostFitted);
    }

    private void mutate(Individual mostFitted) {
        int mutationCount = CHROMOSOME_LENGTH * MUTATION_PERCENTAGE / 100;
        Random random = new Random();
        Collections.shuffle(individuals);
        for (int j = 0; j < individuals.size() / 2; j++) {
            Individual individual = individuals.get(j);
            //preserve elitist
            if (individual == mostFitted) {
                continue;
            }
            for (int i = 0; i < mutationCount; i++) {
                int index = random.nextInt(CHROMOSOME_LENGTH);
                individual.genes[index] = random.nextInt(6) + 1;
            }
        }
        start();
    }

    private void runSolution() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    private boolean validateMove(Individual individual) {
        for (Block block : blocks) {
            if ((individual.x >= block.x && individual.x <= block.x + block.w && individual.y >= block.y && individual.y <= block.y + block.h)
                    || (individual.x + individual.w >= block.x && individual.x + individual.w <= block.x + block.w && individual.y + individual.h >= block.y && individual.y + individual.h <= block.y + block.h)) {
                return false;
            }
        }
        return true;
    }
}

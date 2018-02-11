/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gatest1;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ravindu
 */
public class Individual {

    private static final double PICK_RADIUS = 100;
    int x;
    int y;
    int[] genes;
    double fitness;
    List<Goal> payload = new ArrayList<>();

    public Individual(int x, int y, int[] genes) {
        this.x = x;
        this.y = y;
        this.genes = genes;
    }

    public boolean pick(Goal[] goals) {

        for (int i = 0; i < goals.length; i++) {
            Double distance = Math.sqrt(Math.pow(x - goals[i].x, 2) + Math.pow(y - goals[i].y, 2));
            //System.out.println("5:: distance : " + distance);
            if (distance <= PICK_RADIUS && !payload.contains(goals[i])) {
                payload.add(goals[i]);
                return true;
            }
        }
        return false;
    }

    public boolean drop(Goal goal) {

        Double distance = Math.sqrt(Math.pow(x - goal.x, 2) + Math.pow(y - goal.y, 2));
        //System.out.println("5:: distance : " + distance);
        if (distance <= PICK_RADIUS && !payload.contains(goal)) {
            payload.add(goal);
            return true;
        }

        return false;
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

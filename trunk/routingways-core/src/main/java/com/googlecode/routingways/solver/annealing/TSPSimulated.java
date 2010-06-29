/**
 * This file is part of routingways.
 *
 * routingways is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * routingways is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with routingways.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.googlecode.routingways.solver.annealing;

import com.googlecode.routingways.points.Point;
import java.util.Collections;
import java.util.LinkedList;

/**
 *
 * @author thiago
 */
public class TSPSimulated {

    public static final double START_TEMP = 50.0;
    public static final double STOP_TEMP = 2.0;
    public static final int CYCLES = 10;
    public static final int MAX_SAME_SOLUTION = 25;
    private TSPSimulatedAnnealing anneal;
    private Point[] points;

    public void startCities(Point[] point) {
        this.points = point;
    }

    /**
     * Create an initial path of cities.
     */
    private void initPath() {
        LinkedList<Integer> path = new LinkedList<Integer>();
        for (int i = 0; i < points.length - 2; i++) {
            path.add(Integer.valueOf(i + 1));
        }
        Collections.shuffle(path);
        path.addFirst(Integer.valueOf(0));
        path.addLast(Integer.valueOf(points.length - 1));
        Integer[] rtrn = new Integer[path.size()];
        rtrn = path.toArray(rtrn);
        this.anneal.putArray(rtrn);
    }

    public Point[] returnPoints() {
        solve();
        Integer path[] = anneal.getArray();
        Point[] retorno = new Point[path.length];
        for (int i = 0; i < retorno.length; i++) {
            retorno[i] = points[path[i]];
        }

        return retorno;
    }

    /**
     * Setup and solve the TSP.
     */
    public void solve() {
        anneal = new TSPSimulatedAnnealing(points, START_TEMP, STOP_TEMP,
                CYCLES);
        initPath();

        int sameSolutionCount = 0;
        double lastSolution = Double.MAX_VALUE;

        while (sameSolutionCount < MAX_SAME_SOLUTION) {
            anneal.iteration();

            double thisSolution = anneal.calculateScore();

            if (Math.abs(lastSolution - thisSolution) < 1.0) {
                sameSolutionCount++;
            } else {
                sameSolutionCount = 0;
            }

            lastSolution = thisSolution;
        }

    }
}

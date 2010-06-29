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
package com.googlecode.routingways.solver.genetic;

import com.googlecode.routingways.points.Point;

public class TSPGenetic {

    public static final double PERCENT_TO_MATE = 0.25;
    public static final double MATING_POPULATION_PERCENT = 0.5;
    public static final int MAP_SIZE = 256;
    public static final int MAX_SAME_SOLUTION = 25;
    public static final int CITIES = 10;
    private int maxIterations = 0;
    private int population = 0;
    private double mutation = 0;
    private TSPGeneticAlgorithm genetic;
    private Point[] points;

    public TSPGenetic(int maxIterarions, int population, double mutation) {
        this.maxIterations = maxIterarions;
        this.population = population;
        this.mutation = mutation;
    }

    public void startPoints(Point[] points) {
        this.points = points;
    }

    public Point[] returnPoints() {
        solve();
        Integer path[] = genetic.getChromosome(0).getGenes();
        Point[] retorno = new Point[path.length];
        for (int i = 0; i < retorno.length; i++) {
            retorno[i] = points[path[i]];
        }

        return retorno;
    }

    /**
     * Setup and solve the TSP.
     */
    private void solve() {
        genetic = new TSPGeneticAlgorithm(
                points,
                population,
                mutation,
                PERCENT_TO_MATE,
                MATING_POPULATION_PERCENT,
                CITIES / 5);

        int sameSolutionCount = 0;
        while (sameSolutionCount < maxIterations) {
            genetic.iteration();
            sameSolutionCount++;
        }

    }
}

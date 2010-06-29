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
import org.encog.neural.NeuralNetworkError;
import org.encog.solve.genetic.GeneticAlgorithm;

public class TSPGeneticAlgorithm extends GeneticAlgorithm<Integer> {

    public TSPGeneticAlgorithm(final Point[] points, final int populationSize,
            final double mutationPercent, final double percentToMate,
            final double matingPopulationPercent, final int cutLength)
            throws NeuralNetworkError {
        this.setMutationPercent(mutationPercent);
        this.setMatingPopulation(matingPopulationPercent);
        this.setPopulationSize(populationSize);
        this.setPercentToMate(percentToMate);
        this.setCutLength(cutLength);
        this.setPreventRepeat(true);

        setChromosomes(new TSPChromosome[getPopulationSize()]);
        for (int i = 0; i < getChromosomes().length; i++) {

            final TSPChromosome c = new TSPChromosome(this, points);
            setChromosome(i, c);
        }
        sortChromosomes();
    }
}

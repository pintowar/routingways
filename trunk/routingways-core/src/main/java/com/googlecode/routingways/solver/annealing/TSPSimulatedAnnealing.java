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
import com.googlecode.routingways.points.Segment;
import org.encog.solve.anneal.SimulatedAnnealing;

/**
 * TSPSimulatedAnnealing: Implementation of the simulated annealing
 * algorithm that trys to solve the traveling salesman problem.
 *
 * @author $Author$
 * @version $Revision$
 */
public class TSPSimulatedAnnealing extends SimulatedAnnealing<Integer> {

    protected Point[] points;
    protected Integer[] path;

    /**
     * The constructor.
     *
     * @param network
     *            The neural network that is to be trained.
     */
    public TSPSimulatedAnnealing(final Point[] points, final double startTemp,
            final double stopTemp, final int cycles) {

        this.setTemperature(startTemp);
        setStartTemperature(startTemp);
        setStopTemperature(stopTemp);
        setCycles(cycles);

        this.points = points;
        this.path = new Integer[this.points.length];
    }

    @Override
    public double calculateScore() {
        Double cost = 0.0;
        for (int i = 0; i < this.points.length - 1; i++) {
            Segment seg = new Segment(this.points[this.path[i]],
                    this.points[this.path[i + 1]]);
            cost += seg.getLength();
        }
        return cost.doubleValue();
    }

    /**
     * Called to get the distance between two cities.
     *
     * @param i
     *            The first city
     * @param j
     *            The second city
     * @return The distance between the two cities.
     */
    public double distance(final int i, final int j) {
        final int c1 = this.path[i % this.path.length];
        final int c2 = this.path[j % this.path.length];
        Segment seg = new Segment(points[c1], this.points[c2]);
        return seg.getLength();
    }

    @Override
    public Integer[] getArray() {
        return this.path;
    }

    @Override
    public void putArray(final Integer[] array) {
        this.path = array;
    }

    @Override
    public void randomize() {
        final int length = this.path.length;

        for (int i = 0; i < this.getTemperature(); i++) {
            int index1 = 1 + (int) Math.floor(Math.random() * (length - 2));
            int index2 = 1 + (int) Math.floor(Math.random() * (length - 2));
            final double d = distance(index1, index1 + 1) + distance(index2, index2 + 1) - distance(index1, index2) - distance(index1 + 1, index2 + 1);
            if (d > 0) {
                // sort index1 and index2 if needed
                if (index2 < index1) {
                    final int temp = index1;
                    index1 = index2;
                    index2 = temp;
                }
                for (; index2 > index1; index2--) {
                    final int temp = this.path[index1 + 1];
                    this.path[index1 + 1] = this.path[index2];
                    this.path[index2] = temp;
                    index1++;
                }
            }
        }
    }

    @Override
    public Integer[] getArrayCopy() {
        Integer result[] = new Integer[this.path.length];
        System.arraycopy(path, 0, result, 0, path.length);
        return result;
    }
}

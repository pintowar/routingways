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
package com.googlecode.routingways.solver.tabu;

public class TSPGreedyStartSolution extends TspSolution {

    public TSPGreedyStartSolution() {
    } // Appease clone()

    public TSPGreedyStartSolution(double[][] customers) {
        // Greedy neighbor initialize
        int[] avail = new int[customers.length];
        tour = new int[customers.length];
        for (int i = 0; i < avail.length; i++) {
            avail[i] = i;
        }
        for (int i = 1; i < tour.length; i++) {
            int closest = -1;
            double dist = Double.MAX_VALUE;
            for (int j = 1; j < avail.length; j++) {
                if ((norm(customers, tour[i - 1], j) < dist) && (avail[j] >= 0)) {
                    dist = norm(customers, tour[i - 1], j);
                    closest = j;
                }   // end if: new nearest neighbor
            }
            tour[i] = closest;
            avail[closest] = -1;
        }   // end for

        for (int i = 0; i < tour.length; i++) {
            System.out.println(
                    customers[tour[i]][0] + "\t" + customers[tour[i]][1]);
        }

    }   // end constructor

    private double norm(double[][] matr, int a, int b) {
        double xDiff = matr[b][0] - matr[a][0];
        double yDiff = matr[b][1] - matr[a][1];
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }   // end norm
}   // end class MyGreedyStartSolution

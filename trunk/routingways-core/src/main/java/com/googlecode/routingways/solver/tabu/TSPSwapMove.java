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

import org.coinor.opents.Move;
import org.coinor.opents.Solution;

public class TSPSwapMove implements Move {

    public int customer;
    public int movement;

    public TSPSwapMove(int customer, int movement) {
        this.customer = customer;
        this.movement = movement;
    }   // end constructor

    @Override
    public void operateOn(Solution soln) {
        int[] tour = ((TspSolution) soln).tour;
        int pos1 = -1;
        int pos2 = -1;

        // Find positions
        for (int i = 0; i < tour.length && pos1 < 0; i++) {
            if (tour[i] == customer) {
                pos1 = i;
            }
        }
        pos2 = pos1 + movement;

        // Swap
        int cust2 = tour[pos2];
        tour[pos1] = cust2;
        tour[pos2] = customer;
    }   // end operateOn

    /** Identify a move for SimpleTabuList */
    @Override
    public int hashCode() {
        return customer;
    }   // end hashCode
}   // end class MySwapMove


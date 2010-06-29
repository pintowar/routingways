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

import org.coinor.opents.SolutionAdapter;


public class TspSolution extends SolutionAdapter {

    public int[] tour;

    public TspSolution() {
    } // Appease clone()

    public TspSolution(double[][] customers) {
        // Crudely initialize solution
        tour = new int[customers.length];
        for (int i = 0; i < customers.length; i++) {
            tour[i] = i;
        }
    }   // end constructor

    @Override
    public Object clone() {
        TspSolution copy = (TspSolution) super.clone();
        copy.tour = (int[]) this.tour.clone();
        return copy;
    }   // end clone

    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();

        s.append("Solution value: " + getObjectiveValue()[0]);
        s.append("Sequence: [ ");

        for (int i = 0; i < tour.length - 1; i++) {
            s.append(tour[i]).append(", ");
        }

        s.append(tour[tour.length - 1]);
        s.append(" ]");

        return s.toString();
    }   // end toString
}   // end class MySolution

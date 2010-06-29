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
import org.coinor.opents.ObjectiveFunction;
import org.coinor.opents.Solution;

public class TspObjectiveFunction implements ObjectiveFunction {

    public double[][] matrix;

    public TspObjectiveFunction(double[][] customers) {
        matrix = createMatrix(customers);
    }   // end constructor

    @Override
    public double[] evaluate(Solution solution, Move move) {
        int[] tour = ((TspSolution) solution).tour;
        int len = tour.length;

        // If move is null, calculate distance from scratch
        if (move == null) {
            double dist = 0;
            for (int i = 0; i < len; i++) {
                dist += matrix[tour[i]][i + 1 >= len ? 0 : tour[i + 1]];
            }

            return new double[]{dist};
        } // end if: move == null
        // Else calculate incrementally
        else {
            TSPSwapMove mv = (TSPSwapMove) move;
            int pos1 = -1;
            int pos2 = -1;

            // Find positions
            for (int i = 0; i < tour.length; i++) {
                if (tour[i] == mv.customer) {
                    pos1 = i;
                    break;
                }
            }
            pos2 = pos1 + mv.movement;

            // Logic below requires pos1 < pos2
            if (pos1 > pos2) {
                int temp = pos2;
                pos2 = pos1;
                pos1 = temp;
            }   // end if

            // Prior objective value
            double dist = solution.getObjectiveValue()[0];

            // Treat a pair swap move differently
            if (pos1 + 1 == pos2) {
                //     | |
                // A-B-C-D-E: swap C and D, say (works for symmetric matrix only)
                dist -= matrix[tour[pos1 - 1]][tour[pos1]];           // -BC
                dist -= matrix[tour[pos2]][tour[(pos2 + 1) % len]];   // -DE
                dist += matrix[tour[pos1 - 1]][tour[pos2]];           // +BD
                dist += matrix[tour[pos1]][tour[(pos2 + 1) % len]];   // +CE
                return new double[]{dist};
            } // end if: pair swap
            // Else the swap is separated by at least one customer
            else {
                //   |     |
                // A-B-C-D-E-F: swap B and E, say
                dist -= matrix[tour[pos1 - 1]][tour[pos1]];           // -AB
                dist -= matrix[tour[pos1]][tour[pos1 + 1]];         // -BC
                dist -= matrix[tour[pos2 - 1]][tour[pos2]];           // -DE
                dist -= matrix[tour[pos2]][tour[(pos2 + 1) % len]];   // -EF

                dist += matrix[tour[pos1 - 1]][tour[pos2]];           // +AE
                dist += matrix[tour[pos2]][tour[pos1 + 1]];         // +EC
                dist += matrix[tour[pos2 - 1]][tour[pos1]];           // +DB
                dist += matrix[tour[pos1]][tour[(pos2 + 1) % len]];   // +BF
                return new double[]{dist};
            }   // end else: not a pair swap
        }   // end else: calculate incremental
    }   // end evaluate

    /** Create symmetric matrix. */
    private double[][] createMatrix(double[][] customers) {
        int len = customers.length;
        double[][] mtrx = new double[len][len];

        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                mtrx[i][j] = mtrx[j][i] = norm(
                        customers[i][0], customers[i][1],
                        customers[j][0], customers[j][1]);
            }
        }
        return mtrx;
    }   // end createMatrix

    /** Calculate distance between two points. */
    private double norm(double x1, double y1, double x2, double y2) {
        double xDiff = x2 - x1;
        double yDiff = y2 - y1;
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }   // end norm
}   // end class MyObjectiveFunction


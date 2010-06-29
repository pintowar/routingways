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
import org.coinor.opents.MoveManager;
import org.coinor.opents.Solution;

public class TspMoveManager implements MoveManager {

    @Override
    public Move[] getAllMoves(Solution solution) {
        int[] tour = ((TspSolution) solution).tour;
        Move[] buffer = new Move[tour.length * tour.length];
        int nextBufferPos = 0;

        // Generate moves that move each customer
        // forward and back up to five spaces.
        for (int i = 1; i < tour.length - 1; i++) {
            for (int j = -5; j <= 5; j++) {
                if ((i + j >= 1) && (i + j < tour.length - 1) && (j != 0)) {
                    buffer[nextBufferPos++] = new TSPSwapMove(tour[i], j);
                }
            }
        }

        // Trim buffer
        Move[] moves = new Move[nextBufferPos];
        System.arraycopy(buffer, 0, moves, 0, nextBufferPos);

        return moves;
    }   // end getAllMoves
}   // end class MyMoveManager


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

import com.googlecode.routingways.points.Point;
import com.googlecode.routingways.points.Route;
import com.googlecode.routingways.points.Segment;
import com.googlecode.routingways.solver.BaseSolver;
import com.googlecode.routingways.solver.OptmizeException;
import org.coinor.opents.BestEverAspirationCriteria;
import org.coinor.opents.MoveManager;
import org.coinor.opents.ObjectiveFunction;
import org.coinor.opents.SimpleTabuList;
import org.coinor.opents.SingleThreadedTabuSearch;
import org.coinor.opents.TabuList;
import org.coinor.opents.TabuSearch;

/**
 *
 * @author thiago
 */
public class TabuSolver extends BaseSolver {

    @Override
    public Route optmizeRoute(Point first, Point last, Point... others) throws OptmizeException {
        Point[] aux = new Point[others.length + 2];
        aux[0] = first;
        aux[aux.length - 1] = last;
        for (int i = 1; i < aux.length - 1; i++) {
            aux[i] = others[i - 1];
        }

        double[][] customers = new double[aux.length][2];
        for (int i = 0; i < aux.length; i++) {
            Point tmp = aux[i];
            customers[i][0] = tmp.getX().doubleValue();
            customers[i][1] = tmp.getY().doubleValue();
        }
        ObjectiveFunction objFunc = new TspObjectiveFunction(customers);
        TspSolution initialSolution = new TspSolution(customers);
        MoveManager moveManager = new TspMoveManager();
        TabuList tabuList = new SimpleTabuList(7); // In OpenTS package

        // Create Tabu Search object
        TabuSearch tabuSearch = new SingleThreadedTabuSearch(
                initialSolution,
                moveManager,
                objFunc,
                tabuList,
                new BestEverAspirationCriteria(), // In OpenTS package
                false); // maximizing = yes/no; false means minimizing

        // Start solving
        tabuSearch.setIterationsToGo(100);
        tabuSearch.startSolving();

        // Show solution
        TspSolution best = (TspSolution) tabuSearch.getBestSolution();

        int[] tour = best.tour;

        Route rota = new Route();
        for (int i = 0; i < aux.length - 1; i++) {
            rota.add(new Segment(aux[tour[i]], aux[tour[i + 1]]));
        }
        return rota;
    }
}

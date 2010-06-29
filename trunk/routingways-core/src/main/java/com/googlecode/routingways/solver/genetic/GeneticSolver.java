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
import com.googlecode.routingways.points.Route;
import com.googlecode.routingways.points.Segment;
import com.googlecode.routingways.solver.BaseSolver;
import com.googlecode.routingways.solver.OptmizeException;

/**
 *
 * @author thiago
 */
public class GeneticSolver extends BaseSolver {

    private TSPGenetic solveTSP = null;

    public GeneticSolver(int evolution, int population, double rate) {
        solveTSP = new TSPGenetic(evolution, population, rate);
    }

    @Override
    public Route optmizeRoute(Point first, Point last, Point... others) throws OptmizeException {
        validate(others);
        Point[] aux = new Point[others.length + 2];
        aux[0] = first;
        aux[aux.length - 1] = last;
        for (int i = 1; i < aux.length - 1; i++) {
            aux[i] = others[i - 1];
        }
        solveTSP.startPoints(aux);
        Point[] points = solveTSP.returnPoints();
        Route route = new Route();
        for (int i = 0; i < points.length - 1; i++) {
            route.add(new Segment(points[i], points[i + 1]));
        }
        return route;
    }
}

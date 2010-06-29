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
package com.googlecode.routingways.solver.brute;

import com.googlecode.routingways.points.Point;
import com.googlecode.routingways.points.Route;
import com.googlecode.routingways.points.Segment;
import com.googlecode.routingways.solver.BaseSolver;
import com.googlecode.routingways.solver.OptmizeException;

/**
 *
 * @author thiago
 */
public class BruteSolver extends BaseSolver {

    @Override
    public Route optmizeRoute(Point first, Point last, Point... others) throws OptmizeException {
        validate(others);
        int[] indexes = null;
        Route min = null;
        PermutationGenerator pg = new PermutationGenerator(others.length);
        Point a = null;
        Point b = null;
        while (pg.hasMore()) {
            Route rota = new Route();
            indexes = pg.getNext();
            for (int i = 0; i < indexes.length; i++) {
                if (i == 0) {
                    a = others[indexes[i]];
                    rota.add(new Segment(first, a));
                } else if (i > 0) {
                    a = others[indexes[i - 1]];
                    b = others[indexes[i]];
                    rota.add(new Segment(a, b));
                }
            }
            rota.add(new Segment(b, last));
            if (min == null) {
                min = rota;
            } else if (rota.totalDistance().compareTo(min.totalDistance()) <= 0) {
                min = rota;
            }
        }

        return min;
    }
}

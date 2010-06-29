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
package com.googlecode.routingways.solver;

import com.googlecode.routingways.persist.Points;
import com.googlecode.routingways.points.Point;
import com.googlecode.routingways.points.Route;


/**
 *
 * @author thiago
 */
public interface Solver {

    public Route optmizeRoute(Point first, Point last, Point... others) throws OptmizeException;
    
    public Route optmizeRoute(Points points) throws OptmizeException;
}

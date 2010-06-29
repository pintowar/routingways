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
package com.googlecode.routingways.points;

/**
 *
 * @author thiago
 */
public class PythagorasDistance implements DistanceHeuristic {

    @Override
    public Double calculateDistance(Point a, Point b) {
        Double dX = a.getX() - b.getX();
        Double dY = a.getY() - b.getY();

        Double sum = Math.pow(dX, 2) + Math.pow(dY, 2);
        return Math.sqrt(sum);
    }
}

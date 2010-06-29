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

import java.io.Serializable;

/**
 *
 * @author thiago
 */
public class Point implements Comparable<Point>, Serializable {

    private final Double x;
    private final Double y;
    private String name = "P";

    public Point() {
        this.x = 0.0;
        this.y = 0.0;
    }

    public Point(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Point(String name, Double x, Double y) {
        this(x, y);
        this.name = name;
    }

    public Point(String x, String y) {
        this.x = Double.valueOf(x);
        this.y = Double.valueOf(y);
    }

    public Point(String name, String x, String y) {
        this(x, y);
        this.name = name;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public Point reversePoint() {
        return new Point(this.name, this.y, this.x);
    }

    @Override
    public int compareTo(Point other) {
        int res = this.x.compareTo(other.x);
        if (res == 0) {
            res = this.y.compareTo(other.y);
        }

        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point other = (Point) obj;
        if (this.x != other.x && (this.x == null || !this.x.equals(other.x))) {
            return false;
        }
        if (this.y != other.y && (this.y == null || !this.y.equals(other.y))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name);
        sb.append('(').append(x).append(',').append(y).append(')');
        return sb.toString();
    }
}

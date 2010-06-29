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
public class Segment implements Comparable<Segment>, Serializable {

    private final Point first;
    private final Point last;
    private final Double length;

    public Segment(Point first, Point last, DistanceHeuristic heuristic) {
        this.first = first;
        this.last = last;
        this.length = heuristic.calculateDistance(first, last);
    }

    public Segment(Point first, Point last) {
        this(first, last, new PythagorasDistance());
    }

    public Point getFirst() {
        return first;
    }

    public Point getLast() {
        return last;
    }

    public Double getLength() {
        return length;
    }

    public boolean isFirstPoint(Point point) {
        return first.equals(point);
    }

    public boolean isLastPoint(Point point) {
        return last.equals(point);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Segment other = (Segment) obj;
        if (this.first != other.first && (this.first == null || !this.first.equals(other.first))) {
            return false;
        }
        if (this.last != other.last && (this.last == null || !this.last.equals(other.last))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (this.first != null ? this.first.hashCode() : 0);
        hash = 67 * hash + (this.last != null ? this.last.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(').append(first.getName()).append(" -> ").
                append(last.getName()).append(')');
        return sb.toString();
    }

    @Override
    public int compareTo(Segment other) {
        int result = first.getName().compareTo(other.first.getName());
        if (result == 0) {
            result = last.getName().compareTo(other.last.getName());
        }
        return result;
    }
}

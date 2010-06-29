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
package com.googlecode.routingways.persist;

import com.googlecode.routingways.points.Point;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author thiago
 */
public class Points implements Serializable {

    private Point first;
    private Point last;
    private List<Point> others = new LinkedList<Point>();

    public Points() {
    }

    public Points(Point first, Point last, List<Point> others) {
        this.first = first;
        this.last = last;
        this.others.addAll(others);
    }

    public Point getFirst() {
        return first;
    }

    public Point getLast() {
        return last;
    }

    public List<Point> getOthers() {
        return others;
    }

    public void setFirst(Point first) {
        this.first = first;
    }

    public void setLast(Point last) {
        this.last = last;
    }

    public void setOthers(List<Point> others) {
        this.others = others;
    }

    public int totalPoints() {
        return others.size() + 2;
    }
}

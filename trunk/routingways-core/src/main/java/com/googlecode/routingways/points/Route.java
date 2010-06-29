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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author thiago
 */
public class Route implements Serializable {

    private final Set<Segment> segments = new LinkedHashSet<Segment>();

    public Route() {
    }

    public void add(Segment segment) {
        segments.add(segment);
    }

    public List<Segment> getSegments() {
        return new ArrayList<Segment>(segments);
    }

    public Double totalDistance() {
        Double sum = 0.0;
        for (Segment segmento : segments) {
            sum = sum + segmento.getLength();
        }

        return sum;
    }

    public Iterator<Segment> iterator() {
        return segments.iterator();
    }

    public int qtSegments() {
        return segments.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Distancia total: ").append(totalDistance()).append("\n");
        sb.append("Caminho percorrido: ").append("\n");
        for (Segment segment : segments) {
            sb.append(segment.toString()).append(" - ").
                    append(segment.getLength()).append("\n");
        }
        return sb.toString();
    }
}

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
import com.thoughtworks.xstream.XStream;
import java.io.Reader;
import java.io.Writer;

/**
 *
 * @author thiago
 */
public class XMLSerializing implements Serializing {

    private XStream stream = new XStream();

    public XMLSerializing() {
        stream.alias("points", Points.class);
        //stream.addImplicitCollection(Point.class, "others");
        stream.alias("point", Point.class);
    }

    @Override
    public void marshall(Points points, Writer writer) {
        stream.toXML(points, writer);
    }

    @Override
    public void unmarshall(Points points, Reader reader) {
        stream.fromXML(reader, points);
    }
}

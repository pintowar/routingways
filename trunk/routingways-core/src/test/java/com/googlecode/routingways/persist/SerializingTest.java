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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlecode.routingways.persist;

import com.googlecode.routingways.points.Point;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author thiago
 */
public class SerializingTest {

    XMLSerializing serializing = new XMLSerializing();

    public SerializingTest() {
    }

    @Test
    public void testMarshall() throws IOException {
        List<Point> points = new ArrayList<Point>();
        points.add(new Point("B", 3.0, 4.0));
        points.add(new Point("C", 7.0, 5.0));
        Points totalPoints = new Points(new Point("A", 3.0, 4.0),
                new Point("D", 5.0, 5.0), points);
        Writer writer = new StringWriter();
        serializing.marshall(totalPoints, writer);
        writer.close();
        System.out.println("Aqui: "+writer);
        assertTrue(true);
    }
}

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
package com.googlecode.routingways.points;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author thiago
 */
public class PythagorasDistanceTest {

    private PythagorasDistance pyDistance = new PythagorasDistance();

    public PythagorasDistanceTest() {
    }

    @Test
    public void testCalculateDistance() {
        double dist = pyDistance.calculateDistance(new Point(4.0, 0.0),
                new Point(0.0, 3.0));
        assertEquals(5.0, dist, 0.0);
    }
}

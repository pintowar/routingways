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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author thiago
 */
public class RouteTest {

    private Route route;

    public RouteTest() {
    }

    @Before
    public void setUp() {
        route = new Route();
        route.add(new Segment(new Point(4.0, 0.0), new Point(0.0, 3.0)));
        route.add(new Segment(new Point(0.0, 4.0), new Point(3.0, 0.0)));
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of totalDistance method, of class Route.
     */
    @Test
    public void testTotalDistance() {
        double expResult = 10.0;
        double result = route.totalDistance();
        assertEquals(expResult, result, 0.0);
    }
}

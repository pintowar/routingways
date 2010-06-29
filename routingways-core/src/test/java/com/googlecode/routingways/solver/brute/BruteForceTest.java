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
package com.googlecode.routingways.solver.brute;

import com.googlecode.routingways.points.Point;
import com.googlecode.routingways.points.Route;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author thiago
 */
public class BruteForceTest {

    private Point a = null;
    private Point b = null;
    private Point c = null;
    private Point d = null;

    public BruteForceTest() {
    }

    @Before
    public void setUp() {
        a = new Point("A", 0.0, 0.0);
        b = new Point("B", 1.0, 1.0);
        c = new Point("C", 2.0, 2.0);
        d = new Point("D", 3.0, 3.0);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of optmizeRoute method, of class BruteForce.
     */
    @Test
    public void testOptmizeRoute() throws Exception {
        BruteSolver bf = new BruteSolver();
        Point[] others = {c, b};
        Route result = bf.optmizeRoute(a, d, others);
        assertEquals(a, result.getSegments().get(0).getFirst());
        assertEquals(b, result.getSegments().get(0).getLast());
        assertEquals(Math.sqrt(2) * 3, result.totalDistance(), 0.0);
    }
}

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
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author thiago
 */
public class SegmentTest {

    private Segment segment;

    public SegmentTest() {
    }

    @Before
    public void setUp() {
        segment = new Segment(new Point(3.5, 3.0), new Point(2.5, 5.0));
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getFirst method, of class Segment.
     */
    @Test
    public void testGetFirst() {
        assertEquals(3.5, segment.getFirst().getX(), 0.0);
        assertEquals(3.0, segment.getFirst().getY(), 0.0);
    }

    /**
     * Test of getLast method, of class Segment.
     */
    @Test
    public void testGetLast() {
        assertEquals(2.5, segment.getLast().getX(), 0.0);
        assertEquals(5.0, segment.getLast().getY(), 0.0);
    }

    /**
     * Test of isFirstPoint method, of class Segment.
     */
    @Test
    public void testIsFirstPoint() {
        assertTrue(segment.isFirstPoint(new Point(3.5, 3.0)));
    }

    /**
     * Test of isLastPoint method, of class Segment.
     */
    @Test
    public void testIsLastPoint() {
        assertTrue(segment.isLastPoint(new Point(2.5, 5.0)));
    }
}

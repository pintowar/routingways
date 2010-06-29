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

import java.util.Arrays;
import java.math.BigInteger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author thiago
 */
public class PermutationGeneratorTest {

    private PermutationGenerator pg = null;

    public PermutationGeneratorTest() {
    }

    @Before
    public void setUp() {
        pg = new PermutationGenerator(5);
    }

    @After
    public void tearDown() {
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor() {
        pg = new PermutationGenerator(0);
    }

    /**
     * Test of getNumLeft method, of class PermutationGenerator.
     */
    @Test
    public void testGetNumLeft() {
        assertEquals(BigInteger.valueOf(120), pg.getNumLeft());
    }

    /**
     * Test of getTotal method, of class PermutationGenerator.
     */
    @Test
    public void testGetTotal() {
        assertEquals(BigInteger.valueOf(120), pg.getTotal());
    }

    /**
     * Test of hasMore method, of class PermutationGenerator.
     */
    @Test
    public void testHasMore() {
        assertTrue(pg.hasMore());
    }

    /**
     * Test of getNext method, of class PermutationGenerator.
     */
    @Test
    public void testGetNext() {
        int[] a = pg.getNext();
        assertEquals(5, a.length);
        assertEquals(BigInteger.valueOf(120), pg.getTotal());
        assertEquals(BigInteger.valueOf(119), pg.getNumLeft());
    }
}

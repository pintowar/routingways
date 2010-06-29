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
package com.googlecode.routingways.solver.simplex;

import com.googlecode.routingways.points.Point;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Modelo de base para o teste:
 *
 * min: 121Sa_b + 236.86Sa_c + 197.21Sb_c + 244.36Sb_d + 197.21Sc_b + 142.23Sc_d;
 * Asai: Sa_b + Sa_c = 1;
 * Bent: Sa_b + Sc_b = 1;
 * Bsai: Sb_d + Sb_c = 1;
 * Cent: Sb_c + Sa_c = 1;
 * Csai: Sc_d + Sc_b = 1;
 * Dent: Sb_d + Sc_d = 1;
 *
 * Ub - Uc + 2Sb_c < 1;
 * Uc - Ub + 2Sc_b < 1;
 *
 * int Sa_b, Sa_c, Sb_c, Sb_d, Sc_b, Sc_d;
 * @author thiago
 */
public class SimplexGenerator4PtsImplTest {

    private SimplexGeneratorImpl generator;

    public SimplexGenerator4PtsImplTest() {
        List<Point> pontos = new ArrayList<Point>();
        pontos.add(new Point("A", 120.0, 126.0));
        pontos.add(new Point("B", 121.0, 24.0));
        pontos.add(new Point("C", 318.0, 256.0));
        pontos.add(new Point("D", 326.0, 114.0));
        generator = new SimplexGeneratorImpl(pontos);
    }

    @Test
    public void testObjFunction() {
        //double[] expected = new double[]{121, 236.86, 197.21, 244.36, 197.21, 142.23, 0, 0};
        double[] expected = new double[]{102.00490184299969, 236.86282950264695, 304.35669862843497, 223.8861317723811, 304.35669862843497, 142.22517358048822, 0.0, 0.0};
        double[] current = generator.objFunction();
        assertTrue(Arrays.equals(expected, current));
    }

    @Test
    public void testMatrix() {
        double[][] expected = new double[][]{
            {1, 1, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 1, 1, 0, 0, 0, 0},
            {0, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 0, 0},
            {0, 0, 0, 1, 0, 1, 0, 0},
            {0, 0, 2, 0, 0, 0, 1, -1},
            {0, 0, 0, 0, 2, 0, -1, 1}
        };
        double[][] current = generator.matrix();
        assertTrue(Arrays.equals(expected[0], current[0]));
        assertTrue(Arrays.equals(expected[1], current[1]));
        assertTrue(Arrays.equals(expected[2], current[2]));
        assertTrue(Arrays.equals(expected[3], current[3]));
        assertTrue(Arrays.equals(expected[4], current[4]));
        assertTrue(Arrays.equals(expected[5], current[5]));
        assertTrue(Arrays.equals(expected[6], current[6]));
        assertTrue(Arrays.equals(expected[7], current[7]));
    }

    @Test
    public void testRightSide() {
        double[] expected = new double[]{1, 1, 1, 1, 1, 1, 1, 1};
        double[] current = generator.rightSide();
        assertTrue(Arrays.equals(expected, current));
    }

    /*
     * -1 representa o sinal <=
     * 0 representa o sinal =
     * 1 representa o sinal >=
     */
    @Test
    public void testEqualities() {
        double[] expected = new double[]{0, 0, 0, 0, 0, 0, -1, -1};
        double[] current = generator.equalities();
        assertTrue(Arrays.equals(expected, current));
    }

    @Test
    public void testLinhaSaida() {
        double[] expected = new double[]{1, 1, 0, 0, 0, 0, 0, 0};
        double[] current = generator.lastLine(new double[]{0, 1, 1, 0}, expected.length, 0);
        assertTrue(Arrays.equals(expected, current));
        expected = new double[]{0, 0, 1, 1, 0, 0, 0, 0};
        current = generator.lastLine(new double[]{0, 0, 1, 1}, expected.length, 1);
        assertTrue(Arrays.equals(expected, current));
        expected = new double[]{0, 0, 0, 0, 1, 1, 0, 0};
        current = generator.lastLine(new double[]{0, 1, 0, 1}, expected.length, 2);
        assertTrue(Arrays.equals(expected, current));
    }

    @Test
    public void testLinhaEntrada() {
        double[] expected = new double[]{0, 0, 0, 1, 0, 1, 0, 0};
        double[] current = generator.firstLine(new double[]{0, 1, 1, 0}, expected.length, 3);
        assertTrue(Arrays.equals(expected, current));
        expected = new double[]{0, 1, 1, 0, 0, 0, 0, 0};
        current = generator.firstLine(new double[]{1, 1, 0, 0}, expected.length, 2);
        assertTrue(Arrays.equals(expected, current));
    }
}

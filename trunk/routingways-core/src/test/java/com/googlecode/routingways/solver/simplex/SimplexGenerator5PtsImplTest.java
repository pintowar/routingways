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
 * min: 115.04Sa_b + 221.55Sa_c + 227.61Sa_d + 130.6Sb_c + 194Sb_d + 226.89Sb_e + 130.6Sc_b + 120.42Sc_d + 215.19Sc_e + 194Sd_b + 120.42Sd_c + 115Sd_e;
 * Asai: Sa_b + Sa_c + Sa_d = 1;
 * Bent: Sa_b + Sc_b + Sd_b = 1;
 * Bsai: Sb_e + Sb_c + Sb_d = 1;
 * Cent: Sb_c + Sa_c + Sd_c = 1;
 * Csai: Sc_e + Sc_b + Sc_d = 1;
 * Dent: Sb_d + Sc_d + Sa_d = 1;
 * Dsai: Sd_e + Sd_b + Sd_c = 1;
 * Eent: Sb_e + Sc_e + Sd_e = 1;
 *
 * Ub - Uc + 3Sb_c < 2;
 * Ub - Ud + 3Sb_d < 2;
 * Uc - Ub + 3Sc_b < 2;
 * Uc - Ud + 3Sc_d < 2;
 * Ud - Ub + 3Sd_b < 2;
 * Ud - Uc + 3Sd_c < 2;
 *
 *int Sc_b, Sb_d, Sb_e, Sc_e, Sb_c, Sc_d, Sd_e, Sa_c, Sa_d, Sd_c, Sd_b, Sa_b;
 * @author thiago
 */
public class SimplexGenerator5PtsImplTest {

    private SimplexGeneratorImpl generator;

    public SimplexGenerator5PtsImplTest() {
        List<Point> pontos = new ArrayList<Point>();
        pontos.add(new Point("A", 70.0, 80.0));
        pontos.add(new Point("B", 73.0, 19.0));
        pontos.add(new Point("C", 177.0, 274.0));
        pontos.add(new Point("D", 267.0, 194.0));
        pontos.add(new Point("E", 268.0, 79.0));
        generator = new SimplexGeneratorImpl(pontos);
    }

    //@Test
    public void testObjFunction() {
        double[] expected = new double[]{115.04, 221.55, 227.61, 130.6, 194, 226.89, 130.6, 120.42, 215.19, 194, 120.42, 115, 0, 0, 0};
        double[] current = generator.objFunction();
        assertTrue(Arrays.equals(expected, current));
    }

    @Test
    public void testMatrix() {
        double[][] expected = new double[][]{
            {1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 0},
            {0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 1, 0, -1},
            {0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, -1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 1, -1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, -1, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, -1, 1}
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
        assertTrue(Arrays.equals(expected[8], current[8]));
        assertTrue(Arrays.equals(expected[9], current[9]));
        assertTrue(Arrays.equals(expected[10], current[10]));
        assertTrue(Arrays.equals(expected[11], current[11]));
        assertTrue(Arrays.equals(expected[12], current[12]));
        assertTrue(Arrays.equals(expected[13], current[13]));
    }

    @Test
    public void testRightSide() {
        double[] expected = new double[]{1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2};
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
        double[] expected = new double[]{0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1};
        double[] current = generator.equalities();
        assertTrue(Arrays.equals(expected, current));
    }
}

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
package com.googlecode.routingways.solver.genetic;

import com.googlecode.routingways.points.Point;
import junit.framework.TestCase;

/**
 *
 * @author thiago
 */
public class TSPChromosomeTest extends TestCase {

    private TSPChromosome chromosome;

    public TSPChromosomeTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testRejustar() {
        Point[] pontos = new Point[]{new Point(1.0, 2.0), new Point(2.0, 1.0),
            new Point(3.0, 4.0), new Point(4.0, 3.0)};
        chromosome = new TSPChromosome(null, pontos);
        TSPChromosome chromosome1 = new TSPChromosome(null, pontos);
        TSPChromosome chromosome2 = new TSPChromosome(null, pontos);

        chromosome1.setGenes(new Integer[]{2, 0, 1, 3});
        chromosome2.setGenes(new Integer[]{0, 1, 2, 3});
        chromosome.readjust(chromosome, chromosome);
        assertEquals(Integer.valueOf(0), chromosome.getGene(0));
        //assertEquals(Integer.valueOf(0), chromosome2.getGene(0));
    }
}

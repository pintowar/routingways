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
package com.googlecode.routingways.solver.genetic;

import com.googlecode.routingways.points.Point;
import com.googlecode.routingways.points.Segment;
import org.encog.solve.genetic.Chromosome;
import java.util.HashSet;
import java.util.Set;

public class TSPChromosome extends Chromosome<Integer> {

    protected Point points[];

    public TSPChromosome(final TSPGeneticAlgorithm owner, final Point points[]) {
        this.setGeneticAlgorithm(owner);
        this.points = points;

        final Integer genes[] = new Integer[this.points.length];
        final boolean taken[] = new boolean[points.length];

        for (int i = 0; i < genes.length; i++) {
            taken[i] = false;
        }
        for (int i = 0; i < genes.length - 1; i++) {
            int icandidate;
            do {
                icandidate = (int) (Math.random() * genes.length);
            } while (taken[icandidate]);
            genes[i] = icandidate;
            taken[icandidate] = true;
            if (i == genes.length - 2) {
                icandidate = 0;
                while (taken[icandidate]) {
                    icandidate++;
                }
                genes[i + 1] = icandidate;
            }
        }
        setGenes(genes);
        readjust(this);

    }

    @Override
    public void calculateScore() {
        Double cost = 0.0;
        for (int i = 0; i < this.points.length - 1; i++) {
            Segment seg = new Segment(this.points[i],
                    this.points[i + 1]);
            cost += seg.getLength();
        }
        setScore(cost);
    }

    @Override
    public void mate(Chromosome<Integer> father, Chromosome<Integer> offspring1, Chromosome<Integer> offspring2) {
        final int geneLength = getGenes().length;
        int interval = getGeneticAlgorithm().getCutLength();
        // the chromosome must be cut at two positions, determine them
        final int cutpoint1 = (int) (Math.random() * (geneLength - interval));
        final int cutpoint2 = cutpoint1 + interval;

        // keep track of which cities have been taken in each of the two
        // offspring, defaults to false.
        final Set<Integer> taken1 = new HashSet<Integer>();
        final Set<Integer> taken2 = new HashSet<Integer>();

        // handle cut section
        for (int i = 0; i < geneLength; i++) {
            if (!(i < cutpoint1 || i > cutpoint2)) {
                offspring1.setGene(i, father.getGene(i));
                offspring2.setGene(i, this.getGene(i));
                taken1.add(offspring1.getGene(i));
                taken2.add(offspring2.getGene(i));
            }
        }

        // handle outer sections
        for (int i = 0; i < geneLength; i++) {
            if (i < cutpoint1 || i > cutpoint2) {
                if (getGeneticAlgorithm().isPreventRepeat()) {
                    offspring1.setGene(i, getNotTaken(this, taken1));
                    offspring2.setGene(i, getNotTaken(father, taken2));
                } else {
                    offspring1.setGene(i, this.getGene(i));
                    offspring2.setGene(i, father.getGene(i));
                }
            }
        }
        readjust(offspring1, offspring2);

        // copy results
        offspring1.calculateScore();
        offspring2.calculateScore();

        if (Math.random() < this.getGeneticAlgorithm().getMutationPercent()) {
            offspring1.mutate();
        }
        if (Math.random() < this.getGeneticAlgorithm().getMutationPercent()) {
            offspring2.mutate();
        }
    }

    private Integer getNotTaken(final Chromosome<Integer> source,
            final Set<Integer> taken) {
        final int geneLength = source.getGenes().length;

        for (int i = 0; i < geneLength; i++) {
            final Integer trial = source.getGene(i);
            if (!taken.contains(trial)) {
                taken.add(trial);
                return trial;
            }
        }

        return null;
    }

    @Override
    public void mutate() {
        final int length = this.getGenes().length;
        final int iswap1 = 1 + (int) (Math.random() * (length - 2));
        final int iswap2 = 1 + (int) (Math.random() * (length - 2));
        final Integer temp = getGene(iswap1);
        setGene(iswap1, getGene(iswap2));
        setGene(iswap2, temp);
    }

    void readjust(Chromosome<Integer> chromosome1, Chromosome<Integer> chromosome2) {
        final int geneLength = getGenes().length;
        for (int i = 0; i < geneLength; i++) {
            Integer ini1 = chromosome1.getGene(i);
            Integer ini2 = chromosome2.getGene(i);
            if (i != 0 && ini1.equals(Integer.valueOf(0))) {
                Integer aux = chromosome1.getGene(0);
                chromosome1.setGene(0, ini1);
                chromosome1.setGene(i, aux);
            }
            if (i != geneLength - 1 && ini1.equals(Integer.valueOf(geneLength - 1))) {
                Integer aux = chromosome1.getGene(geneLength - 1);
                chromosome1.setGene(geneLength - 1, ini1);
                chromosome1.setGene(i, aux);
            }
            if (i != 0 && ini2.equals(Integer.valueOf(0))) {
                Integer aux = chromosome2.getGene(0);
                chromosome2.setGene(0, ini2);
                chromosome2.setGene(i, aux);
            }
            if (i != geneLength - 1 && ini2.equals(Integer.valueOf(geneLength - 1))) {
                Integer aux = chromosome2.getGene(geneLength - 1);
                chromosome2.setGene(geneLength - 1, ini2);
                chromosome2.setGene(i, aux);
            }
        }
    }

    void readjust(Chromosome<Integer> chromosome) {
        final int geneLength = getGenes().length;
        for (int i = 0; i < geneLength; i++) {
            Integer ini1 = chromosome.getGene(i);
            if (i != 0 && ini1.equals(Integer.valueOf(0))) {
                Integer aux = chromosome.getGene(0);
                chromosome.setGene(0, ini1);
                chromosome.setGene(i, aux);
            }
            if (i != geneLength - 1 && ini1.equals(Integer.valueOf(geneLength - 1))) {
                Integer aux = chromosome.getGene(geneLength - 1);
                chromosome.setGene(geneLength - 1, ini1);
                chromosome.setGene(i, aux);
            }
        }
    }
}

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
package com.googlecode.routingways.solver.simplex;

import com.googlecode.routingways.points.Point;
import com.googlecode.routingways.points.Segment;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrix;

/**
 *
 * @author thiago
 */
public class SimplexGeneratorImpl implements SimplexGenerator {

    private double[] objFunction;
    private double[][] matrix;
    private double[] rSide;
    private double[] equalities;
    private StringBuilder varInicio;
    private StringBuilder varFim;
    private Map<String, Segment> segments = new LinkedHashMap<String, Segment>();

    public SimplexGeneratorImpl(List<Point> points) {
        generateMatrix(points);
    }

    private boolean isOrigemIgualDestino(int i, int j) {
        return (i == j);
    }

    private boolean isValidSubCons(int i, int j, int size) {
        return (i != j && i != 0 && i != (size - 1) && j != 0 && j != (size - 1));
    }

    private boolean isPonteOrigemDestino(int i, int j, int size) {
        return ((i != (size - 1) || j != 0) && (j != (size - 1) || i != 0));
    }

    private boolean isDestinationTheFirst(int i, int size) {
        return (i == (size - 1));
    }

    private boolean isOriginTheLast(int j) {
        return (j == 0);
    }

    public boolean satisfiesObjectiveFunc(int i, int j, int size) {
        return (!isOrigemIgualDestino(i, j)
                && !isDestinationTheFirst(i, size)
                && !isOriginTheLast(j)
                && isPonteOrigemDestino(i, j, size));
    }

    private void generateMatrix(List<Point> pontos) {
        final int size = pontos.size();
        final int others = (size - 2);
        final int columns = size * others;
        final int lines = 2 * size - 2;
        final int subCons = others * (others - 1);
        varInicio = new StringBuilder();
        varFim = new StringBuilder();
        objFunction = new double[columns];
        matrix = new double[lines + subCons][columns];
        rSide = new double[lines + subCons];
        equalities = new double[lines + subCons];
        int[] pos = new int[subCons];
        RealMatrix localMatrix = new Array2DRowRealMatrix(size, size);
        RealMatrix secondCons = new Array2DRowRealMatrix(subCons, others);

        int idxObj = 0;
        int idxLig = 0;
        int idxSubCons = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Point inicio = pontos.get(i);
                Point fim = pontos.get(j);
                if (satisfiesObjectiveFunc(i, j, size)) {
                    if (isValidSubCons(i, j, size)) {
                        secondCons.setEntry(idxSubCons, i - 1, 1);
                        secondCons.setEntry(idxSubCons, j - 1, -1);
                        pos[idxSubCons++] = idxObj;
                    }
                    String aux = "S" + inicio.getName().toLowerCase() + "_" + fim.getName().toLowerCase();
                    varInicio.append(aux).append(' ');
                    Segment seg = new Segment(inicio, fim);
                    segments.put(aux, seg);
                    objFunction[idxObj++] = seg.getLength();
                    localMatrix.setEntry(i, j, 1);
                }
                if (idxLig < lines + subCons) {
                    rSide[idxLig] = (idxLig < (lines)) ? 1 : others - 1;
                    equalities[idxLig] = (idxLig < (lines)) ? 0 : - 1;
                    idxLig++;
                }
            }
            if (!(i == 0 || i == size - 1)) {
                Point pt = pontos.get(i);
                String aux = "U" + pt.getName().toLowerCase();
                varFim.append(aux).append(' ');
            }
        }

        int novaLinha = 0;
        for (int i = 0; i < localMatrix.getRowDimension(); i++) {
            double[] line = localMatrix.getRow(i);
            double[] column = localMatrix.getColumn(i);
            if (i == 0) {
                //starting
                matrix[novaLinha++] = lastLine(line, columns, i);
            } else if (i == (localMatrix.getRowDimension() - 1)) {
                //ending
                matrix[novaLinha++] = firstLine(column, columns, i);
            } else {
                //common case
                matrix[novaLinha++] = firstLine(column, columns, i);
                matrix[novaLinha++] = lastLine(line, columns, i);
            }
        }

        int dobro = 0;
        for (int i = lines; i < lines + subCons; i++) {
            double[] aux1 = secondCons.getRow(dobro);
            double[] aux2 = matrix[i];
            System.arraycopy(aux1, 0, aux2, columns - others, aux1.length);
            aux2[pos[dobro++]] = others;
            matrix[i] = aux2;
        }
    }

    double[] lastLine(double[] row, int colunas, int ponto) {
        int count = 0;
        double[] output = new double[colunas];
        for (int i = 0; i < row.length; i++) {
            for (int j = 0; j < row.length; j++) {
                if (satisfiesObjectiveFunc(i, j, row.length)) {
                    if (i == ponto) {
                        output[count] = row[j];
                    }
                    count++;
                }
            }
        }
        return output;
    }

    double[] firstLine(double[] row, int colunas, int ponto) {
        int count = 0;
        double[] output = new double[colunas];
        for (int i = 0; i < row.length; i++) {
            for (int j = 0; j < row.length; j++) {
                if (satisfiesObjectiveFunc(i, j, row.length)) {
                    if (j == ponto) {
                        output[count] = row[i];
                    }
                    count++;
                }
            }
        }
        return output;
    }

    @Override
    public double[] objFunction() {
        return objFunction;
    }

    @Override
    public double[][] matrix() {
        return matrix;
    }

    @Override
    public double[] rightSide() {
        return rSide;
    }

    @Override
    public double[] equalities() {
        return equalities;
    }

    @Override
    public String colNames() {
        return (varInicio.toString() + varFim.toString()).trim();
    }

    @Override
    public Map<String, Segment> segments() {
        return segments;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        //sb.append("Objetivo: \n");
        //sb.append(Arrays.toString(objetivo));
        sb.append("\n");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                sb.append(matrix[i][j]);
                if (j < matrix.length - 1) {
                    sb.append(", ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

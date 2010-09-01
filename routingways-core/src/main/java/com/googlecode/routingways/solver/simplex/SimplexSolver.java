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
import com.googlecode.routingways.points.Route;
import com.googlecode.routingways.points.Segment;
import com.googlecode.routingways.solver.BaseSolver;
import com.googlecode.routingways.solver.OptmizeException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import lpsolve.LpSolve;
import lpsolve.LpSolveException;

/**
 *
 * @author thiago
 */
public class SimplexSolver extends BaseSolver {

    private SimplexGenerator generator;

    @Override
    public Route optmizeRoute(Point first, Point last, Point... others) throws OptmizeException {
        validate(others);
        List<Point> points = new LinkedList<Point>();
        points.add(first);
        points.addAll(Arrays.asList(others));
        points.add(last);
        generator = new SimplexGeneratorImpl(points);
        Route route = new Route();

        try {
            double[] objective = generator.objFunction();
            double[] rightSide = generator.rightSide();
            double[] equalities = generator.equalities();
            double[][] matrix = generator.matrix();
            String columns = generator.colNames();
            LpSolve lpSolve = LpSolve.makeLp(0, objective.length);
            lpSolve.setVerbose(LpSolve.NEUTRAL);
            for (int i = 0; i < matrix.length; i++) {
                double[] line = matrix[i];
                int sign = equalitySign((int) equalities[i]);
                double rSide = rightSide[i];
                lpSolve.strAddConstraint(toStr(line), sign, rSide);
            }
            int idx = 1;
            for (String col : columns.split(" ")) {
                lpSolve.setColName(idx++, col);
            }
            lpSolve.strSetObjFn(toStr(objective));
            for (int i = 0; i < objective.length - others.length; i++) {
                lpSolve.setInt(i + 1, true);
            }
            lpSolve.solve();
            String key = null;
            double[] coisas = lpSolve.getPtrVariables();
            Segment aux = null;
            for (int i = 0; i < coisas.length; i++) {
                double d = coisas[i];
                if (d != 0.0) {
                    key = lpSolve.getColName(i + 1);
                    aux = generator.segments().get(key);
                    if (aux != null) {
                        route.add(aux);
                    }
                }
            }

            lpSolve.deleteLp();
        } catch (LpSolveException ex) {
            String message = ResourceBundle.getBundle("messages").getString("lpsolve.problema");
            Logger.getLogger(SimplexSolver.class.getName()).log(Level.SEVERE, null, ex);
            throw new OptmizeException(message, ex);
        } catch (UnsatisfiedLinkError error) {
            String message = ResourceBundle.getBundle("messages").getString("lpsolve.problema");
            Logger.getLogger(SimplexSolver.class.getName()).log(Level.SEVERE, null, error);
            throw new OptmizeException(message, error);
        } catch (NoClassDefFoundError error) {
            String message = ResourceBundle.getBundle("messages").getString("lpsolve.problema");
            Logger.getLogger(SimplexSolver.class.getName()).log(Level.SEVERE, null, error);
            throw new OptmizeException(message, error);
        }
        return route;
    }

    private int equalitySign(int signal) {
        int sig = 0;
        switch (signal) {
            case -1:
                sig = LpSolve.LE;
                break;
            case 0:
                sig = LpSolve.EQ;
                break;
            default:
                sig = LpSolve.GE;
        }
        return sig;
    }

    private String toStr(double[] signal) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < signal.length; i++) {
            sb.append(signal[i]).append(' ');

        }
        return sb.toString().trim();
    }
}

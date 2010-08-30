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
package com.googlecode.routingwaysui;

import com.googlecode.routingways.points.Point;
import com.googlecode.routingways.points.Route;
import com.googlecode.routingways.points.Segment;
import com.googlecode.routingways.solver.OptmizeException;
import com.googlecode.routingways.solver.Solver;
import com.googlecode.routingwaysui.util.Values;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.JPanel;

/**
 *
 * @author thiago
 */
public class DefaultPlan extends JPanel implements Plan {

    private static final int DIAMETER = 10;
    private Solver solver;
    private Set<Point> points = new LinkedHashSet<Point>();
    private LinkedList<Point> queue = new LinkedList<Point>();
    private Status status = Status.STARTED;
    private Point verify;
    private Point first;
    private Point last;
    private Route route;
    private Aparencia aparencia;
    private int count = 0;

    public DefaultPlan(Aparencia ap) {
        this.aparencia = ap;
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(true);
        setBackground(Color.WHITE);
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseMotionListener);
    }

    public void addPoint(Point point) {
        points.add(point);
        queue.addLast(point);
        repaint();
    }

    @Override
    public void removeLastPoint() {
        Point last = queue.removeLast();
        points.remove(last);
        count--;
        repaint();
    }

    @Override
    public void end() {
        status = Status.ENDED;
        aparencia.end();
    }

    @Override
    public void restart() {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        points.clear();
        queue.clear();
        count = 0;
        verify = null;
        first = null;
        last = null;
        route = null;
        status = Status.STARTED;
        aparencia.restart();
        repaint();
    }

    @Override
    public void optimizeRoute() throws OptmizeException {
        aparencia.optimizing();
        Point[] others = null;
        Set<Point> pnts = new TreeSet<Point>(points);
        if (pnts.contains(first) && pnts.contains(last)) {
            pnts.remove(first);
            pnts.remove(last);
            others = new Point[pnts.size()];
            int i = 0;
            for (Point point : pnts) {
                others[i++] = point;
            }
        }
        long init = System.currentTimeMillis();
        route = solver.optmizeRoute(first, last, others);
        long term = System.currentTimeMillis();
        status = Status.OPTIMIZED;
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        repaint();
        aparencia.optimizing(route, (term - init), (route.qtSegments() + 1));
    }

    public Solver getSolver() {
        return solver;
    }

    @Override
    public void setSolver(Solver solver) {
        this.solver = solver;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public Point getLast() {
        return last;
    }

    @Override
    public Point getFirst() {
        return first;
    }

    @Override
    public Set<Point> getPoints() {
        return points;
    }

    @Override
    public void setLast(Point last) {
        this.last = last;
    }

    @Override
    public void setFirst(Point first) {
        this.first = first;
    }

    @Override
    public void setPoints(Set<Point> points) {
        this.points = points;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        switch (status) {
            case STARTED:
                initialPaint(g2d);
                break;
            case ENDED:
                finalizadoPaint(g2d);
                break;
            case OPTIMIZED:
                otimizadoPaint(g2d);
                break;
        }
    }

    public void initialPaint(Graphics2D g2d) {
        g2d.setBackground(Color.WHITE);
        g2d.setColor(Color.BLACK);
        Point point = null;
        for (Iterator<Point> it = points.iterator(); it.hasNext();) {
            point = it.next();
            g2d.fillOval(point.getX().intValue() - DIAMETER / 2,
                    point.getY().intValue() - DIAMETER / 2, DIAMETER, DIAMETER);
        }
        if (first != null) {
            g2d.setColor(Color.GREEN);
            g2d.fillOval(first.getX().intValue() - DIAMETER / 2,
                    first.getY().intValue() - DIAMETER / 2, DIAMETER, DIAMETER);
        }
        if (last != null) {
            g2d.setColor(Color.RED);
            g2d.fillOval(last.getX().intValue() - DIAMETER / 2,
                    last.getY().intValue() - DIAMETER / 2, DIAMETER, DIAMETER);
        }
    }

    public void finalizadoPaint(Graphics2D g2d) {
        initialPaint(g2d);
        Point p = onArea(verify);
        if (p != null && !p.equals(first) && !p.equals(last)) {
            g2d.setColor(Color.BLUE);
            g2d.fillOval(p.getX().intValue() - DIAMETER / 2,
                    p.getY().intValue() - DIAMETER / 2, DIAMETER, DIAMETER);
        } else if (p != null && p.equals(first)) {
            g2d.setColor(Color.GREEN);
            g2d.fillOval(p.getX().intValue() - DIAMETER / 2,
                    p.getY().intValue() - DIAMETER / 2, DIAMETER, DIAMETER);
        } else if (p != null && p.equals(last)) {
            g2d.setColor(Color.RED);
            g2d.fillOval(p.getX().intValue() - DIAMETER / 2,
                    p.getY().intValue() - DIAMETER / 2, DIAMETER, DIAMETER);
        }
    }

    public void otimizadoPaint(Graphics2D g2d) {
        finalizadoPaint(g2d);
        g2d.setColor(Color.BLUE);
        Segment seg = null;
        for (Iterator<Segment> it = route.iterator(); it.hasNext();) {
            seg = it.next();
            g2d.drawLine(seg.getFirst().getX().intValue(), seg.getFirst().getY().intValue(),
                    seg.getLast().getX().intValue(), seg.getLast().getY().intValue());
            g2d.fillRect(seg.getLast().getX().intValue() - DIAMETER / 8,
                    seg.getLast().getY().intValue() - DIAMETER / 8, DIAMETER / 3, DIAMETER / 3);
        }
    }

    public Point onArea(Point v) {
        int xv = v.getX().intValue();
        int yv = v.getY().intValue();
        int xp = 0;
        int yp = 0;
        Point rtrn = null;
        for (Point pt : points) {
            xp = pt.getX().intValue();
            yp = pt.getY().intValue();
            if (xp >= xv - DIAMETER / 2 && xp <= xv + DIAMETER / 2) {
                if (yp >= yv - DIAMETER / 2 && yp <= yv + DIAMETER / 2) {
                    rtrn = pt;
                    break;
                }
            }
        }
        return rtrn;
    }

    public Point getOnArea(Point p) {
        Point other = onArea(p);

        Set<Point> pnts = new TreeSet<Point>(points);
        for (Point point : pnts) {
            if (point.equals(other)) {
                return other;
            }
        }
        return null;
    }
    private MouseMotionListener mouseMotionListener = new MouseMotionAdapter() {

        @Override
        public void mouseMoved(MouseEvent e) {
            if (status == Status.ENDED) {
                verify = new PointAdapter("P", e.getPoint());
                repaint();
            }
        }
    };
    private MouseListener mouseListener = new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
            switch (status) {
                case STARTED:
                    addPoint(new PointAdapter(String.valueOf(Values.POINTS[count++]), e.getPoint()));
                    repaint();
                    break;
                case ENDED:
                    if (first == null) {
                        first = getOnArea(new PointAdapter("P", e.getPoint()));
                        aparencia.statusSelectFinal();
                    } else if (last == null) {
                        Point p = getOnArea(new PointAdapter("P", e.getPoint()));
                        if (p != null) {
                            last = p;
                            aparencia.statusOptimized();
                        }
                    }
                    repaint();
                    break;
            }

        }
    };

    @Override
    public double realDistance(double distance) {
        return 1 * distance;
    }
}

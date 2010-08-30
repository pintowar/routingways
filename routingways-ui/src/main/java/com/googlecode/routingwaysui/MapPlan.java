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
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.painter.Painter;

/**
 *
 * @author thiago
 */
public class MapPlan extends JXMapKit implements Plan {

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

    public MapPlan(Aparencia aparencia) {
        this.aparencia = aparencia;

        getMainMap().addMouseListener(mouseListener);
        getMainMap().addMouseMotionListener(mouseMotionListener);

        setAddressLocationShown(false);
        //setDefaultProvider(DefaultProviders.SwingLabsBlueMarble);
        //getMainMap().setTileFactory(new DefaultTileFactory(getTileInfo()));
        setTileFactory(GoogleMapsTileProvider.getDefaultTileFactory());
        getMainMap().setCenterPosition(new GeoPosition(-3.7169, -38.5427));
        getMainMap().setZoom(10);

        setMiniMapVisible(false);
        setZoomButtonsVisible(false);
        getMainMap().setZoomEnabled(false);
        refreshScreen();
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
        //TODO modificar a medida 
        long term = System.currentTimeMillis();
        status = Status.OPTIMIZED;
        refreshScreen();
        aparencia.optimizing(route, (term - init), (route.qtSegments() + 1));
    }

    @Override
    public void restart() {
        points.clear();
        queue.clear();
        count = 0;
        verify = null;
        first = null;
        last = null;
        route = null;
        status = Status.STARTED;
        aparencia.restart();
        refreshScreen();
    }

    @Override
    public void end() {
        status = Status.ENDED;
        aparencia.end();
    }

    @Override
    public void removeLastPoint() {
        if (!queue.isEmpty()) {
            Point last = queue.removeLast();
            points.remove(last);
            count--;

            refreshScreen();
        }
    }

    @Override
    public void setSolver(Solver solver) {
        this.solver = solver;
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

    public void addPoint(Point p) {
        points.add(p);
        queue.addLast(p);
        refreshScreen();
    }

    public Point onArea(Point v) {
        java.awt.Point ver = geoToPoint(new GeoPosition(v.getY().doubleValue(), v.getX().doubleValue()));
        int xv = ver.x;
        int yv = ver.y;
        int xp = 0;
        int yp = 0;
        Point rtrn = null;
        for (Point pt : points) {
            java.awt.Point point = geoToPoint(new GeoPosition(pt.getY().doubleValue(), pt.getX().doubleValue()));
            xp = point.x;
            yp = point.y;
            if (xp >= xv - DIAMETER / 2 && xp <= xv + DIAMETER / 2) {
                if (yp >= yv - DIAMETER / 2 && yp <= yv + DIAMETER / 2) {
                    rtrn = pt;
                    break;
                }
            }
        }
        return rtrn;
    }

    private void refreshScreen() {
        Painter<JXMapViewer> polygonOverlay = new Painter<JXMapViewer>() {

            @Override
            public void paint(Graphics2D g, JXMapViewer map, int w, int h) {
                g = (Graphics2D) g.create();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                //convert from viewport to world bitmap
                Rectangle rect = map.getViewportBounds();
                g.translate(-rect.x, -rect.y);

                for (Point aux : points) {
                    switch (status) {
                        case STARTED:
                            initialPaint(g, map, aux);
                            break;
                        case ENDED:
                            endedPaint(g, map, aux);
                            break;
                        case OPTIMIZED:
                            otimizedPaint(g, map, aux);
                            break;
                    }
                }

                g.dispose();
            }
        };

        getMainMap().setOverlayPainter(polygonOverlay);
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

    public void initialPaint(Graphics2D g2d, JXMapViewer map, Point aux) {
        Point2D p2 = map.getTileFactory().geoToPixel(((GeoPontoAdapter) aux).getGeo(), map.getZoom());
        g2d.setColor(Color.BLACK);
        if (first != null && aux.equals(first)) {
            g2d.setColor(Color.GREEN);
        } else if (last != null && aux.equals(last)) {
            g2d.setColor(Color.RED);
        }
        g2d.fillOval((int) (p2.getX() - (DIAMETER / 2)),
                (int) (p2.getY() - (DIAMETER / 2)), DIAMETER, DIAMETER);
    }

    public void endedPaint(Graphics2D g2d, JXMapViewer map, Point aux) {
        initialPaint(g2d, map, aux);
        Point p = onArea(verify);
        Point2D p2 = map.getTileFactory().geoToPixel(((GeoPontoAdapter) aux).getGeo(), map.getZoom());
        if (p != null && !p.equals(first) && !p.equals(last)) {
            if (p.equals(new GeoPontoAdapter("P", ((GeoPontoAdapter) aux).getGeo()))) {
                g2d.setColor(Color.BLUE);
                g2d.fillOval((int) (p2.getX() - (DIAMETER / 2)), (int) (p2.getY() - (DIAMETER / 2)), DIAMETER, DIAMETER);
            }
        } else if (p != null && p.equals(first)) {
            if (p.equals(new GeoPontoAdapter("P", ((GeoPontoAdapter) aux).getGeo()))) {
                g2d.setColor(Color.GREEN);
                g2d.fillOval((int) (p2.getX() - (DIAMETER / 2)), (int) (p2.getY() - (DIAMETER / 2)), DIAMETER, DIAMETER);
            }

        } else if (p != null && p.equals(last)) {
            if (p.equals(new GeoPontoAdapter("P", ((GeoPontoAdapter) aux).getGeo()))) {
                g2d.setColor(Color.RED);
                g2d.fillOval((int) (p2.getX() - (DIAMETER / 2)), (int) (p2.getY() - (DIAMETER / 2)), DIAMETER, DIAMETER);
            }

        }
    }

    public void otimizedPaint(Graphics2D g2d, JXMapViewer map, Point aux) {
        initialPaint(g2d, map, aux);
        g2d.setColor(Color.BLUE);
        if (first != null && aux.equals(first)) {
            Segment seg = null;
            /*int zx = -1;
            int zy = -1;*/
            for (Iterator<Segment> it = route.iterator(); it.hasNext();) {
                seg = it.next();
                Point2D ptAtual = map.getTileFactory().geoToPixel(((GeoPontoAdapter) seg.getLast()).getGeo(), map.getZoom());
                Point2D ptAnt = map.getTileFactory().geoToPixel(((GeoPontoAdapter) seg.getFirst()).getGeo(), map.getZoom());
                g2d.drawLine((int) ptAnt.getX(), (int) ptAnt.getY(), (int) ptAtual.getX(), (int) ptAtual.getY());
            }
        }
    }
    private MouseMotionListener mouseMotionListener = new MouseMotionAdapter() {

        @Override
        public void mouseMoved(MouseEvent e) {
            if (status == Status.ENDED) {
                GeoPosition pos = pointToGeo(e.getPoint());
                verify = new GeoPontoAdapter("P", pos);
                refreshScreen();
            }

        }
    };
    private MouseListener mouseListener = new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
            switch (status) {
                case STARTED:
                    GeoPosition pos = pointToGeo(e.getPoint());
                    addPoint(new GeoPontoAdapter(String.valueOf(Values.POINTS[count++]), pos));
                    refreshScreen();
                    break;
                case ENDED:
                    if (first == null) {
                        first = getOnArea(new GeoPontoAdapter("P", pointToGeo(e.getPoint())));
                        aparencia.statusSelectFinal();
                    } else if (last == null) {
                        Point p = getOnArea(new GeoPontoAdapter("P", pointToGeo(e.getPoint())));
                        if (p != null) {
                            last = p;
                            aparencia.statusOptimized();
                        }
                    }
                    refreshScreen();
                    break;

            }
        }
    };

    public GeoPosition pointToGeo(java.awt.Point p) {
        return getMainMap().convertPointToGeoPosition(p);
    }

    public java.awt.Point geoToPoint(GeoPosition p) {
        Point2D p2 = getMainMap().convertGeoPositionToPoint(p);
        java.awt.Point point = new java.awt.Point();
        point.setLocation(p2.getX(), p2.getY());
        return point;
    }

    @Override
    public double realDistance(double distance) {
        return 111.133 * distance;
    }
}

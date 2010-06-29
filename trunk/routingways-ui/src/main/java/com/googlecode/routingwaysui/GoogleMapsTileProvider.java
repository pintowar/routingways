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

import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;

/**
 *
 * @author thiago
 */
public class GoogleMapsTileProvider {

    private static final String VERSION = "2.75";
    private static final int minZoom = 1;
    private static final int maxZoom = 16;
    private static final int mapZoom = 17;
    private static final int tileSize = 256;
    private static final boolean xr2l = true;
    private static final boolean yt2b = true;
    /*private static final String baseURL =
    "http://mt2.google.com/mt?n=404&v=w" + VERSION;*/
    private static final String baseURL =
    //"http://mt.google.com/mt?";
    "http://mt.google.com/vt/";
    /*private static final String baseURL =
            "http://maps.google.com/staticmap?";*/
    private static final String x = "x";
    private static final String y = "y";
    private static final String z = "zoom";
    private static final TileFactoryInfo GOOGLE_MAPS_TILE_INFO = new TileFactoryInfo(
            minZoom, maxZoom, mapZoom, tileSize, xr2l,
            yt2b, baseURL, x, y, z) {

        @Override
        public String getTileUrl(int x, int y, int zoom) {
            /*int nrTiles = (int) Math.pow(2, 2*zoom);
            int circumference = tileSize * nrTiles/2;
            double radius = circumference / (2 * Math.PI);
            double xp = MercatorUtils.xToLong(x, radius);
            double yp = MercatorUtils.yToLat(y, radius);
            String url = new StringBuffer(baseURL).
                    append("x=").append(x).append("&y=").append(y).
                    //append("center=").append(xp).append(",").append(yp).
                    append("&zoom=").append(zoom).
                    append("&size=").append(tileSize).append("x").append(tileSize).toString();*/
            String url = super.getTileUrl(x, y, zoom);
            //GeoPosition gp = getDefaultTileFactory().pixelToGeo(new Point(x, y), zoom);
            //System.out.println(url);
            return url;
        }
    };

    public static TileFactory getDefaultTileFactory() {

        return (new DefaultTileFactory(
                GOOGLE_MAPS_TILE_INFO));

    }
}

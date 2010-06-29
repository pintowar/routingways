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
package com.googlecode.routingwaysui.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thiago
 */
public class Values {

    public static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String[] POINTS = generator();//{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "W", "X", "Y", "Z"};

    public static final String[] generator() {
        String[] base = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "W", "X", "Y", "Z"};
        List<String> conjunto = new ArrayList<String>();
        List<String> unicos = new ArrayList<String>();
        for (int i = 0; i < base.length; i++) {
            String a = base[i];
            unicos.add(a);
            for (int j = 0; j < base.length; j++) {
                String b = base[j];
                if (!a.equals(b)) {
                    conjunto.add(a + b);
                }
            }
        }

        unicos.addAll(conjunto);
        String[] rtrn = new String[unicos.size()];
        rtrn = unicos.toArray(rtrn);
        return rtrn;
    }
}

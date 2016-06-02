/*
 * Created 16-05-11 by 16cassidyandrew
 * Copyright (C) 2016
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package NotDoom;

/**
 *
 * @author 16cassidyandrew
 */
public class IntVector {
    // FIELDS

    private int x;
    private int y;

    // CONSTRUCTOR

    public IntVector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // METHODS

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public Vector toVector() {
        return new Vector(x, y);
    }

    public boolean equals(IntVector v) {
        return (v.x == x && v.y == y);
    }

    //checks if the points are in a clockwise order
    public static boolean clockwise(IntVector v1, IntVector v2, IntVector v3) {
        int dx1 = v2.x() - v1.x();
        int dx2 = v3.x() - v1.x();
        int dy1 = v2.y() - v1.y();
        int dy2 = v3.y() - v1.y();

        return ((dx1 * dy2 - dx2 * dy1) < 0);
    }

    //checks if the points are in a clockwise order
    public static boolean clockwise(IntVector v1, IntVector v2, Vector v3) {
        float dx1 = v2.x() - v1.x();
        float dx2 = v3.x() - v1.x();
        float dy1 = v2.y() - v1.y();
        float dy2 = v3.y() - v1.y();

        return ((dx1 * dy2 - dx2 * dy1) < 0);
    }
}

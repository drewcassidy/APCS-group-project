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
package NotDoom.Map;

import NotDoom.Vector;
import NotDoom.IntVector;
import java.util.ArrayList;

/**
 *
 * @author 16cassidyandrew
 */

// convex area in a NotDoom map
public class Region {
    
    // FIELDS

    private IntVector[] vertexes; //typo preserved for Doom authenticity
    private Region[] neighbors; // other regions inside from this one
    private RegionData regionData;
    private Wall[] walls;


    // CONSTRUCTORS

    public Region(IntVector[] vertexes, WallData[] wallData, RegionData regionData) {
        walls = new Wall[vertexes.length];
        this.vertexes = vertexes;
        for (int i = 0; i < vertexes.length - 1; i++) {
            int j = (i + 1) % vertexes.length;
            walls[i] = new Wall(vertexes[i], vertexes[j], wallData[i]);
        }
        this.regionData = regionData;
        neighbors = new Region[vertexes.length];
    }


    // METHODS

    public void addNeighbor(int index, Region r) {
        neighbors[index] = r;
    }

    public boolean contains(Vector v) {
        for (int i = 0; i < vertexes.length; i++) {
            int j = (i + 1) % vertexes.length;
            if (!IntVector.clockwise(vertexes[i], vertexes[j], v)) return false;
        }
        return true;
    }

    public Wall[] getWalls() {
        return walls;
    } 

    public ArrayList<Wall> getVisibleWalls(Vector v) {
        ArrayList<Wall> visibleWalls = new ArrayList<Wall>();
        for (int i = 0; i < walls.length; i++) {
            if (walls[i].inside(v)) visibleWalls.add(walls[i]);
        }
        return visibleWalls;
    }

    public ArrayList<Wall> getVisibleWallsRecursive(Vector v, int depth, ArrayList<Region> exclude) {
        exclude.add(this);
        ArrayList<Wall> visibleWalls = new ArrayList<>();
        visibleWalls.addAll(getVisibleWalls(v));
        if (depth != 0) {
            for (Region r : neighbors) {
                if (!exclude.contains(r)) {
                    visibleWalls.addAll(r.getVisibleWallsRecursive(v, depth - 1, exclude));
                }
            }
        }
        return visibleWalls;
    }
}

/*
 * Copyright 2016 16kohnegrant.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package NotDoom.Map;

import NotDoom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 *
 * @author 16kohnegrant
 */
public class Map {
    //array of regions, array of enemys
    private Region[] regions;
    private Sprite[] sprites;
    private Player   player;

    private final int MAPSIZE = 500;

    public Map(String path) {
        BufferedImage[] textures = new BufferedImage[MAPSIZE];
        IntVector[] vertexes = new IntVector[MAPSIZE];
        regions = new Region[MAPSIZE];
        ArrayList<IntVector>[] regionVertexes = new ArrayList[MAPSIZE];
        ArrayList<BufferedImage>[] regionTextures = new ArrayList[MAPSIZE];
        HashMap<Integer, Integer>[] regionNeighbors = new HashMap[MAPSIZE];
        RegionData[] regionData = new RegionData[MAPSIZE];

        player = new Player(new Vector(2, 2));

        int tempFloor = -1;
        int tempCeiling = -1;
        int regionCount = 0;

        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String line; 

            while ((line = br.readLine()) != null) {
                if (!line.isEmpty() && Character.isLetter(line.charAt(0))) {
                    String[] split = line.split("\\s+");
                    char key = split[0].charAt(0);
                    int index = Integer.parseInt(split[0].substring(1));
                    int region = 0;

                    switch (key) {
                        case 't':
                            textures[index] = ImageIO.read(new File(split[1]));
                            System.out.println("adding texture " + index + " at " + split[1]);
                            break;
                        case 'v':
                            vertexes[index] = new IntVector(Integer.parseInt(split[1]), Integer.parseInt(split[2]));
                            System.out.println("adding vertex " + index + " at " + split[1] +  "," + split[2]);
                            break;
                        case 'r':
                            region = index;
                            int floorHeight = 0;
                            int ceilingHeight = 10;
                            BufferedImage floorTexture = null;
                            BufferedImage ceilingTexture = null;

                            if (split.length > 1) floorHeight = Integer.parseInt(split[1]);
                            if (split.length > 2) ceilingHeight = Integer.parseInt(split[2]);
                            if (split.length > 3) floorTexture = textures[Integer.parseInt(split[3])];
                            if (split.length > 4) ceilingTexture = textures[Integer.parseInt(split[4])];
                            regionData[index] = new RegionData(ceilingTexture, floorTexture, ceilingHeight, floorHeight);
                            regionTextures[index] = new ArrayList<BufferedImage>();
                            regionVertexes[index] = new ArrayList<IntVector>();
                            regionNeighbors[index] = new HashMap<Integer, Integer>();
                            regionCount++;
                            System.out.println("starting region " + index);
                            break;
                        case 'l':
                            regionVertexes[region].add(vertexes[Integer.parseInt(split[1])]);
                            BufferedImage texture = null;
                            if (split.length > 2) {
                                texture = textures[Integer.parseInt(split[2])];
                            }
                            regionTextures[region].add(texture);
                            System.out.println("adding line " + index + " with vertex " + split[1]);
                            break;
                        case 'n':
                            regionNeighbors[region].put(index, Integer.parseInt(split[1]));
                            break;
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Could not find file " + path);
            
        } catch (IOException e) {
            System.out.println("Unknown IO error");

        }

        System.out.println("done reading " + path);

        for (int i = 0; i < regionCount; i++) {
            int size = regionVertexes[i].size();
            IntVector[] tempVertexes = regionVertexes[i].toArray(new IntVector[size]);
            BufferedImage[] tempTextures = regionTextures[i].toArray(new BufferedImage[size]);
            regions[i] = new Region(tempVertexes, tempTextures, regionData[i]);
            for (int j = 0; j < size; j++) {
                if (regionNeighbors[i].containsKey(j)) {
                    Region neighbor = regions[regionNeighbors[i].get(j)];
                    regions[i].addNeighbor(j, neighbor);
                }
            }
        }

        System.out.println("done building map");
    }

    public Region currentRegion() {
        for (int i = 0; i < regions.length; i++) {
            if (regions[i].contains(player.getPos())) return regions[i];
        }
        return null;
    }
}

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

import NotDoom.IntVector;
import NotDoom.Sprite;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author 16kohnegrant
 */
public class Map {
    //array of regions, array of enemys
    private Region[] regions;
    private Sprite[] sprites;

    public Map(String path) {
        String[] texturePaths = new String[65536];
        IntVector[] vertexes = new IntVector[65536];
        Region[] regions = new Region[65536];
        ArrayList<IntVector>[] regionVertexes = new ArrayList[65536];
        ArrayList<WallData>[]  regionWallData = new ArrayList[65536];
        RegionData[] regionData = new RegionData[65536];

        int tempFloor = -1;
        int tempCeiling = -1;

        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String line; 

            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] split = line.split(" ");
                    char key = split[0].charAt(0);
                    int index = Integer.parseInt(split[0].substring(1));
                    int region = 0;

                    switch (key) {
                        case 't':
                            texturePaths[index] = split[1];
                            System.out.println("adding texture " + index + " at " + split[1]);
                            break;
                        case 'v':
                            vertexes[index] = new IntVector(Integer.parseInt(split[1]), Integer.parseInt(split[2]));
                            System.out.println("adding vertex " + index + " at " + split[1] +  "," + split[2]);
                            break;
                        case 'r':
                            region = index;
                            regionData[index] = new RegionData(null, null, Integer.parseInt(split[1]), Integer.parseInt(split[2]));
                            regionWallData[index] = new ArrayList<WallData>();
                            regionVertexes[index] = new ArrayList<IntVector>();
                            System.out.println("starting region " + index);
                            break;
                        case 'l':
                            regionVertexes[region].add(vertexes[Integer.parseInt(split[1])]);
                            int top = Integer.parseInt(split[2]);
                            int mid = Integer.parseInt(split[3]);
                            int bot = Integer.parseInt(split[4]);
                            boolean transparent = Boolean.parseBoolean(split[5]);
                            regionWallData[region].add(new WallData(null, null, null, transparent));
                            System.out.println("adding line " + index + " with vertex " + split[1]);
                            break ;
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Could not find file " + path);
            
        } catch (IOException e) {

        }

        for (int i = 0; i < regionData.length; i++) {
            int size = regionVertexes[i].size();
            IntVector[] tempVertexes = regionVertexes[i].toArray(new IntVector[size]);
            WallData[] tempWallData = regionWallData[i].toArray(new WallData[size]);
            //regions[i] = new Region(tempVertexes, tempWallData, regionData[i]);
        }
    }
}
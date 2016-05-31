/*
 * Copyright 2016 16cassidyandrew.
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
package NotDoom.Renderer;
import NotDoom.IntVector;
import NotDoom.Map.*;
import NotDoom.Player;
import NotDoom.Vector;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author 16cassidyandrew
 */
public class DoomRenderer {

    private BufferedImage buffer;
    private int[] backBuffer;
    private final int WIDTH;
    private final int HEIGHT;
    private ArrayList<Wall> walls;
    private final int MAPSCALE = 10;

    public DoomRenderer(BufferedImage buffer) {
        this.buffer = buffer;
        WIDTH = buffer.getWidth();
        HEIGHT = buffer.getHeight();
        backBuffer = new int[WIDTH * HEIGHT];
        walls = new ArrayList<>();
        
        
    }

    public void DrawRegion(Region r) {
    }

    public void DrawPixel(int x, int y, int c) {
        backBuffer[x + y * WIDTH] = c;
    }

    public void DrawColumn(int x, int y1, int y2, int height, int offset, BufferedImage texture) {
        for (int y = y1; y <= y2; y++) {
            DrawPixel(x, y, texture.getRGB(offset % texture.getWidth(), (int) ((float) (y - y1) / (y2 - y1) * height % texture.getHeight())));
        }
    }

    public void DrawWall(Wall w, Player p, int minx, int maxx, int miny, int maxy, int floor, int ceiling) {
        Vector v1 = p.worldToLocal(w.v1());
        Vector v2 = p.worldToLocal(w.v2());
        int x1 = (int) ((WIDTH / 2) + (float) (WIDTH / 2) * (v1.x() / v1.y()));
        int x2 = (int) ((WIDTH / 2) + (float) (WIDTH / 2) * (v2.x() / v2.y()));
        int y1 = (int) ((HEIGHT / 2) + (float) (HEIGHT / 2) * (p.getHeight() - floor) / v1.y());
        int y2 = (int) ((HEIGHT / 2) + (float) (HEIGHT / 2) * (p.getHeight() - floor) / v2.y());
        int h1 = (int) ((HEIGHT / 2) + (float) (HEIGHT / 2) * (ceiling - floor) / v1.y());
        int h2 = (int) ((HEIGHT / 2) + (float) (HEIGHT / 2) * (ceiling - floor) / v2.y());
        
    }
    
    public void DrawMap(Map m){
        Region [] r = m.getRegions();
              
        for (int i = 0; i < r.length; i++){
            walls.addAll(r[i].getSolidWalls());
        }
        
        for (int j = 0; j < walls.size(); j++){
            IntVector v1, v2;
            v1 = walls.get(j).v1();
            v2 = walls.get(j).v2();
            
            DrawLine(10 + v1.x() * MAPSCALE, 10 + v2.x() * MAPSCALE, 10 + v1.y() * MAPSCALE, 10 + v2.y() * MAPSCALE, 0xFFFFFF);
        }

        Player p = m.getPlayer();
        drawBox(MAPSCALE * (int) p.getPos().x() - 3, MAPSCALE * (int) p.getPos().y() - 3, 6, 6, 0xFF0000);
    }

    public void DrawLine(int x1, int x2, int y1, int y2, int color){
        int dx = x2 - x1;
        int dy = y2 - y1;
        
        if (dx != 0) {
            float error = -1.0f;
            float dError = Math.abs((float) dy / (float) dx);
            if (dError < 1) {
                if (x1 > x2) {
                    int temp = x1;
                    x1 = x2;
                    x2 = temp;
                    temp = y1;
                    y1 = y2;
                    y2 = temp;
                }
                int y = y1;
                for (int x = x1; x < x2; x++) {
                    DrawPixel(x, y, color);
                    error += dError;
                    while (error >=  0 ) {
                        y += Math.signum(y2 - y1);
                        error -= 1;
                    }
                }
            } else {
                if (y1 > y2) {
                    int temp = x1;
                    x1 = x2;
                    x2 = temp;
                    temp = y1;
                    y1 = y2;
                    y2 = temp; 
                }
                int x = x1;
                for (int y = y1; y < y2; y++) {
                    DrawPixel(x, y, color);
                    error += (1 / dError);
                    while (error >= 0) {
                        x += Math.signum(x2 - x1);
                        error -= 1;
                    }
                }
            }
        } else {
            if (y1 > y2) {
                int temp = y1;
                y1 = y2;
                y2 = temp; 
            }
            for (int y = y1; y <= y2; y++) {
                DrawPixel(x1, y, color);
            }
        }
    }

    public void drawBox(int x, int y, int l, int h, int color) {
        for (int dx = 0; dx <= l; dx++) {
            for (int dy = 0; dy <= h; dy++) {
                DrawPixel(x + dx, y + dy, color);
            }
        }
    }

    public void DrawFrame() {
        buffer.setRGB(0, 0, WIDTH, HEIGHT, backBuffer, 0, WIDTH);
        for (int i = 0; i < backBuffer.length; i++) {
            backBuffer[i] = 0;
        }
    }
}

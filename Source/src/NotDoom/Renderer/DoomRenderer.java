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
    private final float FOV = 0.8f;
    private final float CLIP = 0.1f;
    private final int STRIPCLIP = 50;
    private final int ZSCALE = 10;
    private final int TEXSCALE = 8;

    public DoomRenderer(BufferedImage buffer) {
        this.buffer = buffer;
        WIDTH = buffer.getWidth();
        HEIGHT = buffer.getHeight();
        backBuffer = new int[WIDTH * HEIGHT];
        walls = new ArrayList<>();
        
        
    }

    public void DrawRegion(Region r, Player p) {
        if (r == null) return;
        Wall[] walls = r.getWalls();

        for (int i = 0; i < walls.length; i++) {
            DrawWallRecursive(walls[i], p, 0, WIDTH, r.getFloor(), r.getCeiling(), null, null, null, null);
        }
    }

    public void DrawRegionRecursive(Region r, Player p, int minx, int maxx, ArrayList<Region> exclude) {
        if (r == null) return;
        if (exclude.size() == 0) {
            p.setHeight(r.getFloor() + 5);
        }
        exclude.add(r);
        Wall[] walls = r.getWalls();
        for (int i = 0; i < walls.length; i++) {
            Region neighbor = r.getNeighbor(i);
            if (!exclude.contains(neighbor)) {
                DrawWallRecursive(walls[i], p, minx, maxx, r.getFloor(), r.getCeiling(), neighbor, exclude, r.getFloorTex(), r.getCeilingTex());
            }
            if (neighbor != null) {
                if (neighbor.getFloor() > r.getFloor()) {
                    DrawWallRecursive(walls[i], p, minx, maxx, r.getFloor(), neighbor.getFloor(), null, null, r.getFloorTex(), null);
                }
                if (neighbor.getCeiling() < r.getCeiling()) {
                    DrawWallRecursive(walls[i], p, minx, maxx, neighbor.getCeiling(), r.getCeiling(), null, null, null, r.getCeilingTex());
                }
            }
        }
    }

    public void DrawPixel(int x, int y, int c) {
        if (x >= 0 && x <= WIDTH && y >= 0 && y <= HEIGHT) {
            backBuffer[x + y * WIDTH] = c;
        }
    }

    public void DrawColumn(int x, int y1, int y2, int height, int offset, BufferedImage texture) {
        int screenHeight = (y2 - y1);
        float texelsPerPixel = (float) height / (float) screenHeight;
        int dy = (y1 < 0) ? y1 * -1 : 0;
        int texY = (int) (dy * texelsPerPixel);
        float tError = dy * texelsPerPixel % 1;
        int texX = (offset + texture.getWidth()) % texture.getWidth();
        int texHeight = texture.getHeight();

        while(dy <= screenHeight && y1 + dy < HEIGHT) {
            DrawPixel(x, y1 + dy, texture.getRGB(texX, texY % texHeight));
            tError += texelsPerPixel;
            texY += (int) tError;
            tError %= 1;
            dy++;
        }
    }

    public void DrawWallRecursive(Wall w, Player p, int minx, int maxx, int floor, int ceiling, Region neighbor, ArrayList<Region> exclude, BufferedImage floorTex, BufferedImage ceilingTex) {
        Vector v1 = p.worldToLocal(w.v1());
        Vector v2 = p.worldToLocal(w.v2());
        float trim1 = 1;
        float trim2 = 1;

        if (v1.y() < CLIP) {
            trim1 = (CLIP - v2.y()) / (v2.y() - v1.y());
            float x = v2.x() + (v2.x() - v1.x()) * trim1; 
            v1 = new Vector(x, CLIP);
        }

        if (v2.y() < CLIP) {
            trim2 = (CLIP - v1.y()) / (v1.y() - v2.y());
            float x = v1.x() + (v1.x() - v2.x()) * trim2;
            v2 = new Vector(x, CLIP);
        }

        int x1 = (int) ((WIDTH / 2) + (float) (WIDTH / 2) * (v1.x() / v1.y()) * FOV);
        int x2 = (int) ((WIDTH / 2) + (float) (WIDTH / 2) * (v2.x() / v2.y()) * FOV);

        if (neighbor == null) {
            int bot1 = (int) ((HEIGHT / 2) + (float) (WIDTH / 2) * (p.getHeight() - floor) / v1.y() / (FOV * ZSCALE));
            int bot2 = (int) ((HEIGHT / 2) + (float) (WIDTH / 2) * (p.getHeight() - floor) / v2.y() / (FOV * ZSCALE));
            int top1 = (int) ((HEIGHT / 2) + (float) (WIDTH / 2) * (p.getHeight() - ceiling) / v1.y() / (FOV * ZSCALE));
            int top2 = (int) ((HEIGHT / 2) + (float) (WIDTH / 2) * (p.getHeight() - ceiling) / v2.y() / (FOV * ZSCALE));
            float d1 = v1.y();
            float d2 = v2.y();
            float length = (TEXSCALE * ZSCALE * w.length());

            for (int dx = 0; dx <= x2 - x1 && dx + x1 < WIDTH; dx++) {
                float step = ((float) dx / (float) (x2 - x1));
                float tStep = step * Math.abs(trim2) * Math.abs(trim1) + (1 - Math.abs(trim1));
                int bot = (int) (bot1 + step * (bot2 - bot1));
                int top = (int) (top1 + step * (top2 - top1));
                int u = (int) (((1 - tStep) * ((1 - Math.abs(trim1)) / d1) + (tStep * (length / d2))) / ((1 - tStep) * (1 / d1) + tStep * (1 / d2)));
                if (dx + x1 >= minx && dx + x1 < maxx) {
                    DrawColumn(dx + x1, top, bot, TEXSCALE * (ceiling - floor), u, w.getTexture());
                    if (ceilingTex != null) {
                        drawStrip(dx + x1, top, (int) p.getHeight() - ceiling, 0, 0, ceilingTex, p.getRot());
                    }
                    if (floorTex != null) {
                        drawStrip(dx + x1, bot, (int) p.getHeight() - floor, 0, 0, floorTex, p.getRot());
                    }
                }
            }
        } else {
            x1 = Math.max(x1, minx);
            x2 = Math.min(x2, maxx);
            DrawRegionRecursive(neighbor, p, x1, x2, exclude);
        }
    }
    
    public void drawStrip(int x, int y, int h, float u1, int v1, BufferedImage texture, float rot) {
        float d2 = Math.abs((FOV * ZSCALE * 2 * Math.abs(y - (HEIGHT / 2))) / (WIDTH * h));
        float d1 = CLIP;
        float u2 = u1 + (int) (d2 * TEXSCALE);
        int length = Math.min(y, HEIGHT - y);

        for (int dy = 0; dy < length; dy++) {
            float step = (float) dy / (float) length;
            float u = (((((1 - step) * (u1 / d1))) + (step * (u2 / d2))) / (((1 - step) / d1) + (step / d2)));
            int color = texture.getRGB(v1, (int) u);
            DrawPixel(x, (h < 0) ? dy : HEIGHT - 1 - dy, color);
        }
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
        IntVector v = new IntVector((int) (MAPSCALE * p.getPos().x()), (int) (MAPSCALE * p.getPos().y()));
        drawBox(v.x() + 7, v.y() + 7, 6, 6, 0xFF0000);
        DrawLine(10 + v.x(), (int) (10 + Math.sin(p.getRot()) * 10) + v.x(), 10 + v.y(), (int) (10 + Math.cos(p.getRot()) * -10) + v.y(), 0xFF0000);
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
    }
    public void clearFrame() {
        for (int i = 0; i < backBuffer.length; i++) {
            backBuffer[i] = 0;
        }
    }
}

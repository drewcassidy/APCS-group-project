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
import NotDoom.Map.*;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author 16cassidyandrew
 */
public class DoomRenderer {

    private BufferedImage buffer;
    private int[] backBuffer;
    private final int WIDTH;
    private final int HEIGHT;

    public DoomRenderer(BufferedImage buffer) {
        this.buffer = buffer;
        WIDTH = buffer.getWidth();
        HEIGHT = buffer.getHeight();
        backBuffer = new int[WIDTH * HEIGHT];
    }

    public void DrawRegion(Region r) {
    }

    public void DrawPixel(int x, int y, int c) {
        System.out.println("drawing pixel " + c);
        backBuffer[x + y * WIDTH] = c;
    }

    public void DrawColumn(int x, int y1, int y2, int height, int offset, BufferedImage texture) {
        for (int y = y1; y <= y2; y++) {
            DrawPixel(x, y, texture.getRGB(offset % texture.getWidth(), (int) ((float) (y - y1) / (y2 - y1) * height % texture.getHeight())));
        }
    }

    public void DrawFrame() {
        buffer.setRGB(0, 0, WIDTH, HEIGHT, backBuffer, 0, WIDTH);
        for (int i = 0; i < backBuffer.length; i++) {
            backBuffer[i] = 0;
        }
    }
}

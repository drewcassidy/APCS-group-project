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
package NotDoom.Map;

import NotDoom.IntVector;
import NotDoom.Vector;

/**
 *
 * @author 16cassidyandrew
 */
public class Line {
    
    // FIELDS

    private IntVector v1;
    private IntVector v2;


    // CONSTRUCTOR

    public Line(IntVector v1, IntVector v2) {
        this.v1 = v1;
        this.v2 = v2;
    }


    // METHODS

    public IntVector v1() {
        return v1;
    }

    public IntVector v2() {
        return v2;
    }

    public boolean inside(Vector v) {
        return IntVector.clockwise(v1, v2, v);
    }

    public float length() {
        return (float) Math.sqrt(Math.pow(v1.x() - v2.x(), 2) + Math.pow(v1.y() - v2.y(), 2));
    }
}

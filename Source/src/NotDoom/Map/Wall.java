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

/**
 *
 * @author 16cassidyandrew
 */
public class Wall extends Line {

    // FIELDS
    
    private WallData wd;


    // CONSTRUCTORS

    public Wall(Vector v1, Vector v2, WallData wd) {
        super(v1, v2);
        this.wd = wd;
    }


    // METHODS
}
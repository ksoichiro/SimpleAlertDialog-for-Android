/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.simplealertdialog.test;

import java.util.ArrayList;
import java.util.List;

public class Sweets {

    public String id;
    public String name;

    public Sweets() {
    }

    public Sweets(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @SuppressWarnings("serial")
    public static final List<Sweets> SWEETS_LIST = new ArrayList<Sweets>() {
        {
            add(new Sweets("1.5", "Cupcake"));
            add(new Sweets("1.6", "Donut"));
            add(new Sweets("2.0", "Eclair"));
            add(new Sweets("2.2", "Froyo"));
            add(new Sweets("2.3", "Gingerbread"));
            add(new Sweets("3.0", "Honeycomb"));
            add(new Sweets("4.0", "Ice Cream Sandwich"));
            add(new Sweets("4.1", "Jelly Beans"));
            add(new Sweets("4.4", "KitKat"));
        }
    };
}

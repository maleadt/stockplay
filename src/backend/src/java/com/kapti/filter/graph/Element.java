/*
 * Copyright (c) 2010 StockPlay development team
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.kapti.filter.graph;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author tim
 */
public abstract class Element {
    //
    // Member data
    //

    protected Map<String, String> mAttributes;


    //
    // Construction
    //

    public Element() {
        mAttributes = new HashMap<String, String>();
    }


    //
    // Methods
    //

    public void setAttribute(String iKey, String iValue) {
        mAttributes.put(iKey, iValue);
    }


    //
    // Interface
    //

    abstract void render(PrintStream iStream);
}
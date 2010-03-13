/*
 * Node.java
 * StockPlay - Node-object voor DOT output.
 *
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

/**
 *
 * @author tim
 */
public class Node extends Element {
    //
    // Member data
    //

    private static int mSingletonID = 1;

    private final String mID;


    //
    // Construction
    //

    public Node() {
        mID = Integer.toString(mSingletonID++);
    }

    public Node(String iPrefix) {
        mID = iPrefix + "_" + Integer.toString(mSingletonID++);
    }


    //
    // Methods
    //

    public void render(PrintStream iStream) {
        // Print all node attributes
        for (String tKey : mAttributes.keySet()) {
            String tValue = mAttributes.get(tKey);
            iStream.println(mID + " [" + tKey + "=\"" + tValue + "\"];");
        }
    }

    public String getID() {
        return mID;
    }
}
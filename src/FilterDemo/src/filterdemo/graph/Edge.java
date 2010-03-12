/*
 * Edge.java
 * StockPlay - Edge-object voor DOT output.
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
package filterdemo.graph;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tim
 */
public class Edge extends Element {
    //
    // Member data
    //

    private List<Node> mNodes;


    //
    // Construction
    //

    public Edge() {
        mNodes = new ArrayList<Node>();
    }

    public Edge(Node... iNodes) {
        this();
        for (Node tNode : iNodes)
            addNode(tNode);
    }


    //
    // Methods
    //

    public void addNode(Node iNode) {
        mNodes.add(iNode);
    }

    public void render(PrintStream iStream) {
        // Print all contained nodes
        for (int i = 0; i < mNodes.size(); i++) {
            iStream.print(mNodes.get(i).getID() + " ");
            if (i != mNodes.size() - 1) {
                iStream.print("-- ");
            }
        }

        // Set the attributes
        if (mAttributes.size() > 0) {
            iStream.print("[");
        }
        for (String tKey : mAttributes.keySet()) {
            String tValue = mAttributes.get(tKey);
            iStream.print(tKey + "=\"" + tValue + "\" ");
        }
        if (mAttributes.size() > 0) {
            iStream.print("]");
        }
        iStream.println(";");
    }
}

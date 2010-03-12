/*
 * Condition.java
 * StockPlay - Conditie-interface.
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
package filterdemo.condition;

import filterdemo.Convertable;
import filterdemo.data.Data;
import filterdemo.graph.Graph;
import filterdemo.graph.Node;

/**
 *
 * @author tim
 */
public abstract class Condition extends Convertable {
    //
    // Methods
    //

    public void check() {
        for (Object tObject : mParameters) {
            if (!(tObject instanceof Data)) {
                throw new RuntimeException("Conditions only accept data as parameters");
            }
        }
    }
    
    public Data getData(int index) {
        return (Data) mParameters.get(index);
    }

    public Node addNode(Graph iGraph) {
        // Self
        Node tNodeSelf = new Node("CONDITION");
        tNodeSelf.setAttribute("shape", "ellipse");
        iGraph.addElement(tNodeSelf);

        return tNodeSelf;
    }
}

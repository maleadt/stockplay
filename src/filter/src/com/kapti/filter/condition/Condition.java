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
package com.kapti.filter.condition;

import com.kapti.filter.Convertable;
import com.kapti.filter.data.Data;
import com.kapti.filter.graph.Graph;
import com.kapti.filter.graph.Node;

/**
 *
 * @author tim
 */
public abstract class Condition extends Convertable {
    //
    // Methods
    //
    
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

/*
 * Relation.java
 * StockPlay - Relatie-interface.
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
package filterdemo.relation;

import filterdemo.condition.Condition;
import filterdemo.graph.Graph;
import filterdemo.graph.Node;

/**
 * Een relatie, wordt gebruikt om het resultaat van verschillende condities
 * met elkaar te linken.
 * 
 * @author tim
 */
public abstract class Relation extends Condition {
    //
    // Construction
    //

    public Condition getCondition(int index) {
        return (Condition) mParameters.get(index);
    }


    //
    // Methods
    //

    @Override
    public void check() {
        for (Object tObject : mParameters) {
            if (!(tObject instanceof Condition)) {
                throw new RuntimeException("Relation only accepts other conditions as parameters");
            }
        }
    }

    @Override
    public Node addNode(Graph iGraph) {
        // Self
        Node tNodeSelf = new Node("RELATION");
        tNodeSelf.setAttribute("shape", "box");
        iGraph.addElement(tNodeSelf);

        return tNodeSelf;
    }
}

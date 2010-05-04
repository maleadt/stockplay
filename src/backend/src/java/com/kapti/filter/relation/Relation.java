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
package com.kapti.filter.relation;

import com.kapti.filter.Convertable;
import com.kapti.filter.condition.Condition;
import com.kapti.filter.graph.Graph;
import com.kapti.filter.graph.Node;
import java.util.List;

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

    public Relation(Relation iObject) {
        super(iObject);
    }

    public Relation(List<Convertable> iParameters) {
        super(iParameters);
    }

    //
    // Methods
    //

    public Condition getCondition(int index) {
        return (Condition) mParameters.get(index);
    }


    public static Class[] getSignature() {
        return new Class[] {Condition.class, Condition.class};
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

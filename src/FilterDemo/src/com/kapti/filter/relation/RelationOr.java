/*
 * RelationOr.java
 * StockPlay - OR relatie.
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
package com.kapti.filter.relation;

import com.kapti.filter.condition.Condition;
import com.kapti.filter.exception.FilterException;
import com.kapti.filter.graph.Edge;
import com.kapti.filter.graph.Graph;
import com.kapti.filter.graph.Node;

/**
 *
 * @author tim
 */
public class RelationOr extends Relation {
    //
    // Methods
    //

    @Override
    public void check() throws FilterException {
        super.check();
        if (mParameters.size() != 2) {
            throw new RuntimeException("OR relation only acceps exactly two parameters, I got " + mParameters.size());
        }
    }

    @Override
    public final Object compile() throws FilterException {
        RelationOr tConverter = (RelationOr) getConverter();

        return tConverter.process(getCondition(0), getCondition(1));
    }

    @Override
    public Node addNode(Graph iGraph) {
        // Self
        Node tNodeSelf = super.addNode(iGraph);
        tNodeSelf.setAttribute("label", "OR");

        // Children
        Node tNodeLeft = getCondition(0).addNode(iGraph);
        Node tNodeRight = getCondition(1).addNode(iGraph);

        // Edges
        iGraph.addElement(new Edge(tNodeSelf, tNodeLeft));
        iGraph.addElement(new Edge(tNodeSelf, tNodeRight));

        return tNodeSelf;
    }
    
    
    //
    // Interface
    //

    public Object process(Condition a, Condition b) throws FilterException {
        throw new RuntimeException();
    }

}

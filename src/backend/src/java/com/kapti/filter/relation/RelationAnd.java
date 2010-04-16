/*
 * RelationAnd.java
 * StockPlay - AND relatie.
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

import com.kapti.exceptions.FilterException;
import com.kapti.filter.Convertable;
import com.kapti.filter.condition.Condition;
import com.kapti.filter.graph.Edge;
import com.kapti.filter.graph.Graph;
import com.kapti.filter.graph.Node;
import java.util.List;

/**
 *
 * @author tim
 */
public class RelationAnd extends Relation {
    //
    // Construction
    //

    public RelationAnd(RelationAnd iObject) {
        super(iObject);
    }

    public RelationAnd(List<Convertable> iParameters) {
        super(iParameters);
    }


    //
    // Methods
    //

    @Override
    public final Object compile() throws FilterException {
        RelationAnd tConverter = (RelationAnd) getConverter();

        return tConverter.process(getCondition(0), getCondition(1));
    }

    @Override
    public Node addNode(Graph iGraph) {
        // Self
        Node tNodeSelf = super.addNode(iGraph);
        tNodeSelf.setAttribute("label", "and");

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

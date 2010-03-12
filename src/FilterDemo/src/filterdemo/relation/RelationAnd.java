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
package filterdemo.relation;

import filterdemo.condition.Condition;
import filterdemo.graph.Edge;
import filterdemo.graph.Graph;
import filterdemo.graph.Node;

/**
 *
 * @author tim
 */
public class RelationAnd extends Relation {
    @Override
    public void check() {
        super.check();
        if (mParameters.size() != 2) {
            throw new RuntimeException("AND relation only acceps exactly two parameters, I got " + mParameters.size());
        }
    }

    @Override
    public final Object convert() throws Exception {
        RelationAnd tConverter = (RelationAnd) getConverter();

        return tConverter.process(getCondition(0), getCondition(1));
    }

    public Object process(Condition a, Condition b) throws Exception {
        throw new RuntimeException();
    }

    @Override
    public Node addNode(Graph iGraph) {
        // Self
        Node tNodeSelf = super.addNode(iGraph);
        tNodeSelf.setAttribute("label", "AND");

        // Children
        Node tNodeLeft = getCondition(0).addNode(iGraph);
        Node tNodeRight = getCondition(1).addNode(iGraph);

        // Edges
        iGraph.addElement(new Edge(tNodeLeft, tNodeSelf));
        iGraph.addElement(new Edge(tNodeRight, tNodeSelf));

        return tNodeSelf;
    }

}

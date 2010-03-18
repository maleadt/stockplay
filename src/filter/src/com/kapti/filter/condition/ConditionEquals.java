/*
 * ConditionEquals.java
 * StockPlay - Gelijkheids-conditie.
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
import com.kapti.filter.data.DataKey;
import com.kapti.filter.exception.FilterException;
import com.kapti.filter.graph.Edge;
import com.kapti.filter.graph.Graph;
import com.kapti.filter.graph.Node;
import java.util.List;

/**
 * Een gelijkheidscontrole. Deze conditie heeft twee parameters nodig, waarbij
 * de eerste een string moet zijn (die de key die moet gecontroleerd worden
 * vastlegt.
 * 
 * @author tim
 */
public class ConditionEquals extends Condition {
    //
    // Constructie
    //

    public ConditionEquals(List<Convertable> iParameters) {
        super(iParameters);
    }

    public ConditionEquals(ConditionEquals iObject) {
        super(iObject);
    }
    

    //
    // Methods
    //

    public static Class[] getSignature() {
        return new Class[] {DataKey.class, Data.class};
    }

    @Override
    public final Object compile() throws FilterException {
        ConditionEquals tConverter = (ConditionEquals) getConverter();

        return tConverter.process(getData(0), getData(1));
    }

    @Override
    public Node addNode(Graph iGraph) {
        // Self
        Node tNodeSelf = super.addNode(iGraph);
        tNodeSelf.setAttribute("label", "EQUALS");

        // Children
        Node tNodeLeft = getData(0).addNode(iGraph);
        Node tNodeRight = getData(1).addNode(iGraph);

        // Edges
        iGraph.addElement(new Edge(tNodeSelf, tNodeLeft));
        iGraph.addElement(new Edge(tNodeSelf, tNodeRight));

        return tNodeSelf;
    }
    
    
    //
    // Interface
    //

    public Object process(Data a, Data b) throws FilterException {
        throw new RuntimeException();
    }
}

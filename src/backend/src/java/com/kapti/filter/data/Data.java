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
package com.kapti.filter.data;

import com.kapti.filter.Convertable;
import com.kapti.filter.graph.Graph;
import com.kapti.filter.graph.Node;
import java.util.ArrayList;

/**
 *
 * @author tim
 */
public abstract class Data extends Convertable {
    //
    // Member data
    //

    protected Object mData;
    
    
    //
    // Construction
    //

    public Data(Data iObject) {
        super(iObject);
        mData = ((Data)iObject).mData;
    }

    public Data(Object iData) {
        super(new ArrayList<Convertable>());
        mData = iData;
    }
    

    //
    // Methods
    //

    public static Class[] getSignature() {
        return new Class[] {};
    }

    public Node addNode(Graph iGraph) {
        // Self
        Node tNodeSelf = new Node("Data");
        tNodeSelf.setAttribute("shape", "doubleoctagon");
        iGraph.addElement(tNodeSelf);

        return tNodeSelf;
    }

    @Override
    public int hashCode() {
        // Hash the type of data
        int code = this.getClass().hashCode();

        // Hash the inner data object
        code = (31 * code) + mData.hashCode();

        return code;
    }
}

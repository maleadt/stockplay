/*
 * DataString.java
 * StockPlay - String dataobject.
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
package com.kapti.filter.data;

import com.kapti.filter.exception.FilterException;
import com.kapti.filter.graph.Graph;
import com.kapti.filter.graph.Node;

/**
 *
 * @author tim
 */
public class DataString extends Data {
    //
    // Construction
    //

    public DataString() {
        
    }
    
    public DataString(String iString) {
        super(iString);
    }


    //
    // Methods
    //

    @Override
    public void check() throws FilterException {
        super.check();
        if (!(mData instanceof String))
            throw new FilterException("String data object can only contain a string");
    }

    @Override
    public final Object compile() throws FilterException {
        DataString tConverter = (DataString) getConverter();

        return tConverter.process((String) mData);
    }

    @Override
    public Node addNode(Graph iGraph) {
        // Self
        Node tNodeSelf = super.addNode(iGraph);
        tNodeSelf.setAttribute("label", "String::"+(String)mData);

        return tNodeSelf;
    }


    //
    // Interface
    //

    public Object process(String a) throws FilterException {
        throw new RuntimeException();
    }

}

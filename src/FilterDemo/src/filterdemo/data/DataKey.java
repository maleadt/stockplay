/*
 * DataKey.java
 * StockPlay - Key dataobject.
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
package filterdemo.data;

import filterdemo.graph.Graph;
import filterdemo.graph.Node;

/**
 *
 * @author tim
 */
public class DataKey extends Data {
    public DataKey() {
        
    }
    public DataKey(String iData) {
        super(iData);
    }
    
    @Override
    public void check() {
        super.check();
        System.err.println("I contain a " + mData);
        // TODO: dit crasht wanner check() op de Converter gecallet wordt, bij gebrek aan data
        if (!(mData instanceof String))
            throw new RuntimeException("Key data object can only contain a string");
    }

    @Override
    public final Object convert() throws Exception {
        DataKey tConverter = (DataKey) getConverter();

        return tConverter.process((String) mData);
    }

    public Object process(String a) throws Exception {
        throw new RuntimeException();
    }

    @Override
    public Node addNode(Graph iGraph) {
        // Self
        Node tNodeSelf = super.addNode(iGraph);
        tNodeSelf.setAttribute("label", "Key::"+(String)mData);

        return tNodeSelf;
    }

}

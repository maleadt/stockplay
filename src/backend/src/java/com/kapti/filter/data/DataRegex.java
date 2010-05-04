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

import com.kapti.exceptions.FilterException;
import com.kapti.filter.graph.Graph;
import com.kapti.filter.graph.Node;

/**
 *
 * @author tim
 */
public class DataRegex extends Data {
    //
    // Data members
    //

    boolean mCaseSensitive = true;


    //
    // Construction
    //

    public DataRegex(DataRegex iObject) {
        super(iObject);
    }

    public DataRegex(String iRegex) {
        super(iRegex);
    }


    //
    // Methods
    //

    public boolean isCaseSensitive() {
        return mCaseSensitive;
    }

    public void setModifiers(char[] iModifiers) throws FilterException {
        for (char tChar : iModifiers) {
            switch (tChar) {
                case 'i':
                    mCaseSensitive = false;
                    break;
                default:
                    throw new FilterException(FilterException.Type.FILTER_FAILURE, "unknown regex modifier '" + tChar + "'");
            }
        }
    }

    @Override
    public final Object compile() throws FilterException {
        DataRegex tConverter = (DataRegex) getConverter();

        return tConverter.process((String) mData);
    }

    @Override
    public Node addNode(Graph iGraph) {
        // Self
        Node tNodeSelf = super.addNode(iGraph);
        tNodeSelf.setAttribute("label", "Regex::"+(String)mData);

        return tNodeSelf;
    }


    //
    // Interface
    //

    public Object process(String s) throws FilterException {
        throw new RuntimeException();
    }

}

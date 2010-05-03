/*
 * DataStringConverter.java
 * StockPlay - Filter converter voor string dataobject.
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
package com.kapti.filter.data.filter;

import com.kapti.exceptions.FilterException;
import com.kapti.filter.data.DataRegex;

/**
 *
 * @author tim
 */
public class DataRegexConverter extends DataRegex {
    //
    // Construction
    //

    public DataRegexConverter(DataRegex iObject) {
        super(iObject);
    }


    //
    // Methods
    //

    @Override
    public Object process(String a) throws FilterException {
        StringBuilder tRegex = new StringBuilder("'" + a + "'");

        // Process modifiers
        if (!isCaseSensitive())
            tRegex.append("i");
        
        return tRegex.toString();
    }
}

/*
 * ConditionGreaterThanOrEqualConverter.java
 * StockPlay - SQL converter voor een groter dan of gelijk aan conditie.
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
package com.kapti.filter.condition.sql;

import com.kapti.filter.condition.ConditionEquals;
import com.kapti.filter.data.Data;
import com.kapti.filter.exception.FilterException;

public class ConditionGreaterThanOrEqualConverter extends ConditionEquals {

    //
    // Construction
    //

    public ConditionGreaterThanOrEqualConverter(ConditionEquals iObject) {
        super(iObject);
    }

    //
    // Methods
    //

    @Override
    public Object process(Data a, Data b) throws FilterException {
        return (String)a.compile() + " >= " + (String)b.compile();
    }
}
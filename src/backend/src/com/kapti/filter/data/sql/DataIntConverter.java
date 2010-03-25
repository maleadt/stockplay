/*
 * DataIntConverter.java
 * StockPlay - Converter voor integer dataobject.
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
package com.kapti.filter.data.sql;

import com.kapti.exceptions.FilterException;
import com.kapti.filter.data.DataInt;

/**
 *
 * @author tim
 */
public class DataIntConverter extends DataInt {
    //
    // Construction
    //

    public DataIntConverter(DataInt iObject) {
        super(iObject);
    }


    //
    // Methods
    //

    @Override
    public Object process(Integer a) throws FilterException {
        return a.toString();
    }

}
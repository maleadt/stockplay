/*
 * DataDateTime.java
 * StockPlay - Datetime dataobject.
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

import com.kapti.exceptions.FilterException;
import com.kapti.filter.graph.Graph;
import com.kapti.filter.graph.Node;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author tim
 */
public class DataDate extends Data {
    //
    // Data members
    //

    // DateTime parse patterns
    private static SimpleDateFormat tFormatISO8601_date = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat tFormatISO8601_full = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");    // using HH forces the Calender in 24-hour mode, maybe to this explicitely?


    //
    // Construction
    //

    public DataDate(DataDate iObject) {
        super(iObject);
    }

    public DataDate(Date iData) {
        super(iData);
    }


    //
    // Methods
    //

    public static Date parseDate(String iString) throws FilterException {
        Date oDate;
        ParsePosition tPosition;

        tPosition = new ParsePosition(0);
        oDate = tFormatISO8601_date.parse(iString, tPosition);
        if (tPosition.getIndex() == iString.length())
            return oDate;
        
        tPosition = new ParsePosition(0);
        oDate = tFormatISO8601_full.parse(iString, tPosition);
        if (tPosition.getIndex() == iString.length())
            return oDate;

        throw new FilterException(FilterException.Type.FILTER_FAILURE, "could not match input DateTime string against any format");
    }

    @Override
    public final Object compile() throws FilterException {
        DataDate tConverter = (DataDate) getConverter();

        return tConverter.process((Date) mData);
    }

    @Override
    public Node addNode(Graph iGraph) {
        // Self
        Node tNodeSelf = super.addNode(iGraph);
        tNodeSelf.setAttribute("label", "Date::"+tFormatISO8601_full.format((Date)mData));

        return tNodeSelf;
    }


    //
    // Interface
    //

    public Object process(Date d) throws FilterException {
        throw new RuntimeException();
    }

}

/*
 * Error.java
 * StockPlay - Protocol error enumeration class
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
package com.kapti.backend.api;

import org.apache.xmlrpc.XmlRpcException;

/**
 *
 * @author tim
 */
public enum Error {
    //
    // Available errors
    //

    INTERNAL_FAILURE(0, "Internal Failure"),

    // Subsystem failures
    DATABASE_FAILURE(1, "Database Failure"),
    SCRAPER_FAILURE(2, "Scraper Failure"),

    // Service issues
    SERVICE_UNAVAILABLE(10, "Service Unavailable"),
    UNAUTHORIZED(11, "Unauthorized"),

    // Invocation issues
    VERSION_NOT_SUPPORTED(20, "Version Not Supported"),
    NOT_FOUND(21, "Not Found"),
    BAD_REQUEST(22, "Bad Request");


    //
    // Data members
    //

    private final int mCode;

    private final String mMessage;


    //
    // Construction
    //

    Error(int iCode, String iMessage) {
        this.mCode = iCode;
        mMessage = iMessage;
    }


    //
    // Methods
    //

    public int getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    public XmlRpcException getException() {
        return new XmlRpcException(getCode(), getMessage());
    }
}

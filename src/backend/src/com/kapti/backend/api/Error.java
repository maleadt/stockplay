/*
 * Error.java
 * StockPlay - Protocol error enumeratieklasse.
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
 * \brief Protocol error enumeratieklasse.
 *
 * Deze enumeratieklasse bevat objecten die gebruikt worden om een uniforme
 * set aan protocolerrors naar de gebruiker te sturen. De verschillende
 * errors zijn ingedeeld in enkele types:
 *  - Subsystem problemen
 *  - Service problemen
 *  - Gebruiksproblemen
 */
public enum Error {
    //
    // Beschikbare foutmeldingen
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
    // Dataleden
    //

    private final int mCode;

    private final String mMessage;


    //
    // Constructie
    //

    Error(int iCode, String iMessage) {
        this.mCode = iCode;
        mMessage = iMessage;
    }


    //
    // Methodes
    //

    /**
     * Haalt de code van een fouttype op. Normaal enkel intern gebruikt.
     */
    public int getCode() {
        return mCode;
    }

    /**
     * Haalt een human-readable foutbericht op, normaal enkel intern gebruikt.
     */
    public String getMessage() {
        return mMessage;
    }

    /**
     * Belangrijkste functie voor extern gebruik: genereert een XmlRpcException
     * gebaseerd op de interne foutcode en -bericht.
     */
    public XmlRpcException getException() {
        return new XmlRpcException(getCode(), getMessage());
    }

    /**
     * Idem, maar met ondersteuning voor de error cause.
     */
    public XmlRpcException getException(Throwable iCause) {
        return new XmlRpcException(getCode(), getMessage(), iCause);
    }
}

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

package com.kapti.exceptions;

public final class InvocationException extends StockPlayException {

    public static enum Type implements Error {
        //
        // Beschikbare foutmeldingen
        //

        VERSION_NOT_SUPPORTED(20, "Version Not Supported"),
        NOT_FOUND(21, "Not Found"),
        BAD_REQUEST(22, "Bad Request"),
        NON_EXISTING_ENTITY(23, "Non Existing Entity"),
        PRE_EXISTING_ENTITY(24, "Pre Existing Entity"),
        READ_ONLY_KEY(25, "Request to modify a read only key"),
        KEY_DOES_NOT_EXIST(26, "Requested key does not exist");

        private final int mCode;
        private final String mMessage;

        Type(int iCode, String iMessage) {
            this.mCode = iCode;
            mMessage = iMessage;
        }

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
    }

    public InvocationException(Type iType, String message) {
        super(iType.getCode(), iType.getMessage() + ": " + message);
    }

    public InvocationException(Type iType, String message, Throwable cause) {
        super(iType.getCode(), iType.getMessage() + ": " + message, cause);
    }

    public InvocationException(Type iType) {
        super(iType.getCode(), iType.getMessage());
    }

    public InvocationException(Type iType, Throwable cause) {
        super(iType.getCode(), iType.getMessage(), cause);
    }

}
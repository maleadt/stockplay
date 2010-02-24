/*
 * Dummy.java
 * StockPlay - Dummy handler implementation
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
package com.kapti.backend.implementation;

import com.kapti.backend.MethodInterface;
import com.kapti.backend.Pobject;

/**
 *
 * @author tim
 */
public class Dummy implements MethodInterface {

    private Pobject pobject = null;

    public void init(Pobject pobject) {
        // our init method, here we receive the object
        // we want our handler class to work with.
        this.pobject = pobject;
    }

    public boolean setString(String string) {
        pobject.setString(string);
        return true;
    }

    public String getString() {
        String returnValue = null;
        returnValue = pobject.getString();
        return returnValue;
    }
}

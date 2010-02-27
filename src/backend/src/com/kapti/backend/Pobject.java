/*
 * Pobject.java
 * StockPlay - Container voor persistente data.
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
package com.kapti.backend;

/**
 * \brief Container voor persistente data.
 *
 * Deze klasse wordt gebruik om persistente data op te slaan, zoals referenties
 * naar dataobjecten. De klasse wordt slechts één keer geinstantieerd, en die
 * referentie zal steeds doorgegeven worden aan alle method handlers.
 * Aangezien de functies uit deze klasse uit verschillende threads
 * gebruikt kunnen worden, kan het nuttig zijn om deze (of de onderliggende)
 * functies gesynchroniseerd te maken.
 */
public class Pobject {
    //
    // Dataleden
    //

    private String storedString = "";


    //
    // Methodes
    //


    // Voorbeeldfunctionaliteit, niet benodigd voor ons //
    public synchronized void setString(String string) {
        storedString = string;
    }

    public synchronized String getString() {
        return storedString;
    }
}

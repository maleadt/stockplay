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
package com.kapti.backend.api.system;

import com.kapti.backend.api.MethodClass;
import com.kapti.exceptions.StockPlayException;
import java.util.Hashtable;

/**
 * \brief   Handler van de System.Scraper subklasse.
 *
 * Deze klasse is de handler van de System.Scraper subklasse. Ze staat in
 * voor de verwerking van aanroepen van functies die zich in deze klasse
 * bevinden, lokaal de correcte aanvragen uit te voeren, en het resultaat
 * op conforme wijze terug te sturen.
 */
public class ScraperHandler extends MethodClass {
    //
    // Methodes
    //

    public int Status() throws StockPlayException {
        return 1;
    }

    public boolean Stop() throws StockPlayException {
        return false;
    }

    public boolean Restart() throws StockPlayException {
        return true;
    }
    
    public Hashtable<String, Object> Stats() throws StockPlayException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
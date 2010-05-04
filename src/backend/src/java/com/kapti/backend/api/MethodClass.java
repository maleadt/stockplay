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
package com.kapti.backend.api;

import com.kapti.data.persistence.StockPlayDAO;
import org.apache.log4j.Logger;

/**
 * \brief Hoofdklasse voor method handlers.
 *
 * Dit is de interface die moet geimplementeerd worden door alle method handlers.
 * Ze voorziet in de functies gerelateerd met het opslaan van de persistente
 * data.
 */
public abstract class MethodClass {
    //
    // Constanten
    //

    public final static int PROTOCOL_VERSION = 1;
    

    //
    // Dataleden
    //

    private StockPlayDAO mDAO = null;

    static Logger mLogger;


    //
    // Constructie
    //

    public MethodClass() {
        mLogger = Logger.getLogger(this.getClass());
    }


    //
    // Methodes
    //

    /**
     * Deze initialisatiefunctie wordt aangeroepen vooraleer de method handler
     * een functie moet afhandelen, en moet dusdanig geimplementeerd worden
     * zodat het doorgegeven object lokaal opgeslaan wordt voor verder gebruik.
     */
    public void init(StockPlayDAO iDAO) {
        mDAO = iDAO;
    }
    
    protected StockPlayDAO getDAO() {
        return mDAO;
    }

    protected Logger getLogger() {
        return mLogger;
    }
}

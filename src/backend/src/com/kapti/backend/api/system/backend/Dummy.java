/*
 * Dummy.java
 * StockPlay - Dummy implementatie van de System.Backend subklasse.
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
package com.kapti.backend.api.system.backend;

import com.kapti.backend.Pobject;
import com.kapti.backend.api.system.IBackend;
import java.util.Hashtable;

/**
 * \brief   Dummy implementatie van de System.Backend interface.
 *
 * Deze klasse is een dummy implementatie van de System.Backend interface. Een
 * dergelijke implementatie geeft valide data terug, zonder daarvoor de database
 * te raadplegen. Deze implementatie kan zo gebruikt worden om een client-systeem
 * te testen.
 */
public class Dummy implements IBackend {
    //
    // Dataleden
    //

    private Pobject pobject = null;


    //
    // Methodes
    //

    public void init(Pobject pobject) {
        this.pobject = pobject;
    }

    public int Status() {
        return 1;
    }

    public boolean Stop() {
        return false;
    }

    public boolean Restart() {
        return true;
    }

    public Hashtable<String, Object> Stats() {
        Hashtable<String, Object> oStats = new Hashtable<String, Object>();
        oStats.put("users", 5);
        oStats.put("req", 100);
        oStats.put("uptime", 12345);
        return oStats;
    }

}


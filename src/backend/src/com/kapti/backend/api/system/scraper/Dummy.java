/*
 * Dummy.java
 * StockPlay - API System.Scraper subclass dummy implementation
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
package com.kapti.backend.api.system.scraper;

import com.kapti.backend.Pobject;
import com.kapti.backend.api.system.IDatabase;

/**
 *
 * @author tim
 */
public class Dummy implements IDatabase {

    private Pobject pobject = null;

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
}


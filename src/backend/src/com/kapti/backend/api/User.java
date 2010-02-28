/*
 * User.java
 * StockPlay - Abstracte klasse vab de User interface.
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
 * \brief Abstracte klasse vab de System interface.
 *
 * Deze klasse voorziet in functiesignaturen zoals voorgeschreven in de
 * protocoldefinitie van de System klasse.
 */
public abstract class User extends MethodClass {
    //
    // Methodes
    //
    
    public abstract int Hello(String iClient, int iProtocolVersion) throws XmlRpcException;
}

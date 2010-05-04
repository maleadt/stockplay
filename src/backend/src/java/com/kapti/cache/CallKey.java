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
package com.kapti.cache;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * \brief   Object dat een methodeaanroep omschrijft
 *
 * Deze klasse voorziet in een object dat een methodeaanroep volledig omschrijft.
 * Als het object waarop de methode moet aangeroepen gekend is, kan men dus zo
 * volledig een functieaanroep uitvoeren. Dit wordt gebruikt om los van een
 * gebruikersquery een cache toch op te vullen met de resultaten van bepaalde
 * opgeslagen queries, waarbij die queries gereproduceerd worden met behulp van
 * een object van deze klasse.
 *
 * De klasse kan ook gebruikt worden om een content-sensitive hashcode te
 * genereren, die de aanroep eenduidig omschrijft maar toch niet gevoelig is
 * voor niet-inhoudsgerelateerde bewerkingen (zoals het kopiëren van een object).
 */
public class CallKey implements Serializable {

    public final Method method;
    public final Object[] args;

    public CallKey(Method method, Object[] args) {
        this.method = method;
        this.args = args;
    }

    @Override
    public int hashCode() {
        // Hash method name
        int code = method.getName().hashCode();

        // Hash arguments
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                code = (31 * code) + args[i].hashCode();
            }
        }

        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof CallKey)) {
            return false;
        }

        final CallKey callKey = (CallKey) o;

        if (!method.equals(callKey.method)) {
            return false;
        }

        if (!Arrays.equals(args, callKey.args)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return Integer.toString(hashCode());
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(method.getName());
        //if (args != null)
        //    stream.writeObject(args);
    }
}

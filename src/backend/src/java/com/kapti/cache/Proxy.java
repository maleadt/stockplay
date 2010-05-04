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

import net.sf.cache4j.Cache;

/**
 * \brief   Cache proxy object
 *
 * Deze klasse wordt gebruikt om transparant methode-invokaties te herleiden
 * via een aparte klasse, de InvocationManager. Dit wordt gedaan zodat de
 * waarden niet continu opnieuw gegenereerd worden, maar via een eventuele
 * cache kunnen opgevangen worden.
 */
public class Proxy {

    private static final ClassLoader CLASS_LOADER = Proxy.class.getClassLoader();

    public static Object newProxyInstance(Object target, Class iface, Cache cache) throws IllegalArgumentException {
        InvocationHandler handler = null;
        Class[] ifaces = new Class[]{iface};

        handler = new InvocationHandler(target, cache);

        Manager.setup(cache, target);
        Object proxy = java.lang.reflect.Proxy.newProxyInstance(CLASS_LOADER, ifaces, handler);
        return proxy;
    }
}

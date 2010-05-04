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

import com.kapti.cache.Annotations.Cachable;
import com.kapti.cache.Annotations.Invalidates;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import net.sf.cache4j.Cache;
import net.sf.cache4j.CacheException;
import org.apache.log4j.Logger;

/**
 * \brief   InvocationHandler voor objecten beheerd door de caching proxy
 *
 * Deze klasse is 1 van de belangrijke pijlers bij het oproepen van een methode
 * op een object dat geregistreert is bij de proxy cache. Daarbij zal eerst
 * de cache geraadpleegd worden, waarna eventueel toch een waarde opgevraagd
 * wordt aan het effectieve object. Die functionaliteit zit vervat in de
 * invoke() methode van deze klasse, en wordt opgeroepen door het proxyobject,
 * dat voor de gebruiker als vervanging dient voor het originele object en zo
 * transparant in gebruik is.
 */
public class InvocationHandler implements java.lang.reflect.InvocationHandler {
    private static Logger mLogger = Logger.getLogger(InvocationHandler.class);
    private Cache cache = null;
    private Object target = null;

    public InvocationHandler(Object target, Cache cache) {
        this.cache = cache;
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Generate a hashcode
        CallKey callKey = null;
        callKey = new CallKey(method, args);

        // Load the annotations
        Annotation tCachable = method.getAnnotation(Cachable.class);
        Annotation tInvalidates = method.getAnnotation(Invalidates.class);

        // Invalidate
        if (cache != null && tInvalidates != null) {
            mLogger.debug("invalidating " + cache.getCacheConfig().getCacheId() + ".*");
            cache.clear();
        }

        // Look in cache
        Object result = null;
        if (cache != null && tCachable != null) {
            try {
                result = cache.get(callKey);
            } catch (CacheException ce) {
                mLogger.error("could not fetch cache entry", ce);
            }
        }

        // Return if found
        if (result != null) {
            Manager.hit(cache, callKey);
            return result;
        }

        // Instantiate
        result = method.invoke(target, args);

        // Save
        if (cache != null && tCachable != null) {
            Manager.miss(cache, callKey);
            try {
                cache.put(callKey, result);
            } catch (CacheException ce) {
                mLogger.error("could not save cache entry", ce);
            }
        }

        return result;
    }
}
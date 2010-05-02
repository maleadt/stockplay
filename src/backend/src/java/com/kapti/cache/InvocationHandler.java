/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author tim
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
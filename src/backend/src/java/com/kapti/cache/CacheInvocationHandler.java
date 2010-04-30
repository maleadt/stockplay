/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.cache;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import net.sf.cache4j.Cache;
import net.sf.cache4j.CacheException;
import net.sf.cache4j.impl.Utils;
import org.apache.log4j.Logger;

/**
 *
 * @author tim
 */
public class CacheInvocationHandler implements InvocationHandler {
    private static Logger mLogger = Logger.getLogger(CacheInvocationHandler.class);
    private Cache cache = null;
    private Object target = null;

    public CacheInvocationHandler(Object target, Cache cache) {
        this.cache = cache;
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Throwable throwable = null;

        CallKey callKey = null;
        callKey = new CallKey(method.getName(), args);
        mLogger.error("Hashkey is " + callKey.hashCode());
        // TODO: filter HashCode

        // Look in cache
        Object result = null;
        if (cache != null) {
            mLogger.error("looking in cache");
            try {
                result = cache.get(callKey);
            } catch (CacheException ce) {
                mLogger.error("could not fetch cache entry", ce);
            }
        }

        // Return if found
        if (result != null) {
            mLogger.error("found in cache");
            return result;
        }

        // Instantiate
            mLogger.error("instantiating");
        result = method.invoke(target, args);

        // Save
        try {
            mLogger.error("saving in cache");
            cache.put(callKey, result);
        } catch (CacheException ce) {
            mLogger.error("Object size is " + Utils.size(result));
            mLogger.error("could not save cache entry", ce);
        }

        return result;
    }
}

class CallKey {

    private final String methodName;
    private final Object[] args;

    public CallKey(String methodName, Object[] args) {
        this.methodName = methodName;
        this.args = args;
    }

    @Override
    public int hashCode() {
        // TODO: hashcode for filter
        
        // Hash method name
        int code = methodName.hashCode();

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

        if (!methodName.equals(callKey.methodName)) {
            return false;
        }

        if (!Arrays.equals(args, callKey.args)) {
            return false;
        }

        return true;
    }
}

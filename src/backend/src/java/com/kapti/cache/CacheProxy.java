/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.cache;

import net.sf.cache4j.Cache;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 *
 * @author tim
 */
public class CacheProxy {

    private static final ClassLoader DEFAULT_CLASS_LOADER = CacheProxy.class.getClassLoader();

    public static Object newProxyInstance(ClassLoader loader, Object target, Class iface, Cache cache) throws IllegalArgumentException {
        InvocationHandler handler = null;
        Class[] ifaces = new Class[]{iface};

        handler = new CacheInvocationHandler(target, cache);

        return Proxy.newProxyInstance(loader, ifaces, handler);
    }

    public static Object newProxyInstance(Object target, Class iface, Cache cache) throws IllegalArgumentException {
        return newProxyInstance(DEFAULT_CLASS_LOADER, target, iface, cache);
    }
}

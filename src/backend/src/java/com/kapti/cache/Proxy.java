
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.cache;

import net.sf.cache4j.Cache;

/**
 *
 * @author tim
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

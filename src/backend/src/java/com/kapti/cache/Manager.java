/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.cache;

import java.io.InputStream;
import net.sf.cache4j.Cache;
import net.sf.cache4j.CacheException;
import net.sf.cache4j.CacheFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author tim
 */
public class Manager {
    private static Logger mLogger = Logger.getLogger(Manager.class);
    private static CacheFactory mFactory = getFactory();

    private static CacheFactory getFactory() {
        CacheFactory tFactory = CacheFactory.getInstance();
        try {
            InputStream tStream = Manager.class.getResourceAsStream("/cache4j_config.xml");
            if (tStream == null) {
                mLogger.error("could not find cache configuration directives");
                return null;
            }
            tFactory.loadConfig(tStream);
        }
        catch (CacheException e) {
            mLogger.error("could not configure cache factory appropriately", e);
            return null;
        }
        return tFactory;
    }

    public static Cache getCache(String iCache) {
        Cache oCache = null;
        if (mFactory == null) {
            mLogger.error("factory not ready for usage, cannot instantiate cache '" + iCache + "'");
            return null;
        }
        try {
            oCache = mFactory.getCache(iCache);
            if (oCache == null)
                throw new CacheException("cache is null!");
        }
        catch (CacheException e) {
            mLogger.error("could not load cache '" + iCache + "'", e);
        }
        return oCache;
    }
}

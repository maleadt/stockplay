/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.cache;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import net.sf.cache4j.Cache;
import net.sf.cache4j.CacheException;
import net.sf.cache4j.CacheFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author tim
 */
public class CacheManager {
    private static Logger mLogger = Logger.getLogger(CacheManager.class);
    private static CacheFactory mFactory = getFactory();

    private static CacheFactory getFactory() {
        CacheFactory tFactory = CacheFactory.getInstance();
        try {
            InputStream tStream = CacheManager.class.getResourceAsStream("/cache4j_config.xml");
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

    public static Cache getCache(Class iClass) {
        Cache oCache = null;
        if (mFactory == null) {
            mLogger.error("factory not ready for usage, cannot instantiate cache " + iClass.getName());
            return null;
        }
        try {
            oCache = mFactory.getCache(iClass.getName());
            if (oCache == null)
                throw new CacheException("cache is null!");
        }
        catch (CacheException e) {
            mLogger.error("could not load cache " + iClass.getName(), e);
        }
        return oCache;
    }
}

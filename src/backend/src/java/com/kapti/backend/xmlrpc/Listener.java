/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.backend.xmlrpc;

import java.lang.reflect.Field;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import net.sf.cache4j.CacheCleaner;
import net.sf.cache4j.CacheException;
import net.sf.cache4j.CacheFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author tim
 */
public class Listener implements ServletContextListener {
    //
    // Dataleden
    //

    static Logger mLogger = Logger.getLogger(Servlet.class);


    //
    // Methoden
    //

    public void contextDestroyed(ServletContextEvent sce) {
        mLogger.info("destroying context");
        
        // Remove all caches
        CacheFactory tFactory = CacheFactory.getInstance();
        for (Object tCacheID : tFactory.getCacheIds()) {
            try {
                tFactory.removeCache(tCacheID);
            } catch (CacheException ce) {
                mLogger.error("could not remove cache " + tCacheID, ce);
            }
        }

        // Stop the cache cleaner (this is a hack)
        try {
            Field tCleanerField = tFactory.getClass().getDeclaredField("_cleaner");
            tCleanerField.setAccessible(true);
            Object tCleaner = new Object();
            tCleaner = tCleanerField.get(tFactory);
            ((CacheCleaner)tCleaner).stop();
        } catch (NoSuchFieldException e) {
            mLogger.error("could not access cleaner field", e);
        }
        catch (IllegalAccessException e) {
            mLogger.error("could not get cleaner field", e);
        }
        catch (Exception e) {
            mLogger.error("error occured while killing cleaner thread", e);
        }
    }

    public void contextInitialized(ServletContextEvent sce) {
        mLogger.info("initialising context");
    }

}

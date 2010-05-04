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
package com.kapti.backend.xmlrpc;

import java.lang.reflect.Field;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import net.sf.cache4j.CacheCleaner;
import net.sf.cache4j.CacheException;
import net.sf.cache4j.CacheFactory;
import org.apache.log4j.Logger;

/**
 * \brief   ContextListener voor de XMLRPC servlet.
 *
 * Deze klasse wordt ingehaakt in de Tomcat server om correct te reageren op
 * contextveranderingen. Zo zal het destroyen van de context als gevolg hebben
 * dat eventuele caches geledigd worden.
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

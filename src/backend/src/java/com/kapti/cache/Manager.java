/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.cache;

import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import net.sf.cache4j.Cache;
import net.sf.cache4j.CacheException;
import net.sf.cache4j.CacheFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author tim
 */
public class Manager extends TimerTask {
    //
    // Member data
    //

    private static Logger mLogger = Logger.getLogger(Manager.class);
    private static CacheFactory mFactory = getFactory();
    private static Map<Cache, Object> mCaches = new HashMap<Cache, Object>();
    private static Map<Cache, Stats> mCacheStats = new HashMap<Cache, Stats>();
    private static int mManagerMisses = 0;
    private static int mManagerClears = 0;
    private final static int LIMIT_KEYS = 25;


    //
    // Construction
    //

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


    //
    // Methods
    //

    public static void setup(Cache cache, Object proxy) {
        mCacheStats.put(cache, new Stats());
        mCaches.put(cache, proxy);
    }

    public static void miss(Cache cache, final CallKey callKey) {
        mLogger.debug("cache miss on entry " + cache.getCacheConfig().getCacheId() + "." + callKey.method.getName());
        mManagerMisses++;
        if (! mCacheStats.containsKey(cache)) {
            return;
        }
        mCacheStats.get(cache).misses++;
        mCacheStats.get(cache).add(callKey);
    }

    public static void hit(Cache cache, final CallKey callKey) {
        mLogger.debug("cache hit on entry " + cache.getCacheConfig().getCacheId() + "." + callKey.method.getName());
        if (! mCacheStats.containsKey(cache)) {
            return;
        }
        mCacheStats.get(cache).hits++;
        mCacheStats.get(cache).add(callKey);
    }

    public static void clear() {
        mLogger.info("clearing cache");
        mManagerClears++;
        for (Cache cache : mCacheStats.keySet()) {
            try {
                cache.clear();
            } catch (CacheException ce) {
                mLogger.error("could not clear cache " + cache.getCacheConfig().getCacheId(), ce);
            }
            mCacheStats.get(cache).clear();
        }

    }

    public void run() {
        // Check if the backend is idle
        boolean isIdle = (mManagerMisses == 0);
        mManagerMisses = 0;

        // Only run if we got caches registered
        if (mCaches.size() == 0)
            return;

        // Process all caches
        int tRefreshCount = 0;
        for (Cache cache : mCaches.keySet()) {
            Object target = mCaches.get(cache);
            Map<CallKey, Entry> tEntries = mCacheStats.get(cache).entries;

            // Update and skim the entries
            CallKey[] tCallKeys = new CallKey[tEntries.size()];
            tEntries.keySet().toArray(tCallKeys);
            for (int i = 0; i < tCallKeys.length; i++) {
                Entry entry = tEntries.get(tCallKeys[i]);
                entry.cycleratio *= 0.95;
                if (entry.count > 0) {
                    entry.count = 0;
                    entry.cycleratio += 10;
                }
            }
            final Map<CallKey, Entry> tEntriesFinal = tEntries;
            Arrays.sort(tCallKeys, new Comparator<CallKey>() {
                public int compare(CallKey a, CallKey b) {
                    return (int)(tEntriesFinal.get(b).cycleratio - tEntriesFinal.get(a).cycleratio);
                }
            });
            for (int i = LIMIT_KEYS; i < tCallKeys.length; i++) {
                tEntries.remove(tCallKeys[i]);
            }

            // Refresh the remaining entries
            if (isIdle) {
                for (CallKey callKey : tEntries.keySet()) {
                    try {
                        if (cache.get(callKey) == null) {
                            Object result = callKey.method.invoke(target, callKey.args);
                            try {
                                mLogger.debug("refreshing cache entry " + cache.getCacheConfig().getCacheId() + "." + callKey.method.getName());
                                cache.put(callKey, result);
                                tRefreshCount++;
                            } catch (CacheException ce) {
                                mLogger.error("could not refresh cache entry", ce);
                            }
                        }
                    }
                    catch (CacheException ce) {
                        mLogger.error("could not request cache entry", ce);
                    }
                    catch (IllegalAccessException e) {
                        mLogger.error("access to method denied while refreshing cache", e);
                    }
                    catch (InvocationTargetException e) {
                        mLogger.error("exception thrown while refreshing cache", e);
                    }
                }
            }
        }
        if (tRefreshCount > 0) {
            mLogger.debug("refreshed " + tRefreshCount + " cache entrie while the backend was idle");
        }
    }

    public static Map<Cache, ManagerInfo> getManagerInfo() throws Throwable {
        Map<Cache, ManagerInfo> oStats = new HashMap<Cache, ManagerInfo>();

        for (Cache tCache : mCacheStats.keySet()) {
            Stats tStat = mCacheStats.get(tCache);
            ManagerInfo tInfo = new ManagerInfo(
                    tStat.entries.size(),
                    tStat.hits,
                    tStat.misses,
                    //Utils.size(mCacheStats.get(tCache))
                    -1,
                    tStat.entries
            );

            oStats.put(tCache, tInfo);
        }

        return oStats;
    }

    public static int getManagerClears() {
        return mManagerClears;
    }
    
    
    //
    // Subclasses
    //

    public static class ManagerInfo {
        final public int keys;
        final public int hits, misses;
        final public int size;
        final public Map<CallKey, Entry> entries;

        public ManagerInfo(int keys, int hits, int misses, int size, Map<CallKey, Entry> entries) {
            this.keys = keys;
            this.hits = hits;
            this.misses = misses;
            this.size = size;
            this.entries = entries;
        }
    }

    private static class Stats implements Serializable {
        public int hits, misses;
        public Map<CallKey, Entry> entries;

        public Stats() {
            entries = new HashMap<CallKey, Entry>();
            hits = 0;
            misses = 0;
        }

        public void add(final CallKey callkey) {
            if (! entries.containsKey(callkey)) {
                entries.put(callkey, new Entry());
            }
            entries.get(callkey).count++;
        }

        public void clear() {
            entries.clear();
        }
    }

    public static class Entry implements Serializable {
        public int count;
        public double cycleratio;

        public Entry() {
            count = 0;
            cycleratio = 0;
        }
    }
}

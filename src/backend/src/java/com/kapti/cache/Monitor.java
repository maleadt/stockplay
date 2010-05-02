/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.cache;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import net.sf.cache4j.Cache;
import net.sf.cache4j.CacheException;
import org.apache.log4j.Logger;

/**
 *
 * @author tim
 */
public class Monitor extends TimerTask {
    private static Logger mLogger = Logger.getLogger(Monitor.class);
    private static Map<Cache, Object> mCaches = new HashMap<Cache, Object>();
    private static Map<Cache, Stats> mCacheStats = new HashMap<Cache, Stats>();
    private static int mCacheMisses = 0;

    public static void setup(Cache cache, Object proxy) {
        mCacheStats.put(cache, new Stats());
        mCaches.put(cache, proxy);
    }

    public static void add(Cache cache, final CallKey callKey) {
        mCacheMisses++;
        if (! mCacheStats.containsKey(cache)) {
            return;
        }

        mCacheStats.get(cache).add(callKey);
    }

    public void run() {
        // Check if the backend is idle
        boolean isIdle = (mCacheMisses == 0);
        mCacheMisses = 0;

        // Process all caches
        for (Cache cache : mCaches.keySet()) {
            Object target = mCaches.get(cache);
            Map<CallKey, Entry> tEntries = mCacheStats.get(cache).entries;

            // Update and skim the entries
            CallKey[] tCallKeys = new CallKey[tEntries.size()];
            tEntries.keySet().toArray(tCallKeys);
            for (int i = 0; i < tCallKeys.length; i++) {
                Entry entry = tEntries.get(tCallKeys[i]);
                if (entry.count > 0) {
                    entry.count = 0;
                    entry.cycleratio += 2;
                } else {
                    if (entry.cycleratio > 0)
                        entry.cycleratio -= 1;
                }
            }
            final Map<CallKey, Entry> tEntriesFinal = tEntries;
            Arrays.sort(tCallKeys, new Comparator<CallKey>() {
                public int compare(CallKey a, CallKey b) {
                    return tEntriesFinal.get(b).cycleratio - tEntriesFinal.get(a).cycleratio;
                }
            });
            for (int i = 10; i < tCallKeys.length; i++) {
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
    }
}

class Stats {
    public Map<CallKey, Entry> entries;

    public Stats() {
        entries = new HashMap<CallKey, Entry>();
    }

    public void add(final CallKey callkey) {
        if (! entries.containsKey(callkey)) {
            entries.put(callkey, new Entry());
        }
        entries.get(callkey).count++;
    }
}

class Entry {
    public Integer count, cycleratio;

    public Entry() {
        count = 0;
        cycleratio = 0;
    }
}
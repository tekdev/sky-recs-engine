package com.sky.assignment.Cache;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class CacheService<K, V> {
    private final Logger log = Logger.getLogger(CacheService.class);
    private final ConcurrentMap<K, Future<V>> cache = new ConcurrentHashMap<>();


    public V getValue(final K key, final Callable<V> callable) throws InterruptedException, ExecutionException {
        evictExpiredCache();
        try {
            final Future<V> future = createFutureIfAbsent(key, callable);
            return future.get();
        } catch (final InterruptedException | ExecutionException | RuntimeException e) {
            cache.remove(key);
            throw e;
        }
    }

    public void setValueIfAbsent(final K key, final V value) {
        createFutureIfAbsent(key, new Callable<V>() {
            @Override
            public V call() throws Exception {
                return value;
            }
        });
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }

    public int cacheSize() {
        return cache.size();
    }

    public void clearCache() {
        cache.clear();
    }

    private Future<V> createFutureIfAbsent(final K key, final Callable<V> callable) {
        Future<V> future = cache.get(key);
        if (future == null) {
            final FutureTask<V> futureTask = new FutureTask<V>(callable);
            future = cache.putIfAbsent(key, futureTask);
            if (future == null) {
                future = futureTask;
                futureTask.run();
            }
        }
        return future;
    }

    private void evictExpiredCache() {
        if (!cache.values().isEmpty()) {
            Iterator<Map.Entry<K, Future<V>>> iterator = cache.entrySet().iterator();
            do {
                try {
                    CacheEntry entry = (CacheEntry) iterator.next().getValue().get();
                    if ((System.currentTimeMillis() - entry.getEntryTime() >= entry.getEntryExpireTimeLimit())) {
                        iterator.remove();
                        log.info("cleaned up some expired recommendations");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    log.error("error at evicting entries", e);
                }
            } while (iterator.hasNext());
        }
    }
}
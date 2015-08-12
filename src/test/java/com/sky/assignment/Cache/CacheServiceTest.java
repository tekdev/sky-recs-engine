package com.sky.assignment.Cache;

import com.sky.assignment.model.Recommendation;
import com.sky.assignment.model.Recommendations;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;


public class CacheServiceTest extends TestCase {

    private CacheService cacheService;
    private Recommendations recommendations;
    private String subscriber = "mo";

    public void setUp() throws Exception {
        cacheService = new CacheService();
        recommendations = getRecommendations(5);
    }

    public void testSetAndGetValue() throws Exception {
        assertTrue(cacheService.isEmpty());
        // testing writing to  the cache
        cacheService.setValueIfAbsent(subscriber.hashCode(), new CacheEntry(recommendations, 5));
        assertTrue(cacheService.cacheSize() == 1);

        //testing reading from the cache
        CacheEntry entry = (CacheEntry) cacheService.getValue(subscriber.hashCode(), new Callable() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        });

        assertTrue("expected recommendations should be equal to the cached one", recommendations.toString().equals(entry.getRecommendations().toString()));
    }

    public void testEvictionAfterExpiry() throws Exception {
        cacheService.clearCache();
        cacheService.setValueIfAbsent(subscriber.hashCode(), new CacheEntry(recommendations, 1000));
        Thread.sleep(1000);
        assertTrue(cacheService.cacheSize() == 1);
        CacheEntry entry = (CacheEntry) cacheService.getValue(subscriber.hashCode(), new Callable() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        });
        assertTrue(entry == null);


    }

    private Recommendations getRecommendations(long numberOfRecs) {
        List<Recommendation> recommendations = new ArrayList<Recommendation>();
        for (int i = 0; i < numberOfRecs; i++) {
            recommendations.add(new Recommendation(UUID.randomUUID().toString(), 1439151812678L, 1439156252678L));
        }
        return new Recommendations(recommendations);
    }
}
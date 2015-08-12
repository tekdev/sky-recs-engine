package com.sky.assignment.Cache;

import com.sky.assignment.model.Recommendations;

import java.util.concurrent.TimeUnit;

public class CacheEntry {
    private Recommendations recommendations;
    private long entryTime = System.currentTimeMillis();
    private long entryExpireTimeLimit;

    public CacheEntry(Recommendations recommendations, long entryExpireTimeLimit) {
        this.recommendations = recommendations;
        this.entryExpireTimeLimit = entryExpireTimeLimit;
    }

    public Recommendations getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(Recommendations recommendations) {
        this.recommendations = recommendations;
    }

    public long getEntryTime() {
        return entryTime;
    }

    public long getEntryExpireTimeLimit() {
        return entryExpireTimeLimit;
    }
}

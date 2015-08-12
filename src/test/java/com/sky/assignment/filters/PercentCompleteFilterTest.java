package com.sky.assignment.filters;

import com.sky.assignment.model.Recommendation;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PercentCompleteFilterTest {
    private long currentTime;
    private RecFilter percentCompleteFilter;
    long requestedStart;
    long requestedEnd;
    Recommendation recs;

    @Before
    public void init() {
        currentTime = System.currentTimeMillis();
        percentCompleteFilter = new PercentCompleteFilter();
        recs = new Recommendation(UUID.randomUUID().toString(), currentTime, currentTime + 3600000);
    }

    @Test
    public void testBelow60StartTime() {
        requestedStart = currentTime + 2160000l; // cuurentTime + 36 mn
        requestedEnd = requestedStart + 3600000; // requestedStart + 60 mn
        assertTrue("Expecting \"percentCompleteFilter.isRelevant\" to return true ", percentCompleteFilter.isRelevant(recs, requestedStart, requestedEnd));
    }


    @Test
    public void testPast60StartTime() {
        requestedStart = currentTime + 2400000; // cuurentTime + 40 mn
        requestedEnd = requestedStart + 3600000; // requestedStart + 60 mn
        assertFalse("Expecting \"percentCompleteFilter.isRelevant\" to return false ", percentCompleteFilter.isRelevant(recs, requestedStart, requestedEnd));
    }
}

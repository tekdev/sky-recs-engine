package com.sky.assignment.filters;

import com.sky.assignment.model.Recommendation;
import org.springframework.stereotype.Component;

@Component
public class PercentCompleteFilter implements RecFilter {

    /**
     * this filter should discard recommendations that running time is past 60%
     * for example if recommendation start time is 8:00 and end time is 9:00 then
     * this recommendation should be discarded if timeslot start is past 8:36, which is 60% of total show time
     *
     * @param r
     * @param start
     * @param end
     * @return
     */
    @Override
    public boolean isRelevant(Recommendation r, long start, long end) {
        final double PERCENTAGE = 0.6;
        long cutOffTime = (long) (((r.end - r.start) * PERCENTAGE) + r.start);
        return (start <= cutOffTime);
    }
}

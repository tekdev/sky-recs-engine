package com.sky.assignment.filters;

import com.sky.assignment.model.Recommendation;

public interface RecFilter {

    public boolean isRelevant(Recommendation r, long start, long end);
}

package com.sky.assignment.controllers;

import com.sky.assignment.Cache.CacheEntry;
import com.sky.assignment.Cache.CacheService;
import com.sky.assignment.internal.RecsEngine;
import com.sky.assignment.model.Recommendations;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/recs")
public class RecsController {
    @Autowired
    private CacheService cacheService;
    private RecsEngine recsEngine;
    private final Logger log = Logger.getLogger(RecsController.class);
    private long evictionTimeLimit = 300000; // in milliseconds

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Autowired
    public RecsController(RecsEngine recsEngine) {
        this.recsEngine = recsEngine;
    }

    @RequestMapping(value = {"/personalised"}, method = RequestMethod.GET)
    @ResponseBody
    public Recommendations getPersonalisedRecommendations(@RequestParam("num") final Long numberOfRecs,
                                                          @RequestParam("start") final Long start,
                                                          @RequestParam("end") final Long end,
                                                          @RequestParam("subscriber") String subscriber) {
        CacheEntry cacheEntry = null;
        try {
            cacheEntry = (CacheEntry) cacheService.getValue(subscriber.hashCode(), new Callable() {
                @Override
                public CacheEntry call() throws Exception {
                    return new CacheEntry(recsEngine.recommend(numberOfRecs, start, end), evictionTimeLimit);
                }
            });
        } catch (InterruptedException | ExecutionException e) {
            log.error(e);
        }
        return cacheEntry.getRecommendations();
    }
}

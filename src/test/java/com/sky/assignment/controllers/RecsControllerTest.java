package com.sky.assignment.controllers;

import com.sky.assignment.Application;
import com.sky.assignment.Cache.CacheEntry;
import com.sky.assignment.Cache.CacheService;
import com.sky.assignment.internal.RecsEngine;
import com.sky.assignment.model.Recommendation;
import com.sky.assignment.model.Recommendations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port=8000")
public class RecsControllerTest {
    private final String BASE_URL = "http://localhost:8000/recs/personalised";
    private long start = 1439115819295L;
    private long end = 1441914309000L;
    private String subscriber = "mo";
    private long numOfRecs = 5L;
    private Recommendations recommendations;
    private final String REQUEST = BASE_URL + "?num=" + numOfRecs + "&start=" + start + "&end=" + end + "&subscriber=" + subscriber;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Mock
    private RecsEngine recsEngine;

    @Mock
    private CacheService cacheService;

    @InjectMocks
    private RecsController recsController;

    @Before
    public void init() {
        recsController = new RecsController(recsEngine);
        MockitoAnnotations.initMocks(this);
        recsController.setCacheService(cacheService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(recsController).build();
    }

    @Test
    public void testGetRecommendations() throws Exception {

        recommendations = getRecommendations(numOfRecs);
        // when(recsEngine.recommend(numOfRecs, start, end)).thenReturn(recommendations);
        when(cacheService.getValue(any(int.class), any(Callable.class))).thenReturn(new CacheEntry(recommendations,30000));

        String resp = this.mockMvc.perform(get(REQUEST))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(recommendations.toString().equals(resp));
    }

    private Recommendations getRecommendations(long numberOfRecs) {
        List<Recommendation> recommendations = new ArrayList<Recommendation>();
        for (int i = 0; i < numberOfRecs; i++) {
            recommendations.add(new Recommendation(UUID.randomUUID().toString(), 1439151812678L, 1439156252678L));
        }
        return new Recommendations(recommendations);
    }

    @Test
    public void testTheTest() {
        SimpleCacheManager cacheMng = (SimpleCacheManager) context.getBean("cacheManager");
        assertTrue(cacheMng != null);
    }
}
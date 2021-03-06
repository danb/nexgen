/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import biz.neustar.service.common.spring.ValidationUtil;
import biz.neustar.service.metrics.ws.model.ContextConfig;
import biz.neustar.service.metrics.ws.model.ContextProvider;
import biz.neustar.service.metrics.ws.model.Metric;
import biz.neustar.service.metrics.ws.model.MetricsDAO;
import biz.neustar.service.metrics.ws.model.QueryCriteria;
import biz.neustar.service.metrics.ws.model.QueryCriteriaBuilder;
import biz.neustar.service.metrics.ws.model.QueryRequest;
import biz.neustar.service.metrics.ws.model.QueryResponse;
import biz.neustar.service.metrics.ws.model.QueryResponseBuilder;
import biz.neustar.service.metrics.ws.model.validation.ContextConfigValidator;
import biz.neustar.service.metrics.ws.model.validation.MetricValidator;
import biz.neustar.service.metrics.ws.model.validation.QueryRequestValidator;

@Component
@Path("/metrics/v1/")
public class MetricsService {
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(MetricsService.class);
    

    @Autowired
    private ValidationUtil validationUtil;

    @Autowired // should be like a DAO or something
    private ContextProvider contextProvider;
    
    @Autowired
    private MetricsDAO metricsDAO;
    
    @GET
    @Path("/hello")
    @Produces({MediaType.TEXT_PLAIN})
    public String hello() {
        return "hello world!";
    }
    
    @POST
    @Path("/metrics")
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(List<Metric> metrics) {
        LOGGER.debug("Received: {}", metrics);
        
        validationUtil.validate(metrics, 
                new MetricValidator(contextProvider));

        for (Metric metric : metrics) {
            LOGGER.debug("Metric Received: {}", metric);
            metricsDAO.put(metric);
        }
    }
    
    @POST
    @Path("/config")
    @Consumes({MediaType.APPLICATION_JSON})
    public void createConfig(ContextConfig contextConfig) {
        LOGGER.debug("Received: {}", contextConfig);
        validationUtil.validate(contextConfig, 
                ContextConfigValidator.getDefaultInstance());
        LOGGER.debug("Validated {}", contextConfig);
        
        contextProvider.setContexts(contextConfig);
        LOGGER.debug("added Context config {}", contextConfig.getName());

    }
    
    @GET
    @Path("/query")
    @Produces({MediaType.APPLICATION_JSON})
    public QueryResponse query(@QueryParam("") QueryRequest query) {
    	LOGGER.debug("Request: " + query.toString());
    	validationUtil.validate(query, new QueryRequestValidator());
    	
    	// Build up the query criteria from the QueryRequest.    	
    	QueryCriteria qc = QueryCriteriaBuilder.buildQueryCriteria( query );
    	
    	metricsDAO.apply( qc );
    	
    	QueryResponse q = QueryResponseBuilder.buildQueryResponse( qc.getOperations( ) );
    	q.setRawDataCount(0);
        return q;
    }
}

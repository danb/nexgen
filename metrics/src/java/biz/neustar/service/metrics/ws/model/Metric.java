/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws.model;

import java.util.Map;

import biz.neustar.service.metrics.utils.ToStringUtil;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;

public class Metric {
    private String timestamp;   // required
    private String from;        // required
    private String host;        // required
    private String requestor; 
    private String resource;    
    private Map<String, String> values = Maps.newHashMap();

    
    
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
    
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getRequestor() {
        return requestor;
    }

    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }
   
    public String toString() {
        return Objects.toStringHelper(this)
            .add("timestamp", timestamp)
            .add("from", from)
            .add("host", host)
            .add("requestor", requestor)
            .add("resource", resource)
            .add("values", ToStringUtil.mapToString(values))
            .toString();
    }
}

package com.distance.optimizer.utils;

import com.distance.optimizer.exception.DistanceOptimizerException;
import com.google.common.collect.ListMultimap;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * @author FarazAhmed
 */
@Component
public class WebServiceUtils {

    private static final Logger LOGGER = Logger.getLogger(WebServiceUtils.class);

    public static <T extends Object> T get (String url, Map<String, String> queryParams, Map<String, String> headers, Class<? extends T> type) {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);
        WebResource webResource = client.resource(url);
        if (queryParams != null) {
            for (String key : queryParams.keySet()) {
                webResource = webResource.queryParam(key, queryParams.get(key));
            }
        }
        WebResource.Builder builder = webResource.getRequestBuilder();
        if(headers != null && !headers.isEmpty()) {
            for(Map.Entry<String, String> entry : headers.entrySet()) {
                builder = webResource.header(entry.getKey(), entry.getValue());
            }
        }

        return builder.accept(MediaType.APPLICATION_JSON).get(type);
    }

    public static <T> T get (String url, ListMultimap<String, String> queryParams, Map<String, String> headers, HttpResponseProcessing response) throws DistanceOptimizerException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        if (EntityHelper.isNotNull(queryParams)) {
            url += "?";
            Boolean isFirst = true;
            for (Map.Entry<String, String> entry : queryParams.entries()) {
                if (isFirst) {
                    url += (entry.getKey() + "=" + entry.getValue());
                    isFirst = false;
                }
                else {
                    url += ("&" + entry.getKey() + "=" + entry.getValue());
                }
            }
        }
        HttpGet request = new HttpGet(url);
        if (EntityHelper.isNotNull(headers)) {
            for(Map.Entry<String, String> entry : headers.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }
        try {
            HttpResponse res = httpClient.execute(request);
            if (res.getStatusLine().getStatusCode() != Response.Status.OK.getStatusCode()) {
                throw new DistanceOptimizerException(StatusCodes.FAILED_TO_GET_API_RESPONSE);
            }

            return (T)response.process(EntityUtils.toString(res.getEntity()));
        }
        catch (DistanceOptimizerException e) {
            LOGGER.error("Failed to get API Response");
            throw e;
        }
        catch (Exception e) {
            LOGGER.error("Failed to get API Response");
            throw new DistanceOptimizerException(StatusCodes.FAILED_TO_GET_API_RESPONSE);
        }
    }

    public static <T> T post (String url, String entity, ListMultimap<String, String> queryParams, Map<String, String> headers, HttpResponseProcessing response) throws DistanceOptimizerException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        if (EntityHelper.isNotNull(queryParams)) {
            url += "?";
            Boolean isFirst = true;
            for (Map.Entry<String, String> entry : queryParams.entries()) {
                if (isFirst) {
                    url += (entry.getKey() + "=" + entry.getValue());
                    isFirst = false;
                }
                else {
                    url += ("&" + entry.getKey() + "=" + entry.getValue());
                }
            }
        }
        HttpPost request = new HttpPost(url);
        if (EntityHelper.isNotNull(headers)) {
            for(Map.Entry<String, String> entry : headers.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }
        try {
            StringEntity stringEntity = new StringEntity(entity, ContentType.APPLICATION_JSON);
            request.setEntity(stringEntity);
            HttpResponse res = httpClient.execute(request);
            if (res.getStatusLine().getStatusCode() != Response.Status.OK.getStatusCode()) {
                throw new DistanceOptimizerException(StatusCodes.FAILED_TO_GET_API_RESPONSE);
            }

            return (T)response.process(EntityUtils.toString(res.getEntity()));
        }
        catch (DistanceOptimizerException e) {
            LOGGER.error("Failed to get API Response");
            throw e;
        }
        catch (Exception e) {
            LOGGER.error("Failed to get API Response");
            throw new DistanceOptimizerException(StatusCodes.FAILED_TO_GET_API_RESPONSE);
        }
    }

    /*public static <T> T put (String url, String com.distanceoptimizer.model.entity, ListMultimap<String, String> queryParams, Map<String, String> headers, HttpResponseProcessing response) throws BYSException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        if (EntityHelper.isNotNull(queryParams)) {
            url += "?";
            Boolean isFirst = true;
            for (Map.Entry<String, String> entry : queryParams.entries()) {
                if (isFirst) {
                    url += (entry.getKey() + "=" + entry.getValue());
                    isFirst = false;
                }
                else {
                    url += ("&" + entry.getKey() + "=" + entry.getValue());
                }
            }
        }
        HttpPut request = new HttpPut(url);
        if (EntityHelper.isNotNull(headers)) {
            for(Map.Entry<String, String> entry : headers.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }
        try {
            StringEntity stringEntity = new StringEntity(com.distanceoptimizer.model.entity, ContentType.create("application/json"));
            request.setEntity(stringEntity);
            HttpResponse res = httpClient.execute(request);
            if (res.getStatusLine().getStatusCode() != Response.Status.OK.getStatusCode()) {
                throw new BYSException(StatusCodes.FAILED_TO_GET_API_RESPONSE);
            }

            return (T)response.process(EntityUtils.toString(res.getEntity()));
        }
        catch (BYSException e) {
            log.error("Failed to get API Response");
            throw e;
        }
        catch (Exception e) {
            log.error("Failed to get API Response");
            throw new BYSException(StatusCodes.FAILED_TO_GET_API_RESPONSE);
        }
    }*/

}

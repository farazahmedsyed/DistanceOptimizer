package utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Venturedive on 20/10/2016.
 */
public class Response implements Serializable {
    public static final String OK = "OK";

    private ResponseHeader responseHeader=null;
    private Map<String, Object> responseBody = new HashMap<String, Object>();

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public Map<String, Object> getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(Map<String, Object> responseBody) {
        this.responseBody = responseBody;
    }

    public void setProperty(String key, Object value){
        responseBody.put(key, value);
    }
}

package utils;


import com.fasterxml.jackson.core.JsonParseException;
import exception.ROException;

import java.io.IOException;

/**
 * Created by Venturedive on 1/10/2017.
 */
public interface HttpResponseProcessing<T> {
    public T process(String responseString) throws ROException, IOException;
}

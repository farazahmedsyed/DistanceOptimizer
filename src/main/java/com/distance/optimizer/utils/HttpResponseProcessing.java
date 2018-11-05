package com.distance.optimizer.utils;


import com.distance.optimizer.exception.DistanceOptimizerException;

import java.io.IOException;

/**
 * @author FarazAhmed
 */
public interface HttpResponseProcessing<T> {
    T process(String responseString) throws DistanceOptimizerException, IOException;
}

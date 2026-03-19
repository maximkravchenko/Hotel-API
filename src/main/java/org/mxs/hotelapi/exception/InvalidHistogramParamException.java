package org.mxs.hotelapi.exception;

public class InvalidHistogramParamException extends RuntimeException {
    public InvalidHistogramParamException(String param) {
        super("Unknown histogram param: " + param);
    }
}

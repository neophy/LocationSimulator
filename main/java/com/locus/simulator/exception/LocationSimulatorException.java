package com.locus.simulator.exception;

import com.locus.simulator.codes.LocationSimulatorErrorCodes;

/**
 * Created by 16544 on 22/11/18.
 */
public class LocationSimulatorException extends java.lang.Exception {
    private static final long serialVersionUID = -7436056881934111681L;
    private final LocationSimulatorErrorCodes code;

    public LocationSimulatorException(String message, Throwable cause, LocationSimulatorErrorCodes errorCode) {
        super(message, cause);
        this.code = errorCode;
    }

    public LocationSimulatorException(String message, LocationSimulatorErrorCodes errorCode) {
        super(message);
        this.code = errorCode;
    }
    public LocationSimulatorErrorCodes getCode() {
        return this.code;
    }
}

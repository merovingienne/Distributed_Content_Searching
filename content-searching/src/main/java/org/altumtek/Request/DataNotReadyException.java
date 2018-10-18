package org.altumtek.Request;

/**
 * Exception to throw when a request does not have
 * any serialized data to send.
 * <p>
 * Created by chanuka on 10/14/18.
 */
public class DataNotReadyException extends Exception {

    private String errorMsg = "Data not ready! Serialize the data to be sent first.";

    public String getErrorMsg() {
        return errorMsg;
    }
}

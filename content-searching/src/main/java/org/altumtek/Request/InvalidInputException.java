package org.altumtek.Request;

/**
 * Created by chanuka on 11/24/18.
 */
public class InvalidInputException extends Exception {

    private String errorMsg = "Invalid data supplied to the constructor! Please check your input.";

    public String getErrorMsg() {
            return errorMsg;
        }

}

package org.altumtek.Request;

/**
 * Acknowledgement of UDP packet receipt.
 * Used to implement reliable communication.
 * <p>
 * Created by chanuka on 10/14/18.
 */
public class AcknowledgementRequest extends BaseRequest {

    public AcknowledgementRequest() {
        this.type = RequestType.ACK;
    }
}

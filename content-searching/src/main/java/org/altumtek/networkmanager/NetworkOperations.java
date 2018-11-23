package org.altumtek.networkmanager;

import org.altumtek.Request.BaseRequest;

public interface NetworkOperations {

    void decodeMessage(BaseRequest request);

    void encodeMessage(BaseRequest request);

    RouteTable getRouteTable();
}

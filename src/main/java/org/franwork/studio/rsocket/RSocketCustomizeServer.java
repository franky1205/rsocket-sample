package org.franwork.studio.rsocket;

import io.rsocket.Payload;
import io.rsocket.util.DefaultPayload;

/**
 * @author Frankie Chao by 2021-01-05.
 */
public interface RSocketCustomizeServer {

    default Payload[] getReturning(int numberOfElements) {
        Payload[] payloads = new Payload[numberOfElements];
        for (int i = 0 ; i < numberOfElements ; i++) {
            payloads[i] = DefaultPayload.create(String.valueOf(i + 1));
        }
        return payloads;
    }
}

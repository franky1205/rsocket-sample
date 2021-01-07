package org.franwork.studio.rsocket.client;

import io.rsocket.Payload;
import io.rsocket.util.DefaultPayload;

/**
 * @author Frankie Chao by 2021-01-05.
 */
public interface RSocketCustomizeClient {

    static Payload[] getClientPublish(int numberOfElements) {
        return getClientPublish("Request-", numberOfElements);
    }

    static Payload[] getClientPublish(String prefix, int numberOfElements) {
        Payload[] payloads = new Payload[numberOfElements];
        for (int i = 0 ; i < numberOfElements ; i++) {
            payloads[i] = DefaultPayload.create(prefix + (i + 1));
        }
        return payloads;
    }
}

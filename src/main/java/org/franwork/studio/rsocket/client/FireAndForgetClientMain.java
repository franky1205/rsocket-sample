package org.franwork.studio.rsocket.client;

import io.rsocket.RSocket;
import io.rsocket.core.RSocketClient;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author Frankie Chao by 2020-12-30.
 */
@Slf4j
public class FireAndForgetClientMain {

    public static void main(String[] args) throws Exception {

        Mono<RSocket> serverSocket =
                RSocketConnector.create()
                        .reconnect(Retry.backoff(50, Duration.ofMillis(500)))
                        .connect(TcpClientTransport.create("localhost", 9898));

        final RSocketClient client = RSocketClient.from(serverSocket);

        client.fireAndForget(Mono.just(DefaultPayload.create("Client Request-1")))
                .log()
                .subscribe();

        TimeUnit.SECONDS.sleep(3L);

        client.fireAndForget(Mono.just(DefaultPayload.create("Client Request-2")))
                .log()
                .subscribe();

        TimeUnit.SECONDS.sleep(3L);

        client.fireAndForget(Mono.just(DefaultPayload.create("Client Request-3")))
                .log()
                .subscribe();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            client.dispose();
            log.info("RSocket Client is Terminate !!!");
        }));

        TimeUnit.SECONDS.sleep(6000L);
    }
}

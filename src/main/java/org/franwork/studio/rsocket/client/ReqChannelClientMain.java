package org.franwork.studio.rsocket.client;

import io.rsocket.RSocket;
import io.rsocket.core.RSocketClient;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author Frankie Chao by 2021-01-05.
 */
@Slf4j
public class ReqChannelClientMain implements RSocketCustomizeClient {

    public static void main(String[] args) throws Exception {
        Mono<RSocket> serverSocket =
                RSocketConnector.create()
                        .reconnect(Retry.backoff(50, Duration.ofMillis(500)))
                        .connect(TcpClientTransport.create("localhost", 10000));
        final RSocketClient client = RSocketClient.from(serverSocket);
        client.requestChannel(Flux.just(RSocketCustomizeClient.getClientPublish(256)))
                .log()
                .publishOn(Schedulers.parallel())
                .doOnNext(payload -> {
                    log.info("============> Received response data from server [{}]", payload.getDataUtf8());
                    try {
                        TimeUnit.MILLISECONDS.sleep(500l);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                })
                .subscribe();

        client.requestChannel(Flux.just(RSocketCustomizeClient.getClientPublish("NEXT-Request-", 128)))
                .log()
                .publishOn(Schedulers.parallel())
                .doOnNext(payload -> {
                    log.info("============> Received response data from server [{}]", payload.getDataUtf8());
                    try {
                        TimeUnit.MILLISECONDS.sleep(500l);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                })
                .subscribe();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            client.dispose();
            log.info("RSocket Client is Terminate !!!");
        }));

        TimeUnit.SECONDS.sleep(6000L);
    }
}

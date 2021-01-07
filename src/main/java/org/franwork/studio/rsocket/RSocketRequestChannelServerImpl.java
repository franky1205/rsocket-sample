package org.franwork.studio.rsocket;

import io.rsocket.Payload;
import io.rsocket.SocketAcceptor;
import io.rsocket.core.RSocketServer;
import io.rsocket.transport.netty.server.TcpServerTransport;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.util.concurrent.TimeUnit;

/**
 * @author Frankie Chao by 2021-01-04.
 */
@Slf4j
public class RSocketRequestChannelServerImpl implements RSocketCustomizeServer {

    private final Disposable server;

    private RSocketRequestChannelServerImpl() {
        SocketAcceptor socketAcceptor = SocketAcceptor.forRequestChannel(payloadPublisher -> {
            payloadPublisher.subscribe(new RequestSubscriber());
            return Flux.just(getReturning(1024));
        });

        server = RSocketServer.create(socketAcceptor)
                .bind(TcpServerTransport.create("localhost", 10000))
                .doOnNext(cc -> log.info("Server started on the address : {}", cc.address()))
                .subscribe();
    }

    private void dispose() {
        this.server.dispose();
    }

    public static void main(String[] args) throws Exception {
        final RSocketRequestChannelServerImpl socketServer = new RSocketRequestChannelServerImpl();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            socketServer.dispose();
            log.info("RSocket Request and Stream server is Terminate !!!");
        }));

        log.info("RSocket server startup successfully !!!");
        TimeUnit.MINUTES.sleep(3000L);
    }

    private static final class RequestSubscriber implements Subscriber<Payload> {

        private Subscription subscription;

        @Override
        public void onSubscribe(Subscription s) {
            this.subscription = s;
            this.subscription.request(3);
        }

        @Override
        public void onNext(Payload payload) {
            log.info("Get request payload from client: [{}]", payload.getDataUtf8());
            try {
                TimeUnit.MILLISECONDS.sleep(500l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.subscription.request(3);
        }

        @Override
        public void onError(Throwable t) {
            log.error("Request payload publisher encounters error: [{}]", t.getMessage(), t);
        }

        @Override
        public void onComplete() {
            log.info("Request payload publisher is completed.");
        }
    }
}

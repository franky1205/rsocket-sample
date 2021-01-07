package org.franwork.studio.rsocket;

import io.rsocket.SocketAcceptor;
import io.rsocket.core.RSocketServer;
import io.rsocket.transport.netty.server.TcpServerTransport;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.util.concurrent.TimeUnit;

/**
 * @author Frankie Chao by 2021-01-03.
 */
@Slf4j
public class RSocketReqStreamServerImpl implements RSocketCustomizeServer {

    private final Disposable server;

    private RSocketReqStreamServerImpl() {
        SocketAcceptor socketAcceptor = SocketAcceptor.forRequestStream(payload -> {
            log.info("======> Server get request from client: [{}]", payload.getDataUtf8());
            return Flux.just(getReturning(512));
        });

        server = RSocketServer.create(socketAcceptor)
                .bind(TcpServerTransport.create("localhost", 9999))
                .doOnNext(cc -> log.info("Server started on the address : {}", cc.address()))
                .subscribe();
    }

    private void dispose() {
        this.server.dispose();
    }

    public static void main(String[] args) throws Exception {
        final RSocketReqStreamServerImpl socketServer = new RSocketReqStreamServerImpl();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            socketServer.dispose();
            log.info("RSocket Request and Stream server is Terminate !!!");
        }));

        log.info("RSocket server startup successfully !!!");
        TimeUnit.MINUTES.sleep(3000L);
    }
}

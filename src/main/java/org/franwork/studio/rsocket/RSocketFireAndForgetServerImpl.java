package org.franwork.studio.rsocket;

import io.rsocket.SocketAcceptor;
import io.rsocket.core.RSocketServer;
import io.rsocket.transport.netty.server.TcpServerTransport;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

/**
 * @author Frankie Chao by 2020-12-30.
 */
@Slf4j
class RSocketFireAndForgetServerImpl {

    private final Disposable server;

    private RSocketFireAndForgetServerImpl() {
        SocketAcceptor socketAcceptor = SocketAcceptor.forFireAndForget(payload -> {
            log.info("I got request from client: [{}]", payload.getDataUtf8());
            return Mono.empty();
        });

        Disposable server = RSocketServer.create(socketAcceptor)
                .bind(TcpServerTransport.create("localhost", 9898))
                .doOnNext(cc -> log.info("Server started on the address : {}", cc.address()))
                .subscribe();

        this.server = server;
    }

    private void dispose() {
        this.server.dispose();
    }

    public static void main(String[] args) throws Exception {
        final RSocketFireAndForgetServerImpl socketServer = new RSocketFireAndForgetServerImpl();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            socketServer.dispose();
            log.info("RSocket Fire and Forget server is Terminate !!!");
        }));

        log.info("RSocket server startup successfully !!!");
        TimeUnit.MINUTES.sleep(3000L);
    }
}

package org.franwork.studio.rsocket;

import io.rsocket.Payload;
import io.rsocket.SocketAcceptor;
import io.rsocket.core.RSocketServer;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.util.DefaultPayload;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

/**
 * @author Frankie Chao by 2020-12-25.
 */
@Slf4j
class RSocketReqResServerImpl implements RSocketCustomizeServer {

    private final Disposable server;

    private RSocketReqResServerImpl() {
        SocketAcceptor socketAcceptor = SocketAcceptor.forRequestResponse(payload -> Mono.just(DefaultPayload.create("Server Response")));

        this.server = RSocketServer.create(socketAcceptor)
                .bind(TcpServerTransport.create("localhost", 9797))
                .doOnNext(cc -> log.info("Server started on the address : {}", cc.address()))
                .subscribe();
    }

    private void dispose() {
        this.server.dispose();
    }

    public static void main(String[] args) throws Exception {
        final RSocketReqResServerImpl socketServer = new RSocketReqResServerImpl();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            socketServer.dispose();
            log.info("RSocket Request/Response server is Terminate !!!");
        }));

        log.info("RSocket server startup successfully !!!");
        TimeUnit.MINUTES.sleep(3000L);
    }
}

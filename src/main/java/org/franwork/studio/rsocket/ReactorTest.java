package org.franwork.studio.rsocket;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Frankie Chao by 2021-01-03.
 */
public class ReactorTest {

    public static void main(String[] args) throws InterruptedException {
//        List<Integer> elements = new ArrayList<>();
//        Flux.just(1, 2, 3, 4)
//                .log()
//                .map(i -> i * 2)
//                .subscribeOn(Schedulers.boundedElastic())
//                .publishOn(Schedulers.parallel())
//                .subscribe(elements::add);

        Flux.interval(Duration.ofSeconds(1), Schedulers.boundedElastic())
                .log()
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(new Subscriber<Long>() {

                    private Subscription subscription;

                    @Override
                    public void onSubscribe(Subscription subscription) {
                        this.subscription = subscription;
                        this.subscription.request(1l);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        try {
                            Thread.sleep(5000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        this.subscription.request(1l);
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        Thread.sleep(500000L);
    }
}

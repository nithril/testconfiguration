package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionTemplate;

import com.example.dao.DataSetRepository;
import com.example.dao.PieceGroupRepository;
import com.example.dom.DataSet;
import com.example.dom.PieceGroup;
import com.example.exception.EntityNotFoundException;
import com.example.exception.UnexpectedCurrentStateException;
import com.example.service.ProposalOne;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestconfigurationApplication.class)
public class TestconfigurationApplicationTests {

    @Autowired
    private ProposalOne fooBean;

    @Autowired
    private DataSetRepository dataSetRepository;
    @Autowired
    private PieceGroupRepository pieceGroupRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(100));

    @Autowired
    private ProcessServiceForTest processServiceForTest;

    @Before
    public void init() {

        dataSetRepository.deleteAll();
        pieceGroupRepository.deleteAll();

    }


    @Test
    public void testLock() throws ExecutionException, InterruptedException {

        final int nbOfConcurrentLock = 20;

        transactionTemplate.execute(t -> {
            dataSetRepository.save(new DataSet(0l, "foo"));
            dataSetRepository.save(new DataSet(1l, "foo"));

            pieceGroupRepository.save(new PieceGroup(0l, "foo"));
            pieceGroupRepository.save(new PieceGroup(1l, "foo"));

            return null;
        });

        AtomicInteger reentrantCounter = new AtomicInteger(0);

        CountDownLatch latch = new CountDownLatch(1);

        List<ListenableFuture<Boolean>> futures = new ArrayList<>();

        for (int i = 0; i < nbOfConcurrentLock; i++) {
            futures.add(executor.submit(() -> transactionTemplate.execute(t -> {
                try {
                    latch.await();
                    processServiceForTest.dataSet(0l, () -> {
                        Assert.assertEquals(1, reentrantCounter.incrementAndGet());
                        Thread.sleep(20l);
                        Assert.assertEquals(1, reentrantCounter.get());
                        return null;
                    });
                    Assert.assertEquals(1, reentrantCounter.get());
                    reentrantCounter.decrementAndGet();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            })));
        }
        latch.countDown();
        Futures.allAsList(futures).get();
    }

    @Test
    public void testReleaseOrder() throws ExecutionException, InterruptedException {

        final int nbOfConcurrentLock = 20;

        transactionTemplate.execute(t -> {
            dataSetRepository.save(new DataSet(0l, "foo"));
            return null;
        });


        List<ListenableFuture<Integer>> futures = new ArrayList<>();

        Phaser phaser = new Phaser(2);

        CountDownLatch latch = new CountDownLatch(1);

        for (int i = 0; i < nbOfConcurrentLock; i++) {
            final int finalI = i;

            futures.add(executor.submit(() -> transactionTemplate.execute(t -> {
                phaser.arriveAndAwaitAdvance();
                return processServiceForTest.dataSet(0l, () -> {
                    latch.await();
                    return finalI;
                });
            })));

            phaser.arriveAndAwaitAdvance();
            // The phaser is set as close as possible of the lock request
            // The sleep may be needed to give more time to the row lock request
            //Thread.sleep(50);
        }

        latch.countDown();

        Assert.assertEquals(IntStream.range(0, nbOfConcurrentLock).boxed().collect(Collectors.toList()), Futures.allAsList(futures).get());
    }


    @Test(expected = UnexpectedCurrentStateException.class)
    public void testUnexpectedState() throws ExecutionException, InterruptedException {
        transactionTemplate.execute(t -> {
            dataSetRepository.save(new DataSet(0l, "wrong"));
            return null;
        });
        transactionTemplate.execute(t -> processServiceForTest.dataSet(0l, () -> true));
    }

    @Test
    public void testNoState() throws ExecutionException, InterruptedException {
        transactionTemplate.execute(t -> {
            dataSetRepository.save(new DataSet(0l, "wrong"));
            return null;
        });
        transactionTemplate.execute(t -> processServiceForTest.noState(0l, () -> true));
    }


    @Test(expected = EntityNotFoundException.class)
    public void testEntityNotFound() throws ExecutionException, InterruptedException {
        transactionTemplate.execute(t -> {
            dataSetRepository.save(new DataSet(0l, "wrong"));
            return null;
        });
        transactionTemplate.execute(t -> processServiceForTest.noState(1l, () -> true));
    }

}

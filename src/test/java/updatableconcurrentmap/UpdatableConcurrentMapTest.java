/*
 * Copyright 2020-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package updatableconcurrentmap;

import hu.perit.spvitamin.core.took.Took;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThatNoException;

@Slf4j
class UpdatableConcurrentMapTest
{
    public static final int COUNT_PARALLEL_EXECUTORS = 10;
    public static final int COUNT_TOKEN_IDS = 10;

    private final UpdatableConcurrentMap<Long, Token> tokenCache = new UpdatableConcurrentMap<>();

    private final CountDownLatch latch = new CountDownLatch(1);

    private AtomicLong countWaiting = new AtomicLong(0);


    @Test
    void runTest() throws InterruptedException
    {
        fillCache();

        waitMillis(1000);
        log.debug("---- START --------------------------------------------------------------------------------------");

        List<ExecutorParams> executors = new ArrayList<>();
        for (int i = 0; i < COUNT_PARALLEL_EXECUTORS; i++)
        {
            executors.add(getExecutorService(i));
            waitMillis(1);
        }

        for (int i = 0; i < COUNT_PARALLEL_EXECUTORS; i++)
        {
            executors.get(i).getExecutorService().shutdown();
        }

        for (int i = 0; i < COUNT_PARALLEL_EXECUTORS; i++)
        {
            for (Future<?> future : executors.get(i).getFutures())
            {
                assertThatNoException().isThrownBy(future::get);
            }
        }

        this.latch.await(5, TimeUnit.SECONDS);
    }


    private ExecutorParams getExecutorService(int id)
    {
        ExecutorParams executorParams = new ExecutorParams();
        ExecutorService executorService = Executors.newFixedThreadPool(9);
        executorParams.setExecutorService(executorService);
        for (int i = 0; i < 10; i++)
        {
            for (long tokenId = 0; tokenId < COUNT_TOKEN_IDS; tokenId++)
            {
                Future<Boolean> future = executorService.submit(new TestGetJob(tokenId));
                executorParams.getFutures().add(future);
            }
        }
        return executorParams;
    }


    private Token getToken(Long tokenId)
    {
        incrementWaiting(tokenId);
        logIn(tokenId);

        Took took = null;
        Token token = null;
        try (Took t = new Took(false))
        {
            took = t;
            token = _getToken(tokenId);
            return token;
        }
        finally
        {
            decrementWaiting(tokenId);
            logOut(token, took.getDuration());
        }
    }


    private void incrementWaiting(Long tokenId)
    {
        if (tokenId == 5)
        {
            this.countWaiting.incrementAndGet();
        }
    }


    private void decrementWaiting(Long tokenId)
    {
        if (tokenId == 5)
        {
            this.countWaiting.decrementAndGet();
        }
    }


    private void logIn(Long tokenId)
    {
        if (tokenId == 5)
        {
            log.debug("getToken({}) <== IN, waiting: {}", getTokenImage(tokenId), this.countWaiting.get());
        }
        else
        {
            log.debug("getToken({}) IN", getTokenImage(tokenId));
        }
    }


    private void logOut(Token token, long duration)
    {
        if (token.getId() == 5)
        {
            log.debug("getToken({}) <== OUT took {} ms - {}, waiting: {}", getTokenImage(token.getId()), duration,
                token.getInfo(), this.countWaiting.get());
        }
        else
        {
            log.debug("getToken({}) OUT took {} ms - {}", getTokenImage(token.getId()), duration, token.getInfo());
        }
    }


    private Token _getToken(Long tokenId)
    {
        Token token = this.tokenCache.get(tokenId);
        if (isTokenValid(token))
        {
            token.setInfo("supplied by get()");
            return token;
        }

        if (token != null)
        {
            log.debug("getToken({}) token is invalid: {} ---------------------------------------------------", tokenId, token);
        }
        token = this.tokenCache.update(tokenId, () -> getNewToken(tokenId));
        token.setInfo("supplied by update()");

        return token;
    }


    private static String getTokenImage(Long tokenId)
    {
        // |0|1|2|3|4|5|6|7|8|9|
        // | | | | | |X| | | | |
        StringBuffer sb = new StringBuffer();
        sb.append(tokenId);
        sb.append(" |");
        sb.append("   |".repeat(Math.toIntExact(tokenId)));
        sb.append(tokenId != 5 ? " X " : " O ");
        sb.append("|");
        if (9 - tokenId > 0)
        {
            sb.append("   |".repeat(Math.toIntExact(9 - tokenId)));
        }
        return sb.toString();
    }


    private void fillCache()
    {
        try (Took took = new Took())
        {
            for (long tokenId = 0; tokenId < 10; tokenId++)
            {
                getToken(tokenId);
            }
        }
    }


    private Token getNewToken(Long tokenId)
    {
        waitMillis(10);
        return new Token(tokenId);
    }


    protected static void waitMillis(long timeoutMillis)
    {
        try
        {
            TimeUnit.MILLISECONDS.sleep(timeoutMillis);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }


    private boolean isTokenValid(Token token)
    {
        if (token == null)
        {
            return false;
        }

        return token.isValid();
    }


    @RequiredArgsConstructor
    private class TestGetJob implements Callable<Boolean>
    {
        private final Long tokenId;


        @Override
        public Boolean call() throws Exception
        {
            waitMillis(tokenId);
            Token token = getToken(tokenId);
            //log.debug("{}: {}", tokenId, accessToken);
            return true;
        }
    }


    @Data
    private static class ExecutorParams
    {
        private ExecutorService executorService;

        @Setter(AccessLevel.NONE)
        private List<Future<?>> futures = new ArrayList<>();
    }
}

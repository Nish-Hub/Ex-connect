package com.exconnect.authservice.executor;

import lombok.SneakyThrows;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class VirtualThreadProcessor<T> {

    @SneakyThrows
    T submit(Callable<T> callable, ExecutorService executorService){
        T result = executorService.submit(callable).get();
        return result;
    }

}

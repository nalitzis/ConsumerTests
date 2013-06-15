package com.blockingqueuetests;

/**
 * Created by ado on 15/06/13.
 */
public interface Product {

    /**
     * Consume a product. It is a sync call.
     * @throws InterruptedException
     */
    void consume() throws InterruptedException;
}

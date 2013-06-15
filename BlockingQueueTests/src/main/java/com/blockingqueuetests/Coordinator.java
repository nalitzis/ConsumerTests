package com.blockingqueuetests;

/**
 * Created by ado on 15/06/13.
 */
public interface Coordinator {

    /**
     * Offer a element
     * @param product
     */
    void offer(Product product);

    /**
     * cancel an element from the queue
     * @param product
     */
    void cancel(Product product);

    /**
     * Quit the Coordinator, removing all resources.
     */
    void quit();
}

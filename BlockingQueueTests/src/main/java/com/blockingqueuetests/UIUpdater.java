package com.blockingqueuetests;

import java.util.PriorityQueue;

/**
 * UI notifier. An activity that wants to be notified from the coordinator can implement these methods
 * Created by ado on 15/06/13.
 */
public interface UIUpdater {

    void onQueueChanged(String queueAsText);

    void onProductInProcessing(Product p);

    void onProductProcessed(Product p);
}

package com.blockingqueuetests;

import android.os.Handler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implements a producer/consumer model
 * Created by ado on 15/06/13.
 */
public class CoordinatorImpl implements Coordinator {

    private final BlockingQueue<Product> mProductBlockingQueue;

    //used if multiple threads can check and change value to the element
    private final AtomicBoolean mRunning;

    private final UIUpdater mUIUpdater;

    //need to post on same thread that creates this object (UI thread)
    private final Handler mUIHandler;

    private final Thread mConsumerThread;

    /**
     * Creates a Coordinator implementation
     * @param updater the updater to be notified when something interesting happens here. Updates are
     *                guaranteed to be delivered on the same thread who created the component. That
     *                thread must have a looper (UI thread is fine)
     */
    public CoordinatorImpl(UIUpdater updater){
        mUIUpdater = updater;
        mUIHandler = new Handler();
        mRunning = new AtomicBoolean();
        mProductBlockingQueue = new LinkedBlockingQueue<Product>();

        Consumer consumer = new Consumer();
        mConsumerThread = new Thread(consumer);

        startConsumer();
    }

    private void startConsumer(){
        mRunning.set(true);
        mConsumerThread.start();
    }

    private class Consumer implements Runnable{
        @Override
        public void run() {
            while(mRunning.get()){
                try{
                    Product p = mProductBlockingQueue.take();
                    postOnQueueChanged(mProductBlockingQueue);
                    postProductInProcessing(p);
                    p.consume();
                    postProductProcessed(p);
                }catch(InterruptedException e){
                    //swallow the exception
                }
            }
        }

    }

    private void postOnQueueChanged(BlockingQueue<Product> queue){
        final String queueAsText = fromQueueToText(queue);
        mUIHandler.post(new Runnable(){
            public void run(){
                mUIUpdater.onQueueChanged(queueAsText);
            }
        });
    }

    private String fromQueueToText(BlockingQueue<Product> queue){
        return queue.toString();
    }

    private void postProductInProcessing(final Product p){
        mUIHandler.post(new Runnable(){
            public void run(){
                mUIUpdater.onProductInProcessing(p);
            }
        });
    }

    private void postProductProcessed(final Product p){
        mUIHandler.post(new Runnable(){
            public void run(){
                mUIUpdater.onProductProcessed(p);
            }
        });
    }



    @Override
    public void offer(Product product) {
        try{
            mProductBlockingQueue.put(product);
            postOnQueueChanged(mProductBlockingQueue);
        }catch(InterruptedException e){
            //swallow the exception
        }

    }

    @Override
    public void cancel(Product product) {
        mProductBlockingQueue.remove(product);
        postOnQueueChanged(mProductBlockingQueue);
    }

    @Override
    public void quit() {
        mRunning.set(false);
        mProductBlockingQueue.clear();
        postOnQueueChanged(mProductBlockingQueue);
        mConsumerThread.interrupt();
    }
}

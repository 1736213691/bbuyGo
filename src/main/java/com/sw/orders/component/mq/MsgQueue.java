package com.sw.orders.component.mq;

import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 消息队列的简单实现
 * @param <T>
 */
@Component
public class MsgQueue<T> {
    private LinkedList<T> msgQueue = new LinkedList<T>();

    int count = 1;

    private Lock lock = new ReentrantLock();
    private Condition empty = lock.newCondition();
    private Thread thread = new Thread(() -> {
        while(true) {
            if(msgQueue.isEmpty()) {
                try {
                    lock.lock();
                    empty.await();
                    lock.unlock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            consumer();
        }
    });


    {
        thread.start();
    }
    public boolean product(T msg) {
        // 要保证线程安全和消息入队顺序
        boolean b = msgQueue.offer(msg);
        if(msgQueue.size() <= 1) {
            lock.lock();
            empty.signal();
            lock.unlock();
        }
        return b;
    }
    public T poll() {
        return msgQueue.poll();
    }
    public void consumer() {
        System.out.println(poll().toString());
    }
}

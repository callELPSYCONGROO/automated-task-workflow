package com.wuhenjian.atw.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程工厂，定义了线程名称格式
 * @author wuhenjian
 */
public class AtwThreadFactory implements ThreadFactory {

    private static final AtomicInteger THREAD_COUNT = new AtomicInteger(1);

    private final ThreadGroup group;

    private final String namePrefix;

    public AtwThreadFactory(String taskName) {
        SecurityManager s = System.getSecurityManager();
        this.group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.namePrefix = "ATW-" + taskName + "-thread-";
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(this.group, r, this.namePrefix + THREAD_COUNT.getAndIncrement(), 0);
    }
}

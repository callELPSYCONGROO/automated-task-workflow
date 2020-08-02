package com.wuhenjian.atw.chain;

/**
 * 任务处理器。
 * 任务的最小处理单元。
 *
 * @author wuhenjian
 */
public interface Processor extends Order {

    /**
     * 处理任务的方法
     */
    void handle() throws Exception;
}

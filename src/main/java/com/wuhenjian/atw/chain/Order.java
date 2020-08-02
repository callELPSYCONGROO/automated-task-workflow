package com.wuhenjian.atw.chain;

/**
 * 排序接口
 *
 * @author wuhenjian
 */
public interface Order {

    /**
     * 最优先处理的排序值
     */
    int FIRST = Integer.MIN_VALUE;

    /**
     * 最后处理的排序值
     */
    int LAST = Integer.MAX_VALUE;

    /**
     * 默认值，0
     */
    int ZERO = 0;

    /**
     * 排序值。值越小，越靠前处理。默认0。
     */
    default int getOrder() {
        return ZERO;
    }
}

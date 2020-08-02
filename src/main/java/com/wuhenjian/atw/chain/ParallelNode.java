package com.wuhenjian.atw.chain;

import java.util.List;

/**
 * 并行节点
 *
 * @author wuhenjian
 */
public class ParallelNode extends AbstractNode {

    private final int order;

    public ParallelNode(List<Processor> processorList) {
        super(processorList);
        this.order = Order.ZERO;
    }

    public ParallelNode(List<Processor> processorList, int order) {
        super(processorList);
        this.order = order;
    }

    @Override
    public ExecutionWay getExecutionWay() {
        return ExecutionWay.PARALLEL;
    }

    @Override
    public int getOrder() {
        return this.order;
    }
}

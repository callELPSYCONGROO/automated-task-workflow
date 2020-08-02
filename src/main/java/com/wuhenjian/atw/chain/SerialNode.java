package com.wuhenjian.atw.chain;

import java.util.List;

/**
 * 串行节点
 *
 * @author wuhenjian
 */
public class SerialNode extends AbstractNode {

    private final int order;

    public SerialNode(List<Processor> processorList) {
        super(processorList);
        this.order = Order.ZERO;
    }

    public SerialNode(List<Processor> processorList, int order) {
        super(processorList);
        this.order = order;
    }

    @Override
    public ExecutionWay getExecutionWay() {
        return ExecutionWay.SERIAL;
    }

    @Override
    public int getOrder() {
        return this.order;
    }
}

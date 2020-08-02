package com.wuhenjian.atw.chain;

import com.wuhenjian.atw.common.CommonUtil;

import java.util.List;
import java.util.Objects;

/**
 * 任务处理节点
 *
 * @author wuhenjian
 */
public abstract class AbstractNode implements Node {

    /**
     * 任务处理器列表
     */
    private final List<Processor> processorList;

    public AbstractNode(List<Processor> processorList) {
        if (Objects.isNull(processorList) || processorList.isEmpty()) {
            throw new IllegalArgumentException("task handler list of node cannot be null or empty");
        }

        this.processorList = CommonUtil.sort(processorList);
    }

    /**
     * 获取任务处理器列表
     */
    @Override
    public List<Processor> getProcessorList() {
        return processorList;
    }
}

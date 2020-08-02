package com.wuhenjian.atw.chain;

import java.util.List;

/**
 * @author wuhenjian
 */
public interface Node extends Order {

    List<Processor> getProcessorList();

    ExecutionWay getExecutionWay();
}

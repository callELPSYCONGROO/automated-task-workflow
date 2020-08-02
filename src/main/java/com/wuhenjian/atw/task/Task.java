package com.wuhenjian.atw.task;

import com.wuhenjian.atw.AtwManagement;
import com.wuhenjian.atw.chain.ExecutionWay;
import com.wuhenjian.atw.chain.Node;
import com.wuhenjian.atw.chain.Processor;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 任务配置
 *
 * @author wuhenjian
 */
public class Task implements Runnable {

    /**
     * 节点列表
     */
    private final List<Node> nodeList;

    /**
     * 任务名称
     */
    private final String name;

    /**
     * 任务周期类型
     */
    private final Period period;

    /**
     * <p>时间，与周期类型匹配，毫秒。</p>
     * <p>周期类型为FIXED_RATE时，则为周期；</p>
     * <p>周期类型为FIXED_DELAY时，则为延迟时间；</p>
     * <p>周期类型为ONCE时，该值无意义；</p>
     */
    private final Integer time;

    /**
     * 任务初始化开始延迟时间，毫秒
     */
    private final Integer initDelay;

    /**
     * 全参构造器，所有参数不允许为null
     */
    public Task(List<Node> nodeList, String name, Period period, Integer time, Integer initDelay) {
        this.nodeList = nodeList;
        this.name = name;
        this.period = period;
        this.time = time;
        this.initDelay = initDelay;
        checkParam();
    }

    /**
     * 构造器，初始化启动时间为0
     */
    public Task(List<Node> nodeList, String name, Period period, Integer time) {
        this(nodeList, name, period, time, 0);
    }

    /**
     * 只执行一次的任务
     */
    public Task(List<Node> nodeList, String name) {
        this(nodeList, name, Period.ONCE, 0);
    }

    /**
     * 检查参数是否合法
     */
    private void checkParam() {
        if (Objects.isNull(nodeList) || nodeList.isEmpty()) {
            throw new IllegalArgumentException("node list of Manager cannot be null or empty");
        }

        if (Objects.isNull(period)) {
            throw new IllegalArgumentException("period cannot be null");
        }

        if (Objects.isNull(time) || time < 0) {
            throw new IllegalArgumentException("time cannot be null or negative");
        }

        if (Objects.isNull(initDelay) || initDelay < 0) {
            throw new IllegalArgumentException("initDelay cannot be null or negative");
        }
    }

    public String getName() {
        return name;
    }

    public Period getPeriod() {
        return period;
    }

    public Integer getTime() {
        return time;
    }

    public Integer getInitDelay() {
        return initDelay;
    }

    @Override
    public void run() {
        // 执行节点
        for (Node node : this.nodeList) {
            ExecutionWay nodeExecutionWay = node.getExecutionWay();
            List<Processor> processorList = node.getProcessorList();
            // 根据节点执行方式选择串行或并行执行节点中的执行器
            switch (nodeExecutionWay) {
                case SERIAL:
                    try {
                        invokeSerial(processorList);
                    } catch (Exception e) {
                        return;
                    }

                    break;
                case PARALLEL:
                    try {
                        invokeParallel(processorList);
                    } catch (Exception e) {
                        return;
                    }

                    break;
                default:
//                    throw new IllegalArgumentException("node execution way cannot be null");
                    return;
            }
        }
    }

    /**
     * 串行执行任务处理
     */
    private void invokeSerial(List<Processor> processorList) throws Exception {
        for (Processor processor : processorList) {
            processor.handle();
        }
    }

    /**
     * 并行执行任务处理
     */
    private void invokeParallel(List<Processor> processorList) throws Exception {
        // 放入线程池中并行
        List<Future<Exception>> futureList = processorList.stream()
                .map(processor ->
                        AtwManagement.PROCESSOR_EXECUTOR.submit(() -> {
                            try {
                                processor.handle();
                            } catch (Exception e) {
                                return e;
                            }
                            // 没有异常则返回空
                            return null;
                        }))
                .collect(Collectors.toList());

        // 查看是否有异常抛出
        List<Exception> exceptionList = futureList.stream()
                .map(future -> {
                    try {
                        // get()方法等待线程结束
                        return future.get();
                    } catch (CancellationException | InterruptedException | ExecutionException e) {
                        return e;
                    }
                })
                // 为空则代表没有异常
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (!exceptionList.isEmpty()) {
            // 异常信息集合成一个exception
            String exceptionMessages = exceptionList.stream()
                    .map(Exception::getMessage)
                    .collect(Collectors.joining(";"));
            throw new Exception(exceptionMessages);
        }
    }

    /**
     * 任务周期
     */
    public enum Period {
        /**
         * 固定频率
         */
        FIXED_RATE,
        /**
         * 固定延迟
         */
        FIXED_DELAY,
        /**
         * 只执行一次
         */
        ONCE,
        ;
    }
}

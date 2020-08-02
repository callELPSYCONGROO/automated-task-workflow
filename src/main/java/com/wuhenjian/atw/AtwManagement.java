package com.wuhenjian.atw;

import com.wuhenjian.atw.task.Task;
import com.wuhenjian.atw.thread.AtwThreadFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 * 自动任务管理
 *
 * @author wuhenjian
 */
public class AtwManagement {

    private static final String PROCESSOR = "processor";

    private static final String TASK = "task";

    private static final String TASK_NAME_SUFFIX = "_1";

    /**
     * 任务节点执行器并行执行线程池。
     */
    public final static ThreadPoolExecutor PROCESSOR_EXECUTOR = new ThreadPoolExecutor(
            0,
            Integer.MAX_VALUE,
            0,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<>(),
            new AtwThreadFactory(PROCESSOR)
    );

    /**
     * 主任务线程池。
     * 负责周期性执行任务。
     */
    private ScheduledExecutorService mainTaskExecutor;

    /**
     * 任务名称与任务事件管理映射
     */
    private Map<String, ScheduledFuture<?>> taskFutureMap;

    public AtwManagement(List<Task> taskList) {
        init(taskList);
    }

    public AtwManagement() {
        init(Collections.emptyList());
    }

    /**
     * 初始化线程池，并注册任务
     */
    private void init(List<Task> taskList) {
        // 初始化映射表
        taskFutureMap = new HashMap<>();
        // 初始化线程池
        mainTaskExecutor = Executors.newScheduledThreadPool(taskList.size(), new AtwThreadFactory(TASK));
        // 配置任务
        taskList.forEach(this::register);
    }

    /**
     * <p>注册任务。</p>
     * <p>该方法通常不会是一个高频操作，这里为了时每个任务都有一个唯一的名称，加锁来控制{@link AtwManagement#taskFutureMap}对象的操作。</p>
     *
     * @param task 任务配置
     * @return 返回注册在对象中的任务名称
     */
    public synchronized String register(Task task) {
        // 根据周期类型，添加任务
        Task.Period period = task.getPeriod();
        ScheduledFuture<?> future;
        switch (period) {
            case FIXED_DELAY:
                future = mainTaskExecutor.scheduleWithFixedDelay(task, task.getInitDelay(), task.getTime(), TimeUnit.MILLISECONDS);
                break;
            case FIXED_RATE:
                future = mainTaskExecutor.scheduleAtFixedRate(task, task.getInitDelay(), task.getTime(), TimeUnit.MILLISECONDS);
                break;
            case ONCE:
                future = mainTaskExecutor.schedule(task, task.getInitDelay(), TimeUnit.MILLISECONDS);
                break;
            default:
                throw new IllegalArgumentException("unknown period: " + period);
        }

        // 查找是否存在相同名称的任务
        String taskName = task.getName();
        if (Objects.nonNull(taskFutureMap.get(taskName))) {
            taskName += TASK_NAME_SUFFIX;
        }

        taskFutureMap.put(taskName, future);
        return taskName;
    }

    /**
     * 停止任务
     *
     * @param taskName 任务名称
     * @param mayInterruptIfRunning 是否直接中断任务运行
     * @throws InputMismatchException 未找到对应的任务，抛出此异常
     */
    public void stopTask(String taskName, boolean mayInterruptIfRunning) {
        ScheduledFuture<?> future = taskFutureMap.get(taskName);
        if (Objects.isNull(future)) {
            throw new InputMismatchException("no task with matching [" + taskName + "] was found");
        }

        future.cancel(mayInterruptIfRunning);
    }
}

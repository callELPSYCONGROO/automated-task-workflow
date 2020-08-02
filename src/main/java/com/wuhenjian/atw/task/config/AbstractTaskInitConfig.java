package com.wuhenjian.atw.task.config;

import com.wuhenjian.atw.task.Task;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 提供检查任务名称是否重复的方法
 *
 * @author wuhenjian
 */
public abstract class AbstractTaskInitConfig implements TaskInitConfig {

    /**
     * 实现该方法，从外部数据获取任务配置信息
     */
    public abstract List<Task> getConfigsFromExternalData();

    @Override
    public List<Task> getConfigList() {
        List<Task> tasks = getConfigsFromExternalData();
        List<String> duplicateForTaskName = findDuplicateForTaskName(tasks);
        if (Objects.nonNull(duplicateForTaskName)) {
            String join = String.join(",", duplicateForTaskName);
            throw new IllegalArgumentException("duplicate task names [" + join + "] from external data");
        }

        return tasks;
    }

    /**
     * 找到重复的任务名称
     *
     * @param taskList 任务配置列表
     * @return 重复的任务名列表
     */
    public List<String> findDuplicateForTaskName(List<Task> taskList) {
        return taskList.stream()
                // 以名称分组、计数
                .collect(Collectors.groupingBy(Task::getName, Collectors.counting()))
                .entrySet()
                .stream()
                // 找出大于1的名称对
                .filter(entry -> entry.getValue() > 1)
                // 取出名称
                .map(Map.Entry::getKey)
                // 组成列表
                .collect(Collectors.toList());
    }
}

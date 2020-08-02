package com.wuhenjian.atw.task.config;

import com.wuhenjian.atw.AtwManagement;
import com.wuhenjian.atw.task.Task;

import java.util.List;

/**
 * 任务初始化配置
 *
 * @author wuhenjian
 */
public interface TaskInitConfig {

    /**
     * <p>获取任务初始化配置。</p>
     * <p>这里需要自行保证任务名称唯一。
     * 如果任务名称不唯一，后添加的任务会在
     * {@link AtwManagement#register(Task)}
     * 方法中添加"_1"后缀。</p>
     *
     * @return 初始化的任务配置列表。不允许返回null。
     */
    List<Task> getConfigList();
}

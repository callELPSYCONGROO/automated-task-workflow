package com.wuhenjian.atw.task.config;

import com.wuhenjian.atw.task.Task;

import java.util.Collections;
import java.util.List;

/**
 * 默认的任务初始化配置。
 * 不从外部数据源获取任务配置。
 *
 * @author wuhenjian
 */
public class DefaultTaskInitConfig implements TaskInitConfig {

    @Override
    public List<Task> getConfigList() {
        // 返回一个空列表
        return Collections.emptyList();
    }

    /**
     * 创建一个默认任务初始化配置对象
     */
    public static DefaultTaskInitConfig build() {
        return new DefaultTaskInitConfig();
    }
}

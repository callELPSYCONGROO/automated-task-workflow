package com.wuhenjian.atw.common;

import com.wuhenjian.atw.chain.Order;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wuhenjian
 */
public class CommonUtil {

    /**
     * 根据{@link Order#getOrder()}的返回值升序排序
     *
     * @param list 待排序数据
     * @param <T> 泛型必须实现{@link Order}接口
     * @return 排序后的新列表
     */
    public static  <T extends Order> List<T> sort(List<T> list) {
        return list.stream()
                .sorted(Comparator.comparingInt(T::getOrder))
                .collect(Collectors.toList());
    }
}

package com.wuhenjian.atw;

import com.wuhenjian.atw.chain.Node;
import com.wuhenjian.atw.chain.ParallelNode;
import com.wuhenjian.atw.chain.Processor;
import com.wuhenjian.atw.chain.SerialNode;
import com.wuhenjian.atw.task.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author wuhenjian
 */
public class AtwManagementTest {

    private List<Task> taskList;

    @Before
    public void before() {
        taskList = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            // node list
            int finalI = i;
            List<Node> nodeList = IntStream.range(1, i % 3 + 2)
                    .mapToObj(j -> {
                        // processor list
                        List<Processor> processorList = IntStream.range(1, 3)
                                .mapToObj(k -> new Processor() {
                                    @Override
                                    public void handle() throws Exception {
                                        Thread.sleep((finalI + j + k) * 100);
                                        System.out.println("this is Task[" + finalI + "] Node[" + j + "] Processor[" + k + "]. order value is " + getOrder());
                                    }

                                    @Override
                                    public int getOrder() {
                                        return k % 3;
                                    }
                                })
                                .collect(Collectors.toList());


                        Node node;
                        if (j == 3) {
                            node = new SerialNode(processorList, 3 - j);
                        } else {
                            node = new ParallelNode(processorList, 3 - j);
                        }

                        return node;
                    })
                    .collect(Collectors.toList());

            Task task = new Task(nodeList, "MyTestTask" + i, Task.Period.values()[i % 3], 1000, 3000);
            taskList.add(task);
        }
    }

    @Test
    public void initTest() throws InterruptedException {
        AtwManagement management = new AtwManagement(taskList);
        Thread.currentThread().join();
    }
}

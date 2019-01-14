package com.hand.hap.cloud.demo.service;

import com.hand.hap.cloud.resource.exception.HapException;
import com.hand.todo.demo.TodoServiceApplication;
import com.hand.todo.demo.domain.TodoTask;
import com.hand.todo.demo.service.TaskService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by ziling.zhong on 2017/7/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TodoServiceApplication.class)
@Transactional
public class TodoServiceTest {

    @Autowired
    private TaskService taskService;

    private static TodoTask todoTask;
    private static String taskNumber;
    private static Long employId;
    private static String taskDescription;

    public static void taskSetValue() {
        todoTask.setEmployeeId(employId);
        todoTask.setTaskNumber(taskNumber);
        todoTask.setTaskDescription(taskDescription);
    }

    @Test
    public void test1Create() {
        todoTask = new TodoTask();
        taskNumber = "test-" + new Date().getTime();
        employId = new Long(1);
        taskDescription = "testInsert";
        String taskState = "testState";
        taskSetValue();
        todoTask.setState(taskState);
        //创建后数据会回写，最终调用insertSelective(T)
        if (taskService.create(todoTask) == null) {
            throw new HapException("error.client.create");
        }
        todoTask = taskService.selectByPrimaryKey(todoTask.getId());
        Assert.assertEquals(employId, todoTask.getEmployeeId());
        Assert.assertEquals(taskNumber, todoTask.getTaskNumber());
        Assert.assertEquals(taskDescription, todoTask.getTaskDescription());
        Assert.assertEquals(taskState, todoTask.getState());
    }

    @Test
    public void test2FindByTaskNumber() {
        test1Create();
        todoTask = taskService.findByTaskNumber(todoTask.getTaskNumber());
        Assert.assertEquals(employId, todoTask.getEmployeeId());
        Assert.assertEquals(taskNumber, todoTask.getTaskNumber());
        Assert.assertEquals(taskDescription, todoTask.getTaskDescription());
    }

    @Test
    public void test3FindByVersionNumber() {
        for (int i = 0; i < 3; i++) {
            test1Create();
        }
        List<TodoTask> todoTasks = taskService.findByTaskVersionNumber(new Long(1));
        Assert.assertFalse(todoTasks.isEmpty());
    }

    @Test
    public void test4Update() {
        test1Create();
        todoTask = taskService.selectByPrimaryKey(todoTask.getId());
        taskNumber = "testService2-" + new Date().getTime();
        taskDescription = "testServiceUpdate2";
        employId = new Long(2);
        taskSetValue();
        if (taskService.updateByPrimaryKeySelective(todoTask) != 1) {
            throw new HapException("error.client.update");
        }
        todoTask = taskService.selectByPrimaryKey(todoTask.getId());
        Assert.assertEquals(employId, todoTask.getEmployeeId());
        Assert.assertEquals(taskNumber, todoTask.getTaskNumber());
        Assert.assertEquals(taskDescription, todoTask.getTaskDescription());
    }

    @Test
    public void test99Delete() {
        test1Create();
        Assert.assertNotNull(taskService.selectByPrimaryKey(todoTask.getId()));
        if (taskService.deleteByPrimaryKey(todoTask.getId()) != 1) {
            throw new HapException("error.client.delete");
        }
        Assert.assertNull(taskService.selectByPrimaryKey(todoTask.getId()));
    }

    @Test
    public void test999DeleteByTaskNumber() {
        test1Create();
        Assert.assertNotNull(taskService.selectByPrimaryKey(todoTask.getId()));
        if (taskService.deleteByTaskNumber(todoTask.getTaskNumber()) != 1) {
            throw new HapException("error.client.delete");
        }
        Assert.assertNull(taskService.selectByPrimaryKey(todoTask.getId()));
    }
}

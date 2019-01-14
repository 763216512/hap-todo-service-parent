package com.hand.hap.cloud.demo.mapper;

import com.hand.hap.cloud.resource.exception.HapException;
import com.hand.todo.demo.TodoServiceApplication;
import com.hand.todo.demo.domain.TodoTask;
import com.hand.todo.demo.mapper.TodoTaskMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by ziling.zhong on 2017/7/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TodoServiceApplication.class)
@Transactional
public class TodoTaskMapperTest {
    @Autowired(required = false)
    private TodoTaskMapper taskMapper;

    private static TodoTask todoTask;
    private static Long employId;
    private static String taskNumber;
    private static String taskDescription;

    public static void taskSetValue() {
        todoTask.setEmployeeId(employId);
        todoTask.setTaskNumber(taskNumber);
        todoTask.setTaskDescription(taskDescription);
    }

    //插入todoTask
    @Test
    public void test1Insert() {
        todoTask = new TodoTask();
        employId = new Long(1);
        taskNumber = "test-" + new Date().getTime();
        taskDescription = "testInsert";
        taskSetValue();
        //插入数据
        if (taskMapper.insertSelective(todoTask) != 1) {
            throw new HapException("error.client.insert");
        }
        todoTask = taskMapper.selectByPrimaryKey(todoTask.getId());
        Assert.assertEquals(employId, todoTask.getEmployeeId());
        Assert.assertEquals(taskNumber, todoTask.getTaskNumber());
        Assert.assertEquals(taskDescription, todoTask.getTaskDescription());
    }

    //更新todoTask
    @Test
    public void test2Update() {
        test1Insert();
        employId = new Long(2);
        taskNumber = "test-" + new Date().getTime();
        taskDescription = "testUpdate";
        taskSetValue();
        if (taskMapper.updateByPrimaryKeySelective(todoTask) != 1) {
            throw new HapException("error.client.update");
        }
        todoTask = taskMapper.selectByPrimaryKey(todoTask.getId());
        Assert.assertEquals(employId, todoTask.getEmployeeId());
        Assert.assertEquals(taskNumber, todoTask.getTaskNumber());
        Assert.assertEquals(taskDescription, todoTask.getTaskDescription());
    }

    //删除todoTask
    @Test
    public void test99Delete() {
        test1Insert();
        if (taskMapper.deleteByPrimaryKey(todoTask.getId()) != 1) {
            throw new HapException("error.client.delete");
        }
        Assert.assertNull(taskMapper.selectByPrimaryKey(todoTask.getId()));
    }
}

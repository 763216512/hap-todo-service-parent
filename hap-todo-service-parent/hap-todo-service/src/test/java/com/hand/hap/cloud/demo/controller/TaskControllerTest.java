package com.hand.hap.cloud.demo.controller;

import com.hand.todo.demo.domain.TodoTask;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by ziling.zhong on 2017/7/5.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TaskControllerTest extends BaseControllerTest {

    private static TodoTask target;

    @Test
    public void test1Create() throws Exception {
        TodoTask todoTask = new TodoTask();
        String taskNumber = "testController" + new Date().getTime();
        Long employId = new Long(1);
        String taskDescription = "testControllerInsert";
        String taskState = "taskState";
        todoTask.setEmployeeId(employId);
        todoTask.setTaskNumber(taskNumber);
        todoTask.setTaskDescription(taskDescription);
        todoTask.setState(taskState);
        target = restTemplate.postForEntity("/v1/todoTask/create", todoTask, TodoTask.class).getBody();
        Assert.assertEquals(employId, target.getEmployeeId());
        Assert.assertEquals(taskNumber, target.getTaskNumber());
        Assert.assertEquals(taskDescription, target.getTaskDescription());
        Assert.assertEquals(taskState, target.getState());
    }

    @Test
    public void test2FindByNumber() throws Exception {
        TodoTask todoTask = restTemplate.getForEntity("/v1/todoTask/findByNumber/{taskNumber}", TodoTask.class, target.getTaskNumber()).getBody();
        Assert.assertEquals(todoTask.getEmployeeId(), target.getEmployeeId());
        Assert.assertEquals(todoTask.getTaskNumber(), target.getTaskNumber());
        Assert.assertEquals(todoTask.getTaskDescription(), target.getTaskDescription());
    }

    //由于TodoTask类继承AuditDomain基类，在更新数据通过Controller层调用时会进行审计，其它层不用考虑
//此时在提交的数据target中必须包含objectVersionNumber字段，并给该字段赋予数据库中的数据。
    @Test
    public void test3Update() throws Exception {
        target.setState("Yes");
        //更新数据
        restTemplate.put("/v1/todoTask/{id}", target, target.getId());
        TodoTask todoTask = restTemplate.getForEntity("/v1/todoTask/findByNumber/{taskNumber}", TodoTask.class, target.getTaskNumber()).getBody();
        Assert.assertEquals(target.getState(), todoTask.getState());
    }

    @Test
    public void test4FindByVersionNumber() throws Exception {
        Map result = restTemplate.getForEntity("/v1/todoTask/findByVersionNumber/{objectVersionNumber}", Map.class, target.getObjectVersionNumber()).getBody();
        Assert.assertFalse(result.isEmpty());
        Assert.assertNotNull(result.get("tasks"));
    }

//    @Test
//    public void test4FindByVersionNumber() throws Exception {
//        List result = restTemplate.getForEntity("/v1/todoTask/findByVersionNumber/{objectVersionNumber}", List.class, target.getObjectVersionNumber()).getBody();
//        Assert.assertFalse(result.isEmpty());
//    }

    @Test
    public void test99DeleteById() throws Exception {
        restTemplate.delete("/v1/todoTask/{id}", target.getId());
        HttpStatus status = restTemplate.getForEntity("/v1/todoTask/findByNumber/{taskNumber}", TodoTask.class, target.getTaskNumber()).getStatusCode();
        Assert.assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    @Test
    public void test999DeleteByNumber() throws Exception {
        test1Create();
        restTemplate.delete("/v1/todoTask/taskNumber/{taskNumber}", target.getTaskNumber());
        HttpStatus status = restTemplate.getForEntity("/v1/todoTask/findByNumber/{taskNumber}", TodoTask.class, target.getTaskNumber()).getStatusCode();
        Assert.assertEquals(HttpStatus.BAD_REQUEST, status);
    }
}

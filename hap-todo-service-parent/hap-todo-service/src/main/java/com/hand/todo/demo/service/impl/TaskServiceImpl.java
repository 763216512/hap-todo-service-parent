package com.hand.todo.demo.service.impl;
import com.hand.hap.cloud.mybatis.service.BaseServiceImpl;
import com.hand.hap.cloud.resource.exception.HapException;
import com.hand.todo.demo.domain.TodoTask;
import com.hand.todo.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ziling.zhong on 2017/7/5.
 */
@Service
@Transactional(rollbackFor = HapException.class)
public class TaskServiceImpl extends BaseServiceImpl<TodoTask> implements TaskService {

    @Override
    public TodoTask findByTaskNumber(String taskNumber) {
        TodoTask task = new TodoTask();
        task.setTaskNumber(taskNumber);
        List<TodoTask> tasks = select(task);
        if (!tasks.isEmpty()) {
            return tasks.get(0);
        }
        return null;
    }

    @Override
    public int delete(Long id) {
        TodoTask task = selectByPrimaryKey(id);
        if (task == null) {
            throw new HapException("error.todoTask.taskNumberExist");
        }
        return deleteByPrimaryKey(id);
    }

    @Override
    public int deleteByTaskNumber(String taskNumber) {
        TodoTask task = findByTaskNumber(taskNumber);
        if (task == null) {
            throw new HapException("error.todoTask.taskNumberExist");
        }
        return delete(task);
    }

    @Override
    public TodoTask create(TodoTask task) throws HapException {
        if (findByTaskNumber(task.getTaskNumber()) != null) {
            throw new HapException("error.todoTask.taskNumberExist");
        }
        if (insert(task) != 1) {
            throw new HapException("error.todoTask.insertNotOne");
        }
        return selectByPrimaryKey(task.getId());
    }

    @Override
    public TodoTask update(Long id, TodoTask task) {
        TodoTask oldTask = selectByPrimaryKey(id);
        if (oldTask == null) {
            throw new HapException("error.todoTask.notFound");
        }
        String taskNumber = task.getTaskNumber();
        if (taskNumber != null && !oldTask.getTaskNumber().equals(taskNumber)
                && findByTaskNumber(taskNumber) != null) {
            throw new HapException("error.todoTask.taskNumberExist");
        }
        task.setId(id);
        if (updateByPrimaryKeySelective(task) != 1) {
            throw new HapException("error.client.update");
        }
        return selectByPrimaryKey(id);
    }

    @Override
    public List<TodoTask> findByTaskVersionNumber(Long objectVersionNumber) {
        TodoTask task = new TodoTask();
        task.setObjectVersionNumber(objectVersionNumber);
        List<TodoTask> tasks = select(task);
        return tasks;
    }
}
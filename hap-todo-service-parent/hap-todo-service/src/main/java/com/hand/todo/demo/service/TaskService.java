package com.hand.todo.demo.service;

import com.hand.hap.cloud.mybatis.service.BaseService;
import com.hand.todo.demo.domain.TodoTask;

import java.util.List;

/**
 * Created by ziling.zhong on 2017/7/5.
 */
public interface TaskService extends BaseService<TodoTask>{

    TodoTask findByTaskNumber(String taskNumber);

    int delete(Long id);

    int deleteByTaskNumber(String taskNumber);

    TodoTask create(TodoTask task);

    TodoTask update(Long id, TodoTask task);

    List<TodoTask> findByTaskVersionNumber(Long objectVersionNumber);
}

package com.hand.todo.demo.controller;
import com.hand.hap.cloud.resource.exception.HapException;
import com.hand.hap.cloud.swagger.annotation.MenuLevel;
import com.hand.hap.cloud.swagger.annotation.Permission;
import com.hand.todo.demo.domain.TodoTask;
import com.hand.todo.demo.service.TaskService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Created by ziling.zhong on 2017/7/5.
 */
@RestController
@RequestMapping(value = "/v1/todoTask")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * 创建task
     *
     * @param task task信息
     * @return 创建的task信息
     * @throws HapException 创建失败
     */
    @Permission(permissionLogin = true)
    @ApiOperation(value = "创建task")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<TodoTask> create(@RequestBody @Valid TodoTask task) throws HapException {
        return Optional.ofNullable(taskService.create(task))
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseThrow(() -> new HapException("error.todoTask.create"));
    }

    @Permission(permissionLogin = true)
    @ApiOperation(value = "根据任务编号获取任务")
    @RequestMapping(value = "/findByNumber/{taskNumber}", method = RequestMethod.GET)
    public ResponseEntity<TodoTask> findByNumber(@PathVariable @Valid String taskNumber) throws HapException {
        return Optional.ofNullable(taskService.findByTaskNumber(taskNumber))
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseThrow(() -> new HapException("error.todoTask.notFound"));
    }

    /**
     * 根据任务ID更新任务信息
     * <p>
     * 注意：更新task时需在json数据中添加objectVersionNumber属性及值才能更新成功。
     *
     * @param id   任务ID
     * @param task 任务信息
     * @return 更新后的任务信息
     * @throws HapException 更新失败
     */
    @Permission(permissionLogin = true)
    @ApiOperation(value = "根据任务ID更新任务信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<TodoTask> updateUser(@PathVariable Long id,
                                               @RequestBody @Valid TodoTask task) throws HapException {
        return Optional.ofNullable(taskService.update(id, task))
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseThrow(() -> new HapException("error.todoTask.update"));
    }

    @Permission(permissionLogin = true)
    @ApiOperation(value = "根据版本编号获取任务")
    @RequestMapping(value = "/findByVersionNumber/{objectVersionNumber}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> findByVersionNumber(@PathVariable @Valid Long objectVersionNumber) throws HapException {
        Map<String, Object> map = new TreeMap<>();
        map.put("tasks", taskService.findByTaskVersionNumber(objectVersionNumber));
        return Optional.ofNullable(map).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseThrow(() -> new HapException("error.todoTask.notFound"));
    }

//    @Permission(permissionLogin = true)
//    @ApiOperation(value = "根据版本编号获取任务")
//    @RequestMapping(value = "/findByVersionNumber/{objectVersionNumber}", method = RequestMethod.GET)
//    public ResponseEntity<List<TodoTask>> findByVersionNumber(@PathVariable @Valid Long objectVersionNumber) throws HapException {
//        return Optional.ofNullable(taskService.findByTaskVersionNumber(objectVersionNumber))
//                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
//                .orElseThrow(() -> new HapException("error.todoTask.findByVersionNumber"));
//    }

    @Permission(permissionLogin = true)
    @ApiOperation(value = "根据ID删除任务")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<TodoTask> deleteByEmployeeId(@PathVariable Long id) throws HapException {
        taskService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Permission(permissionLogin = true)
    @ApiOperation(value = "根据任务号码删除任务")
    @RequestMapping(value = "/taskNumber/{taskNumber}", method = RequestMethod.DELETE)
    public ResponseEntity<TodoTask> deleteByTaskNumber(@PathVariable String taskNumber) throws HapException {
        taskService.deleteByTaskNumber(taskNumber);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
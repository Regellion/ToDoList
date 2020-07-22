package main;

import main.model.TaskRepository;
import main.service.ServiceTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import main.model.Task;


import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class TaskController {
    @Autowired
    private ServiceTask serviceTask = new ServiceTask();
    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/tasks/")
    public List<Task> list(){
        return serviceTask.getListTasks();
    }

    @PostMapping("/tasks/")
    public int add(Task task){
        return serviceTask.addTask(task);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity get(@PathVariable int id){
        return serviceTask.getTask(id);
    }

    @PostMapping("/tasks/{id}")
    public ResponseEntity addTaskId(){
        return serviceTask.addTaskId();
    }

    @DeleteMapping("/tasks/")
    public ResponseEntity deleteAll(){
        return serviceTask.deleteAllTask();
    }
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity deleteTask(@PathVariable int id){
        return serviceTask.deleteTask(id);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity updateTask(Task task, @PathVariable int id){
        return serviceTask.updateTask(task, id);
    }
}


package main;

import main.model.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import main.model.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class TaskController {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/tasks/")
    public List<Task> list(){
        Iterable<Task> taskIterable = taskRepository.findAll();
        List<Task> tasks = Collections.synchronizedList(new ArrayList<>());
        for (Task task : taskIterable){
            tasks.add(task);
        }
        return tasks;
    }

    @PostMapping("/tasks/")
    public int add(Task task){
        Task newTask = taskRepository.save(task);
        return newTask.getId();
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> get(@PathVariable int id){
        Optional<Task> taskOptional = taskRepository.findById(id);
        if(!taskOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity(taskOptional.get(), HttpStatus.OK);

    }

    @PostMapping("/tasks/{id}")
    public ResponseEntity<Task> addTaskId(){
        return ResponseEntity.status(405).body(null);
    }

    @DeleteMapping("/tasks/")
    public ResponseEntity deleteAll(){
        Iterable<Task> taskIterable = taskRepository.findAll();
        List<Task> tasks = Collections.synchronizedList(new ArrayList<>());
        for (Task task : taskIterable){
            tasks.add(task);
        }
        if(tasks.size() == 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        taskRepository.deleteAll();
        return new ResponseEntity(HttpStatus.OK);

    }
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Task> deleteTask(@PathVariable int id){
        Optional<Task> taskOptional = taskRepository.findById(id);

        if(!taskOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        taskRepository.deleteById(id);
        return new ResponseEntity(taskOptional.get(), HttpStatus.OK);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity updateTask(@RequestParam Map<String, String> map, @PathVariable int id){
        Optional<Task> taskOptional = taskRepository.findById(id);
        if(!taskOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Task task = taskOptional.get();
        //Если не вводится новое имя, то оставляем прежнее
        if(map.get("name").length() != 0) {
            task.setName(map.get("name"));
        }
        //Если не вводится новое описание, то оставляем прежнее
        if(map.get("description").length() != 0) {
            task.setDescription(map.get("description"));
        }
        //Если не вводится новая дата, то оставляем преджюю
        if(map.get("date").length() != 0) {
            task.setDate(LocalDate.parse(map.get("date"), formatter));
        }
        //Обновляем дело
        taskRepository.save(task);
        return new ResponseEntity(taskOptional.get(), HttpStatus.OK);
    }
}


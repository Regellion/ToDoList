package main.service;

import main.model.Task;
import main.model.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceTask {

    private TaskRepository taskRepository;

    public ServiceTask(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public List<Task> getListTasks(){
        Iterable<Task> taskIterable = taskRepository.findAll();
        List<Task> tasks = Collections.synchronizedList(new ArrayList<>());
        for (Task task : taskIterable){
            tasks.add(task);
        }
        return tasks;
    }

    public int addTask(Task task){
        Task newTask = taskRepository.save(task);
        return newTask.getId();
    }

    public ResponseEntity getTask(int id){
        Optional<Task> taskOptional = taskRepository.findById(id);
        if(!taskOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity(taskOptional, HttpStatus.OK);
    }

    public ResponseEntity addTaskId(){
        return ResponseEntity.status(405).body(null);
    }

    public ResponseEntity deleteAllTask(){
        // Почитал статьи и стаковерфлоу, там реккомендуют не изобретать велосипед и воспользоваться методом
        // repositoryName.count() для подсчета элементов. Говорят что это современный подход)
        // Но если что, я понял что это же можно было бы сделать при помощи анотации
        // @Query("SELECT COUNT(*) FROM Task")

        if(taskRepository.count() == 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        taskRepository.deleteAll();
        return new ResponseEntity(HttpStatus.OK);
    }

    public ResponseEntity deleteTask(int id){
        Optional<Task> taskOptional = taskRepository.findById(id);
        if(!taskOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        taskRepository.deleteById(id);
        return new ResponseEntity(taskOptional, HttpStatus.OK);
    }

    public ResponseEntity updateTask(Task task, int id){
        Optional<Task> taskOptional = taskRepository.findById(id);
        if(!taskOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        task.setId(taskOptional.get().getId());

        //Обновляем дело
        taskRepository.save(task);
        return new ResponseEntity(task, HttpStatus.OK);
    }
}

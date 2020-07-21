package main;

import main.model.Task;
import main.model.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class DefaultController {
    @Autowired
    TaskRepository taskRepository;

    @RequestMapping("/")
    public String index(Model model)
    {
        Iterable<Task> taskIterable = taskRepository.findAll();
        List<Task> tasks = Collections.synchronizedList(new ArrayList<>());
        for (Task task : taskIterable){
            tasks.add(task);
        }
        model.addAttribute("tasks", tasks);
        model.addAttribute("tasksCount", tasks.size());
        return "index";
    }
}

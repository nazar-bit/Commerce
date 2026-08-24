package pjv.hello.vasylnaz.windfarmbackend;


import org.springframework.scheduling.config.Task;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class Controller {

    private List<Details> detailsList = new ArrayList<>();

    @GetMapping("/{id}")
    public Details getTask(@PathVariable int id) {
        for (Details details : detailsList) {
            if (details.getId() == id) {
                return details;
            }
        }
        return null;
    }

    @GetMapping
    public List<Details> getAllTasks() {
        return detailsList;
    }

    @PostMapping
    public void addTask(@RequestBody Details details) {
        detailsList.add(details);
    }
}

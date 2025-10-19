package org.example.java.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recipes")
public class Controller {

    private final RecipeService service;
    private final JobLauncher jobLauncher;
    private final Job job;

    public Controller(RecipeService service, JobLauncher jobLauncher, Job job) {
        this.service = service;
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @PostMapping("/start")
    public ResponseEntity<String> runJob(){
        try {
            System.out.println("Starting the Recipe Import Job...");
            JobParameters jobParameter = new JobParametersBuilder()
                    .addLong("StartJob",System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(job,jobParameter);
            System.out.println("Recipe Import Job completed successfully!");
            return ResponseEntity.ok("Batch Job Completed Successfully");
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("Batch Failed: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Map<String,Object>> getAllRecipes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ){
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("rating").descending());
        Page<RecipeEntity> recipePage = service.getRecipes(pageable);
        Map<String,Object> response = new LinkedHashMap<>();
        response.put("Page",page);
        response.put("Limit",limit);
        response.put("Total",recipePage.getTotalElements());
        response.put("Data",recipePage.getContent());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String,Object>> searchRecipe(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) Double rating,
            @RequestParam(required = false) Integer calories,
            @RequestParam(required = false, name = "total_time") Integer totalTime
    ){
        List<RecipeEntity> recipes = service.searchRecipe(title,cuisine,rating,calories,totalTime);
        Map<String,Object> response = new LinkedHashMap<>();
        response.put("data",recipes);
        return ResponseEntity.ok(response);
    }
}
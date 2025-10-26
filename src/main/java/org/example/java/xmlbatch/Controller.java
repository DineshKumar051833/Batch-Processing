package org.example.java.xmlbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cpes")
public class Controller {

    private final Service service;
    private final JobLauncher jobLauncher;
    private final Job job;

    public Controller(Service service, JobLauncher jobLauncher, Job job) {
        this.service = service;
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @PostMapping("/start")
    public ResponseEntity<String> startBatchJob(){
        try {
            System.out.println("Starting CPE Batch Processing...");
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("StartJob",System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(job,jobParameters);
            System.out.println("CPE Batch Job Completed Successfully");
            return ResponseEntity.ok("Batch Job Completed Successfully");
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("Batch Failed: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Map<String,Object>> getAllCpeS(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ){
        Pageable pageable = PageRequest.of(page-1, limit, Sort.by("id"));
        Page<CpeEntity> cpeS = service.getAll(pageable);
        Map<String,Object> response = new LinkedHashMap<>();
        response.put("Page",page);
        response.put("limit",limit);
        response.put("Total",cpeS.getTotalElements());
        response.put("Data",cpeS.getContent());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String,Object>> searchCpeS(
            @RequestParam(required = false, name = "cpe_title") String title,
            @RequestParam(required = false, name = "cpe_22_uri") String cpe22Uri,
            @RequestParam(required = false, name = "cpe_23_uri") String cpe23Uri,
            @RequestParam(required = false, name = "deprecation_date") String deprecationDate
    ){
        List<CpeEntity> list = service.searchCpeS(title,cpe22Uri,cpe23Uri,deprecationDate);
        Map<String,Object> response = new LinkedHashMap<>();
        response.put("data",list);
        return ResponseEntity.ok(response);
    }
}

package com.uor.eng.cryptx.controller;

import com.uor.eng.cryptx.model.Job;
import com.uor.eng.cryptx.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

  @Autowired
  private  JobService jobService;
    // CREATE
  @PostMapping("/create")
  public ResponseEntity<Job> createJob(@RequestBody Job job) {
    Job createdJob = jobService.createJob(job);
    return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
  }

  // READ - All
  @GetMapping("/all")
  public ResponseEntity<List<Job>> getAllJobs() {
    List<Job> jobs = jobService.getAllJobs();
    return new ResponseEntity<>(jobs, HttpStatus.OK);
  }

  // READ - By ID
  @GetMapping("/{id}")
  public ResponseEntity<Job> getJobById(@PathVariable Long id) {
    Job job = jobService.getJobById(id);
    return new ResponseEntity<>(job, HttpStatus.OK);
  }

  // UPDATE
  @PutMapping("/{id}")
  public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job updatedJob) {
    Job job = jobService.updateJob(id, updatedJob);
    return new ResponseEntity<>(job, HttpStatus.OK);
  }

  // DELETE
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
    jobService.deleteJob(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}

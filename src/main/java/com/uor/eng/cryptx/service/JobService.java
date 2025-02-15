package com.uor.eng.cryptx.service;

import com.uor.eng.cryptx.model.Job;

import java.util.List;

public interface JobService {

  Job createJob(Job job);

  List<Job> getAllJobs();

  Job getJobById(Long id);

  Job updateJob(Long id, Job updatedJob);

  void deleteJob(Long id);
}

package com.uor.eng.cryptx.service.Impl;


import com.uor.eng.cryptx.exception.JobNotFoundException;
import com.uor.eng.cryptx.model.Job;
import com.uor.eng.cryptx.repository.JobRepository;
import com.uor.eng.cryptx.service.JobService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JobServiceImpl implements JobService {

  private final JobRepository jobRepository;

  public JobServiceImpl(JobRepository jobRepository) {
    this.jobRepository = jobRepository;
  }

  @Override
  public Job createJob(Job job) {
    // Business validations can be added here
    return jobRepository.save(job);
  }

  @Override
  public List<Job> getAllJobs() {
    return jobRepository.findAll();
  }

  @Override
  public Job getJobById(Long id) {
    return jobRepository.findById(id)
        .orElseThrow(() -> new JobNotFoundException("Job not found with id: " + id));
  }

  @Override
  public Job updateJob(Long id, Job updatedJob) {
    Job existingJob = jobRepository.findById(id)
        .orElseThrow(() -> new JobNotFoundException("Cannot update. Job not found with id: " + id));

    existingJob.setRole(updatedJob.getRole());
    existingJob.setDescription(updatedJob.getDescription());
    existingJob.setSalary(updatedJob.getSalary());
    // Update relationships if needed:
    // existingJob.setBuyer(updatedJob.getBuyer());
    // existingJob.setFarmerEmployees(updatedJob.getFarmerEmployees());
    // existingJob.setFormerFarmers(updatedJob.getFormerFarmers());

    return jobRepository.save(existingJob);
  }

  @Override
  public void deleteJob(Long id) {
    Job existingJob = jobRepository.findById(id)
        .orElseThrow(() -> new JobNotFoundException("Cannot delete. Job not found with id: " + id));
    jobRepository.delete(existingJob);
  }
}

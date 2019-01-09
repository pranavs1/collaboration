package com.niit.dao;

import java.util.List;

import com.niit.model.Job;

public interface Jobdao {
void addJob(Job job);
List<Job>     getAllJobs();
}

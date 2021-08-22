package com.Batchprocess.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Batchprocess.Model.EmployeeRecord;

public interface EmployeeRecordsRepos extends JpaRepository<EmployeeRecord, Long> {

}

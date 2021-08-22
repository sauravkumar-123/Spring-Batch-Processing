package com.Batchprocess.Config;

import org.springframework.batch.item.ItemProcessor;

import com.Batchprocess.Model.EmployeeRecord;

public class EmployeeItemprocessor implements ItemProcessor<EmployeeRecord, EmployeeRecord> {

	@Override
	public EmployeeRecord process(EmployeeRecord employeeRecord) throws Exception {
		return employeeRecord;
	}

}

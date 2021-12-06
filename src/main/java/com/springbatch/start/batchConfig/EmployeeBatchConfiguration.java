package com.springbatch.start.batchConfig;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.springbatch.start.Model.Employee;

@Configuration
@EnableBatchProcessing
public class EmployeeBatchConfiguration {
    
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	//@ ItemReader
	@Bean
	public FlatFileItemReader<Employee> reader(){
		FlatFileItemReader<Employee> flatFileItemReader=new FlatFileItemReader<Employee>();
		flatFileItemReader.setResource(new ClassPathResource("employees.csv"));
		flatFileItemReader.setLineMapper(getLineMapper());
		flatFileItemReader.setLinesToSkip(1);
		return flatFileItemReader;
	}

	private LineMapper<Employee> getLineMapper() {
		DefaultLineMapper<Employee> defaultLineMapper=new DefaultLineMapper<Employee>();
		
		DelimitedLineTokenizer delimitedLineTokenizer=new DelimitedLineTokenizer();
		delimitedLineTokenizer.setNames(new String[] {"EMPLOYEE_ID","FIRST_NAME","LAST_NAME","EMAIL","PHONE_NUMBER","HIRE_DATE","JOB_ID","SALARY","MANAGER_ID","DEPARTMENT_ID"});
		delimitedLineTokenizer.setIncludedFields(new int[] {0,1,2,3,4,5,6,7,8,9});
		
		BeanWrapperFieldSetMapper<Employee> fieldSetMapper=new BeanWrapperFieldSetMapper<Employee>();
		fieldSetMapper.setTargetType(Employee.class);
		
		defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
		defaultLineMapper.setFieldSetMapper(fieldSetMapper);
		return defaultLineMapper;
	}
	
	//@ItemProcess.
	@Bean
	public EmployeeItemProcess employeeItemProcess() {
		return new EmployeeItemProcess();
	}
	
	//@ItemWriter
	@Bean
	public JdbcBatchItemWriter<Employee> writer(){
		JdbcBatchItemWriter<Employee> jdbcBatchItemWriter=new JdbcBatchItemWriter<Employee>();
		jdbcBatchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Employee>());
		jdbcBatchItemWriter.setSql("insert into employee(employeeId,firstName,lastName,email,phoneNo,hireDate,jobId,salary,managerId,departmentId) values (:employeeId,:firstName,:lastName,:email,:phoneNo,:hireDate,:jobId,:salary,:managerId,:departmentId)");
		jdbcBatchItemWriter.setDataSource(this.dataSource);
		return jdbcBatchItemWriter;
	}
	
	@Bean
	public Job employeeJob() {
		return this.jobBuilderFactory.get("EMPLOYEE-IMPORT-JOB").incrementer(new RunIdIncrementer()).flow(step1()).end().build();
	}

	@Bean
	public Step step1() {
	  return this.stepBuilderFactory.get("step1").<Employee,Employee>chunk(10).reader(reader()).processor(employeeItemProcess()).writer(writer()).build();
	
	}
}

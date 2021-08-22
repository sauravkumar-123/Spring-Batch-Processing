package com.Batchprocess.Config;

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

import com.Batchprocess.Model.EmployeeRecord;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	
	/*
	 * @Reader
	 */
	@Bean
	public FlatFileItemReader<EmployeeRecord> reader(){
		FlatFileItemReader<EmployeeRecord> reader=new FlatFileItemReader<EmployeeRecord>();
		reader.setResource(new ClassPathResource("records.csv"));
		reader.setLineMapper(getLineMapper());
		reader.setLinesToSkip(1);
	    return reader;	
	}

	private LineMapper<EmployeeRecord> getLineMapper() {
		DefaultLineMapper<EmployeeRecord> lineMapper=new DefaultLineMapper<>();
		
		DelimitedLineTokenizer lineTokenizer=new DelimitedLineTokenizer();
		/*
		 * Read Column name and index position from a csv file.
		 * 
		 */
		lineTokenizer.setNames(new String[] {"Emp ID","Name Prefix","First Name","Last Name","Gender","E Mail","Father's Name","Mother's Name","Date of Birth",
				                             "Age in Yrs.","Weight in Kgs.","Date of Joining","Quarter of Joining","Half of Joining","Year of Joining","Month of Joining",
				                             "Month Name of Joining","Short Month","Day of Joining","DOW of Joining","Short DOW","Age in Company (Years)","Salary","Last % Hike",
				                             "SSN","Phone No.","Place Name","County","City","State","Zip","Region","User Name","Password"});
		
		lineTokenizer.setIncludedFields(new int[] {0,1,2,4,5,6,7,8,10,12,13,14,15,16,17,18,19,20,21,22,23,24,25,36,27,28,29,30,31,32,33,34});
		
		BeanWrapperFieldSetMapper<EmployeeRecord> fieldSetMapper=new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(EmployeeRecord.class);
		
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		
		return lineMapper;
	}
	
	/*
	 *@Processor
	 */
	@Bean
	public EmployeeItemprocessor processor() {
		return new EmployeeItemprocessor();
	}
	
	/*
	 * @Writter.
	 */
	@Bean
	public JdbcBatchItemWriter<EmployeeRecord> writter(){
		JdbcBatchItemWriter<EmployeeRecord> writter=new JdbcBatchItemWriter<>();
		writter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<EmployeeRecord>());
		writter.setSql("INSERT INTO batchprocessing (Emp ID, Name Prefix, First Name, Last Name, Gender, E Mail, Father's Name, Mother's Name, Date of Birth, Age in Yrs., Weight in Kgs., Date of Joining, Quarter of Joining, Half of Joining,  Year of Joining, Month of Joining, Month Name of Joining,"
				+ "Short Month, Day of Joining, DOW of Joining, Short DOW, Age in Company (Years), Salary, Last % Hike, SSN, Phone No, Place Name, County, City, State, Zip, Region, User Name, Password) VALUES (:employeeId, :namePrefix, :firstName, :lastName, :gender, :email, :fathersName, :mothersName,"
				+ ":dob, :ageinyears, :weightinkg, :doj, :qoj, :hoj, :yoj, :moj, :monthname, :shortmonth, :dayofjoining, :dow, :shortdow, :ageincompany, :salary, :hikepercent, :ssn, :phoneno, :palacename, :country, :state, :city, :zipcode, :region, :username, :password)");
		writter.setDataSource(this.dataSource);
		return writter;
	}
	
	/*
	 * @Job
	 */
	@Bean
	public Job importUserJob() {
		return this.jobBuilderFactory.get("EMPLOYEERECORD-IMPORT-JOB")
				.incrementer(new RunIdIncrementer())
				.flow(step1())
				.end()
				.build();
	}

	@Bean
	public Step step1() {
		return this.stepBuilderFactory.get("step1")
		.<EmployeeRecord,EmployeeRecord>chunk(10)
		.reader(reader())
		.processor(processor())
		.writer(writter())
		.build();
	}
}

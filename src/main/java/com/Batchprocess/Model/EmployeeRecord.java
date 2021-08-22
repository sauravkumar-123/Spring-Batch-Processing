package com.Batchprocess.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employeerecord")
public class EmployeeRecord {

	@Id
	@Column(name="Emp ID")
	private long employeeId;
	
	@Column(name="Name Prefix",length = 5)
	private String namePrefix;
	
	@Column(name="First Name",length = 40)
	private String firstName;
	
	@Column(name="Last Name",length = 40)
	private String lastName;
	
	@Column(name="Gender")
	private char gender;
	
	@Column(name="E Mail",length = 60)
	private String email;
	
	@Column(name="Father's Name",length = 40)
	private String fathersName;
	
	@Column(name="Mother's Name",length = 40)
	private String mothersName;
	
	@Column(name="Date of Birth",length = 20)
	private String dob;
	
	@Column(name="Age in Yrs.")
	private double ageinyears;
	
	@Column(name="Weight in Kgs.")
	private double weightinkg;
	
	@Column(name="Date of Joining",length = 20)
	private String doj;
	
	@Column(name="Quarter of Joining",length = 20)
	private String qoj;
	
	@Column(name="Half of Joining",length = 20)
	private String hoj;
	
	@Column(name="Year of Joining")
	private int yoj;
	
	@Column(name="Month of Joining")
	private int moj;
	
	@Column(name="Month Name of Joining",length = 20)
	private String monthname;
	
	@Column(name="Short Month",length = 20)
	private String shortmonth;
	
	@Column(name="Day of Joining")
	private int dayofjoining;
	
	@Column(name="DOW of Joining",length = 40)
	private String dow;
	
	@Column(name="Short DOW",length = 20)
	private String shortdow;
	
	@Column(name="Age in Company (Years)")
	private double ageincompany;
	
	@Column(name="Salary")
	private long salary;
	
	@Column(name="Last % Hike",length = 10)
	private String hikepercent;
	
	@Column(name="SSN",length = 60)
	private String ssn;
	
	@Column(name="Phone No.",length = 30)
	private String phoneno;
	
	@Column(name="Place Name",length = 50)
	private String palacename;
	
	@Column(name="County",length = 40)
	private String country;
	
	@Column(name="City",length = 40)
	private String city;
	
	@Column(name="State",length = 20)
	private String state;
	
	@Column(name="Zip")
	private long zipcode;
	
	@Column(name="Region",length = 40)
	private String region;
	
	@Column(name="User Name",length = 50)
	private String username;
	
	@Column(name="Password",length = 100)
	private String password;	
}

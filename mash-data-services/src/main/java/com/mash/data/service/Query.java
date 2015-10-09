package com.mash.data.service;

import com.mash.model.catalog.Location;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Query")
@XmlRootElement(name = "query")
public class Query {
	
	 protected String firstName;
	 protected String lastName;
	 protected Date dateOfBirthFrom;
	 protected Date dateOfBirthTo;
		
	 protected String gender;
	 
	 protected Location sampleLocation;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDateOfBirthFrom() {
		return dateOfBirthFrom;
	}

	public void setDateOfBirthFrom(Date dateOfBirthFrom) {
		this.dateOfBirthFrom = dateOfBirthFrom;
	}

	public Date getDateOfBirthTo() {
		return dateOfBirthTo;
	}

	public void setDateOfBirthTo(Date dateOfBirthTo) {
		this.dateOfBirthTo = dateOfBirthTo;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Location getSampleLocation() {
		return sampleLocation;
	}

	public void setSampleLocation(Location sampleLocation) {
		this.sampleLocation = sampleLocation;
	}
	 
	 
	 

}

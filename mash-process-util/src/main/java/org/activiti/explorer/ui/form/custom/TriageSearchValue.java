package org.activiti.explorer.ui.form.custom;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;


public class TriageSearchValue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4666745825592153896L;
	private String value;
	
	private List<TriagePersonSummary> subjects;
	

	public TriageSearchValue(String value,List<TriagePersonSummary> subjects) {
		
		this.value = value;
		
		this.subjects = subjects;
	}
	
	public String getValue() {
		return value;
	}
	
	
	 public String objectToString() {
		  String encoded = null;
		  
	
		  return value;
		 }
	 
	  public static TriageSearchValue stringToObject(String string) {
		  
		  TriageSearchValue value = new TriageSearchValue(string,new ArrayList<TriageSearchValue.TriagePersonSummary>());
		  return value;
		 }
	  
	  
	  public List<TriagePersonSummary> getSubjects() {
		return subjects;
	}
	  public static class TriagePersonSummary implements Serializable
	  {
		  
		
		private static final long serialVersionUID = -940469004097521346L;

		private String id;
		  
	    private String firstName;
		  
		private String lastName;
		  
		private String homeAddress;
		  
		private String relationship;
		  
		private Date dateOfBirth;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

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

		public String getHomeAddress() {
			return homeAddress;
		}

		public void setHomeAddress(String homeAddress) {
			this.homeAddress = homeAddress;
		}

		public String getRelationship() {
			return relationship;
		}

		public void setRelationship(String relationship) {
			this.relationship = relationship;
		}

		public Date getDateOfBirth() {
			return dateOfBirth;
		}

		public void setDateOfBirth(Date dateOfBirth) {
			this.dateOfBirth = dateOfBirth;
		} 
		  
		  
		  
	  }

}

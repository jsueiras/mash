package org.activiti.explorer.ui.form.custom;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import mash.graph.NetworkState;

public class TriageSearchValue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4666745825592153896L;
	private String value;
	private NetworkState networkState;
	private transient List<TriagePersonSummary> subjects;
	

	public TriageSearchValue(String value, NetworkState networkState, List<TriagePersonSummary> subjects) {
		
		this.value = value;
		this.networkState = networkState;
		this.subjects = subjects;
	}
	
	public String getValue() {
		return value;
	}
	
	public NetworkState getNetworkState() {
		return networkState;
	}
	
	 public String objectToString() {
		  String encoded = null;
		  
		  try {
		   ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		   ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		   objectOutputStream.writeObject(this);
		   objectOutputStream.close();
		   encoded = new String(Base64.encodeBase64(byteArrayOutputStream.toByteArray()));
		  } catch (IOException e) {
		   e.printStackTrace();
		  }
		  return encoded;
		 }
	 
	  public static TriageSearchValue stringToObject(String string) {
		  byte[] bytes = Base64.decodeBase64(string);
		  Object object = null;
		  try {
		   ObjectInputStream objectInputStream = new ObjectInputStream( new ByteArrayInputStream(bytes) );
		   object = objectInputStream.readObject();
		  } catch (IOException e) {
		   e.printStackTrace();
		  } catch (ClassNotFoundException e) {
		   e.printStackTrace();
		  } catch (ClassCastException e) {
		   e.printStackTrace();
		  }
		  return (TriageSearchValue)object;
		 }
	  
	  protected static class TriagePersonSummary implements Serializable
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

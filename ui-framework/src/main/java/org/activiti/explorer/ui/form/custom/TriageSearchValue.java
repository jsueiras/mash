package org.activiti.explorer.ui.form.custom;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.codec.binary.Base64;

import mash.graph.NetworkState;

public class TriageSearchValue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4666745825592153896L;
	private String value;
	private NetworkState networkState;

	public TriageSearchValue(String value, NetworkState networkState) {
		
		this.value = value;
		this.networkState = networkState;
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

}

package org.activiti.explorer.ui.form.custom;

import java.io.Serializable;

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

}

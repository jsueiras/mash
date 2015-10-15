package mash.graph;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Node implements Serializable {
	public final String id;
	public String label;

	public Node(String id) {
		this.id = id;
	}
	
	public Node(String id,String label) {
		this(id);
		this.label = label;
		
	}
	
	@Override
	public boolean equals(Object obj) {
      return (obj!= null && obj.getClass() == getClass()) && (id!=null) &&  id.equals(((Node)obj).id);	    
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return new HashCodeBuilder(17, 31).append(id).toHashCode();
	}
	
}

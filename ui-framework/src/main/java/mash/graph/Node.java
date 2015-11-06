package mash.graph;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class Node implements Serializable {
	public static enum Group {
		FEMALES, FEMALES_UNEXPLORED,
		MALES, MALES_UNEXPLORED,
		LOCATIONS, LOCATIONS_UNEXPLORED;

	     public boolean isUnexplored()
	     {
	    	return this.toString().contains("_UNEXPLORED");
	     }

	}

	private static final long serialVersionUID = -2561388826782706977L;

	public final String id;
	public final String label;

	public Group group;

	/** Tooltip */
	public String title = "";

	public int age = -1;
	public boolean primary = false;
	public boolean warning = false;

	public Node(String id, String label, Group group) {
		if (id == null) {
			throw new IllegalArgumentException("Node.id cannot be null.");
		}
		this.id = id;
		this.label = label;
		this.group = group;
	}

	@Override
	public boolean equals(Object obj) {
		return (obj != null && obj.getClass() == getClass()) && ((id == ((Node) obj).id) || id.equals(((Node) obj).id));
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(id).toHashCode();
	}
}

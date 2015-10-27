package mash.graph;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class Node implements Serializable {
	public static enum Group {
		FEMALES, FEMALES_UNEXPLORED,
		MALES, MALES_UNEXPLORED,
		LOCATIONS, LOCATIONS_UNEXPLORED;
	}

	private static final long serialVersionUID = -2561388826782706977L;

	public final String id;
	public String label;
	public Group group;
	public int age = -1;
	public boolean primary = false;

	public Node(String id) {
		if (id == null) {
			throw new IllegalArgumentException("Node.id cannot be null.");
		}
		this.id = id;
	}

	public Node(String id, String label, Group group) {
		this(id);
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

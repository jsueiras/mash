package mash.graph;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class Node implements Serializable {
	public enum Group {
		FEMALES, MALES, PERSONS, LOCATIONS;
	}

	public final String id;
	public String label;
	public Group group;

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

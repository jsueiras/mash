package mash.graph;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Edge implements Serializable {


	private static final long serialVersionUID = 130821260933146318L;

	public final String from;
	public final String to;
	public final String label;
	public final Arrows arrows = new Arrows();

	/** Tooltip */
	public String title = "";

	public Edge(String from, String to, String label) {
		this.from = from;
		this.to = to;
		this.label = label;
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj == null) || (obj.getClass() != getClass()) || (from == null) || (to == null))
			return false;
		Edge edge = (Edge) obj;
		return from.equals(edge.from) && to.equals(edge.to) || from.equals(edge.to) && to.endsWith(edge.from);

	}

	@Override
	public int hashCode() {
		//The multiplier is 1 so the factor
		int val = new HashCodeBuilder(17, 31).toHashCode();
		if (to != null)
			val = val + to.hashCode();
		if (from != null)
			val = val + from.hashCode();
		return val;
	}

	/**
	 * Determines on which ends of the edge to draw an arrow.
	 */
	public static class Arrows implements Serializable{
		public boolean to = true;
		public boolean from = false;
	}
}

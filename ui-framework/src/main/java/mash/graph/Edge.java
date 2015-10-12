package mash.graph;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Edge {
	
   public final String from;
   
   public final String to;
   
   public String label;
   
   public Edge(String from, String to) {
	  this.from = from;
	  this.to = to;
   }
   
   public Edge(String from, String to, String label) {
	 this(from,to);
	 this.label = label;
}

@Override
	public boolean equals(Object obj) {
		if ((obj==null) || (obj.getClass() != getClass()) || (from ==null) || (to ==null)) return false;
		Edge edge = (Edge)obj;
		return from.equals(edge.from) && to.equals(edge.to) || from.equals(edge.to) && to.endsWith(edge.from);
		
	}

@Override
public int hashCode() {
	//The multiplier is 1 so the factor
	int val = new HashCodeBuilder(17, 31).toHashCode() ;
	if (to!=null) val= val +to.hashCode();
	if (from!=null) val = val + from.hashCode();
	return val;
}

}

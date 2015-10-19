package mash.graph.util;

import java.util.List;

import mash.graph.Edge;
import mash.graph.NetworkState;
import mash.graph.Node;

import com.mash.model.catalog.Entity;
import com.mash.model.catalog.Location;
import com.mash.model.catalog.Occupant;
import com.mash.model.catalog.Person;
import com.mash.model.catalog.Relation;

public class NetworkBuilder {


	 private  String getLabel(Person person){
		return  String.format("%s %s", person.getFirstName(), person.getLastName());
	 }

	 private String getLabel(Location location) {
		if (location== null) return "";
		return  String.format("%s %s %s", location.getNumberOrName(), location.getStreet(),location.getCity());
	 }

	 private String getId(Entity entity){
			return entity.getId();
	 }

	 private String getLabel(Entity entity){
			if (entity instanceof Person)
			   return getLabel((Person)entity);
			else
			  return getLabel((Location)entity);
	 }

	private  void appendNode(NetworkState state,Person person) {

		state.nodes.add(createNode(person, Node.Group.PERSONS));
       if  (person.getHousehold()!=null)
       {
        for (Relation relation : person.getHousehold().getRelations()) {
           state.nodes.add(createNode(relation.getPerson(), Node.Group.PERSONS));
           state.edges.add(createEdge(person,relation.getPerson(),relation.getType()));

        }
       }

        if (person.getHomeAddress()!=null && person.getHomeAddress().getLocation()!= null)
        {
        	Location location = person.getHomeAddress().getLocation();
        	state.nodes.add(createNode(location, Node.Group.LOCATIONS));
        	state.edges.add(createEdge(person, location, "Home Address"));
        }

	 }


	private void appendNode(NetworkState state,Location location) {

		state.nodes.add(createNode(location, Node.Group.LOCATIONS));

	    for (Occupant occupant : location.getOccupants()) {
	           state.nodes.add(createNode(occupant.getPerson(), Node.Group.PERSONS));
	           state.edges.add(createEdge(location,occupant.getPerson(),"Occupant"));
		}

	}

	private void appendNode(NetworkState state,Entity entity) {

		if (entity instanceof Person)
			appendNode(state, (Person)entity);
		else
			appendNode(state, (Location)entity);

	}

	private Edge createEdge(Entity source, Entity target, String label) {
		return new Edge(getId(source), getId(target),label);
	}

	private Node createNode(Entity entity, Node.Group group) {
		Node node = new Node(getId(entity), getLabel(entity), group);
		return node;
	}


	public NetworkState initNetworkState(List<Entity> entities)
	{
		NetworkState state = new NetworkState();
		addNodesToNetwork(state, entities);
		return state;
	}

	public void addNodesToNetwork(NetworkState state,List<Entity> entities) {
		for (Entity entity : entities) {
			appendNode(state, entity);
		}
	}


}

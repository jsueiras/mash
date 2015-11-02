package mash.graph.util;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import mash.graph.Edge;
import mash.graph.NetworkState;
import mash.graph.Node;
import mash.graph.Node.Group;

import com.mash.model.catalog.Entity;
import com.mash.model.catalog.Location;
import com.mash.model.catalog.Occupant;
import com.mash.model.catalog.Person;
import com.mash.model.catalog.Relation;

import javax.xml.datatype.XMLGregorianCalendar;

public class NetworkBuilder {

	private static final String LIVES_AT = "lives at";
	private static final Set<String> nondirectionalSet = new HashSet<String>();

	static {
		nondirectionalSet.add("married");
		nondirectionalSet.add("sibiling");
		nondirectionalSet.add("friend");
		nondirectionalSet.add("divorced");
		nondirectionalSet.add("partner");
		nondirectionalSet.add("cousin");
	}

	private String getLabel(Person person) {
		return String.format("%s %s", person.getFirstName(), person.getLastName());
	}

	private String getLabel(Location location) {
		if (location == null)
			return "";
		return String.format("%s %s %s", location.getNumberOrName(), location.getStreet(), location.getCity());
	}

	private String getId(Entity entity) {
		return entity.getId();
	}

	private String getLabel(Entity entity) {
		if (entity instanceof Person)
			return getLabel((Person) entity);
		else
			return getLabel((Location) entity);
	}

	private Node appendNode(NetworkState state, Person person) {

		Node newNode = createNode(person);
		state.nodes.add(newNode);
		if (person.getHousehold() != null) {
			for (Relation relation : person.getHousehold().getRelations()) {
				Person relatedPerson = relation.getPerson();
				state.nodes.add(createNode(relatedPerson));
				state.edges.add(createEdge(person, relation.getPerson(), relation.getType()));

			}
		}

		if (person.getHomeAddress() != null && person.getHomeAddress().getLocation() != null) {
			Location location = person.getHomeAddress().getLocation();
			state.nodes.add(createNode(location));
			state.edges.add(createEdge(person, location, LIVES_AT));
		}
		return newNode;
	}

	private Node appendNode(NetworkState state, Location location) {

		Node newNode = createNode(location);
		state.nodes.add(newNode);

		for (Occupant occupant : location.getOccupants()) {
			state.nodes.add(createNode(occupant.getPerson()));
			state.edges.add(createEdge(occupant.getPerson(), location, LIVES_AT));
		}
		return newNode;

	}

	private Node appendNode(NetworkState state, Entity entity) {

		if (entity instanceof Person)
			return appendNode(state, (Person) entity);
		else
			return appendNode(state, (Location) entity);

	}


	private Edge createEdge(Entity source, Entity target, String label) {
		Edge edge = new Edge(getId(source), getId(target), label.toLowerCase());
		edge.arrows.to = !isBidirectional(label);
		return edge;
	}

	private Node createNode(Entity entity) {
		Node.Group group = Node.Group.LOCATIONS_UNEXPLORED;
		int age = -1;
		if (entity instanceof Person) {
			Person person = (Person) entity;
			XMLGregorianCalendar dateOfBirth = person.getDateOfBirth();
			age = age(dateOfBirth);
			if ("female".equalsIgnoreCase(person.getGender())) {
				group = Node.Group.FEMALES_UNEXPLORED;
			} else if ("male".equalsIgnoreCase(person.getGender())) {
				group = Node.Group.MALES_UNEXPLORED;
			}
		}
		Node node = new Node(getId(entity), getLabel(entity), group);
		node.age = age;
		return node;
	}

	private boolean isBidirectional(String type) {
		return nondirectionalSet.contains(type.toLowerCase());
	}

	private int age(XMLGregorianCalendar dateOfBirth) {
		if (dateOfBirth != null) {
			int year = dateOfBirth.getYear();
			int month = dateOfBirth.getMonth();
			int day = dateOfBirth.getDay();
			return Period.between(LocalDate.of(year, month, day), LocalDate.now()).getYears();
		} else {
			return -1;
		}
	}

	
	
	public void addNodesToNetwork(NetworkState state, List<Entity> entities)
	{
		addNodesToNetwork(state, entities,false);
	}

	public void addNodesToNetwork(NetworkState state, List<Entity> entities,boolean initial) {
		Set expandedIds = new HashSet<String>();
	
		for (Entity entity : entities) {
			appendNode(state, entity);
			expandedIds.add(entity.getId());
		}
		for (Iterator<Node> iterator = state.nodes.iterator(); iterator.hasNext();) {
			Node node = iterator.next();
			if (expandedIds.contains(node.id)) {
				setExpanded(node); 
			}
			if (initial && node.id.equals(entities.get(0).getId()))
			{
				node.primary = true;
			}	
		}
			
		
	}
	
	public Node addNodesToNetwork(NetworkState state, Entity entity) {
		Set expandedIds = new HashSet<String>();
	
			Node result = appendNode(state, entity);
			setExpanded(result);
			return result;
	}

	private void setExpanded(Node node) {
		String currentGroup = node.group.toString();
		node.group = Group.valueOf(currentGroup.replace("_UNEXPLORED", ""));
	}


}

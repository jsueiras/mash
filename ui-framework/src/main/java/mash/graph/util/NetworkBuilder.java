package mash.graph.util;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mash.graph.Edge;
import mash.graph.NetworkState;
import mash.graph.Node;

import com.mash.model.catalog.Entity;
import com.mash.model.catalog.Location;
import com.mash.model.catalog.Occupant;
import com.mash.model.catalog.Person;
import com.mash.model.catalog.Relation;

import javax.xml.datatype.XMLGregorianCalendar;

public class NetworkBuilder {

	private static final String LIVES_AT = "lives at";
	private static final Set<String> nondirecctionalSet = new HashSet<String>();

	static
	{
		nondirecctionalSet.add("married");
		nondirecctionalSet.add("sibiling");
		nondirecctionalSet.add("friend");
		nondirecctionalSet.add("divorced");
		nondirecctionalSet.add("partner");
		nondirecctionalSet.add("cousin");
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

	private void appendNode(NetworkState state, Person person) {

		state.nodes.add(createNode(person));
		if (person.getHousehold() != null) {
			for (Relation relation : person.getHousehold().getRelations()) {
				Person relatedPerson = relation.getPerson();
				state.nodes.add(createNode(relatedPerson));
				appendAge(state, relatedPerson);
				state.edges.add(createEdge(person, relation.getPerson(), relation.getType()));

			}
		}

		if (person.getHomeAddress() != null && person.getHomeAddress().getLocation() != null) {
			Location location = person.getHomeAddress().getLocation();
			state.nodes.add(createNode(location));
			state.edges.add(createEdge(person, location, LIVES_AT));
		}
		appendAge(state, person);
	}

	private void appendAge(NetworkState state, Person person) {
		XMLGregorianCalendar dateOfBirth = person.getDateOfBirth();
		int age = age(dateOfBirth);
		String personId = person.getId();
		String ageId = "age-" + personId;

		Node ageNode = new Node(ageId, "" + age, Node.Group.AGE);
		state.nodes.add(ageNode);

		Edge ageEdge = new Edge(personId, ageId, "age");
		ageEdge.length = 0;
		ageEdge.arrows.to = false;
		state.edges.add(ageEdge);
	}


	private void appendNode(NetworkState state, Location location) {

		state.nodes.add(createNode(location));

		for (Occupant occupant : location.getOccupants()) {
			state.nodes.add(createNode(occupant.getPerson()));
			state.edges.add(createEdge( occupant.getPerson(),location, LIVES_AT));
		}

	}

	private void appendNode(NetworkState state, Entity entity) {

		if (entity instanceof Person)
			appendNode(state, (Person) entity);
		else
			appendNode(state, (Location) entity);

	}


	private Edge createEdge(Entity source, Entity target, String label) {
		Edge edge = new Edge(getId(source), getId(target), label.toLowerCase());
		edge.arrows.to = !isBidirecctional(label);
		return edge;
	}

	private Node createNode(Entity entity) {
		Node.Group group = Node.Group.LOCATIONS;
		if (entity instanceof Person) {
			Person person = (Person) entity;
			XMLGregorianCalendar dateOfBirth = person.getDateOfBirth();
			boolean underAge = isUnderAge(dateOfBirth);
			if ("female".equalsIgnoreCase(person.getGender())) {
				group = underAge ? Node.Group.FEMALES_UNDERAGE : Node.Group.FEMALES;
			} else if ("male".equalsIgnoreCase(person.getGender())) {
				group = underAge ? Node.Group.MALES_UNDERAGE : Node.Group.MALES;
			} else {
				group = Node.Group.PERSONS;
			}
		}
		Node node = new Node(getId(entity), getLabel(entity), group);
		return node;
	}


	private boolean isBidirecctional(String type)
	{
		return nondirecctionalSet.contains(type.toLowerCase());
	}

	private boolean isUnderAge(XMLGregorianCalendar dateOfBirth) {

		boolean underAge = false;
		if  (dateOfBirth!=null)

			underAge=Period.between(LocalDate.of(dateOfBirth.getYear(), dateOfBirth.getMonth(), dateOfBirth.getDay()),
						LocalDate.now()).getYears() < 18;
		return underAge;
	}

	private int age(XMLGregorianCalendar dateOfBirth) {
		return Period.between(LocalDate.of(dateOfBirth.getYear(), dateOfBirth.getMonth(), dateOfBirth.getDay()),
				LocalDate.now()).getYears();
	}


	public NetworkState initNetworkState(List<Entity> entities) {
		NetworkState state = new NetworkState();
		addNodesToNetwork(state, entities);
		return state;
	}

	public void addNodesToNetwork(NetworkState state, List<Entity> entities) {
		for (Entity entity : entities) {
			appendNode(state, entity);
		}
	}


}

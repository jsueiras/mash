package mash.graph;

import com.mash.data.service.Repository;
import com.mash.model.catalog.Entity;
import com.mash.model.catalog.Location;
import com.mash.model.catalog.Occupant;
import com.mash.model.catalog.Person;
import com.mash.model.catalog.Relation;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.shared.ui.JavaScriptComponentState;
import com.vaadin.ui.AbstractJavaScriptComponent;

import mash.graph.util.NetworkBuilder;

import org.activiti.explorer.ExplorerApp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@JavaScript({"webjars/visjs/4.8.2/vis.min.js", "js/network-connector.js"})
@StyleSheet({"webjars/visjs/4.8.2/vis.min.css", "css/network.css"})
public class Network extends AbstractJavaScriptComponent {

	public Network(boolean isLocation, String id) {
		addStyleName("mash-network");
		setSizeFull();

		initNetwork(isLocation, id);
	}

	private void initNetwork(boolean isLocation, String id) {
		NetworkBuilder builder = new NetworkBuilder();
		if (id == null) {
			// clear the graph
		} else {
			if (isLocation) {
				builder.addNodesToNetwork(getState(), getLocationPrimaryLinks(id));
				
			} else {
				builder.addNodesToNetwork(getState(), getPersonPrimaryLinks(id));
			}
		}
	}

	private List<Entity> getPersonPrimaryLinks(String id) {
		Repository mashRep = ExplorerApp.get().getMashRepository();
		List<String> ids = new ArrayList<String>();
		Person person = mashRep.findPersonById(id);
		List<Entity> entities;
		
		if (person.getHomeAddress() != null && person.getHomeAddress().getLocation()!= null)
		   ids.add(person.getHomeAddress().getLocation().getId());
		 
		if  (person.getHousehold()!= null && person.getHousehold().getRelations() != null)
		{	
		   for ( Relation relation : person.getHousehold().getRelations()) {
				ids.add(relation.getPerson().getId());
			}
		}
		if  (ids.size() >0) entities = mashRep.findEntitiesById(ids);
		else entities = new ArrayList<Entity>();
		entities.add(0,person);

		return entities;
	}

	private List<Entity> getLocationPrimaryLinks(String id) {
	    Repository mashRep = ExplorerApp.get().getMashRepository();
		Location location = mashRep.findLocationById(id);
		List<Entity> entities;
		if  (location.getOccupants()!=null && location.getOccupants().size()>0)
		{	
			List<String> ids = new ArrayList<String>();
			
			for (Occupant person : location.getOccupants()) {
				ids.add(person.getPerson().getId());
			}
		   entities = mashRep.findEntitiesById(ids);
		}
		else
		{
			entities = new ArrayList<Entity>();
		}		
		
		
		entities.add(0,location);
		return entities;
		
	}

	@Override
	protected NetworkState getState() {
		NetworkState state = (NetworkState) super.getState();
		if (state.nodes == null) state.nodes = new HashSet<Node>();
		if (state.edges ==null) state.edges = new HashSet<Edge>();
		return state;
	}
}

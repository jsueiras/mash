package mash.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mash.graph.util.NetworkBuilder;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.ui.form.custom.TriageSearchValue;
import org.activiti.explorer.ui.search.SearchDetailPanel;
import org.activiti.explorer.ui.search.SearchTabEvent;

import com.mash.data.service.Repository;
import com.mash.model.catalog.Entity;
import com.mash.model.catalog.Location;
import com.mash.model.catalog.Occupant;
import com.mash.model.catalog.Person;
import com.mash.model.catalog.Relation;
import com.vaadin.ui.CssLayout;

public class NetworkPanel extends CssLayout {
	
	
	private static final String TRIAGE_REASON = "triageReason";
	private RuntimeService runtimeService;
	
	
	public NetworkPanel() 
	{
		   this.runtimeService = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
		   
	}

	public void setRootEntity(boolean isLocation,String id)
	{
		  removeAllComponents();
		  if (id!=null)
		  {	  
	         addComponent(initNetwork(isLocation, id));
		  }   

	}
	public void initTask(Task task)
	{
		  
		  removeAllComponents();
		   Object value = runtimeService.getVariable(task.getProcessInstanceId(), TRIAGE_REASON);
		   if (value!=null)
		   {
			   TriageSearchValue reason = TriageSearchValue.stringToObject((String) value);
			   addComponent(new Network(reason.getNetworkState()));
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
	
	private Network initNetwork(boolean isLocation, String id) {
		NetworkBuilder builder = new NetworkBuilder();
		NetworkState state = new NetworkState();
		state.edges = new HashSet<Edge>();
		state.nodes = new HashSet<Node>();
		
		if (isLocation) {
			builder.addNodesToNetwork(state, getLocationPrimaryLinks(id));
			
		} else {
			builder.addNodesToNetwork(state, getPersonPrimaryLinks(id));
		}		
		 fireEvent(new NetworkChangeEvent(NetworkPanel.this, state));
				
		return new Network(state);
		
	}
	
	public void addNetworkChangeEventListener(NetworkChangeListener listener)
	{
	    addListener(listener);
	}


}

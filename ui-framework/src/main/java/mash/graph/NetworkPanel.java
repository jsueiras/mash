package mash.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
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

public class NetworkPanel extends VerticalSplitPanel {


	private static final String TRIAGE_REASON = "triageReason";
	private RuntimeService runtimeService;
	private Network network = null;

	public NetworkPanel() {
		this.runtimeService = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
		setSecondComponent(new TabSheet());

		setSplitPosition(100, Unit.PERCENTAGE);
		setLocked(true);
	}

	public void setRootEntity(boolean isLocation, String id) {
		setFirstComponent(null);
		if (id != null) {
			network = initNetwork(isLocation, id);
			setFirstComponent(network);
		}
	}

	public void initTask(Task task) {
		setFirstComponent(null);
		Object value = runtimeService.getVariable(task.getProcessInstanceId(), TRIAGE_REASON);
		if (value != null) {
			TriageSearchValue reason = TriageSearchValue.stringToObject((String) value);
			NetworkState networkState = reason.getNetworkState();
			if (reason != null && networkState != null) {
				network = new Network(networkState.nodes, networkState.edges);
				setFirstComponent(network);
			}
		}
	}


	private List<Entity> getPersonPrimaryLinks(String id) {
		Repository mashRep = ExplorerApp.get().getMashRepository();
		List<String> ids = new ArrayList<String>();
		Person person = mashRep.findPersonById(id, null);
		List<Entity> entities;

		if (person.getHomeAddress() != null && person.getHomeAddress().getLocation() != null)
			ids.add(person.getHomeAddress().getLocation().getId());

		if (person.getHousehold() != null && person.getHousehold().getRelations() != null) {
			for (Relation relation : person.getHousehold().getRelations()) {
				ids.add(relation.getPerson().getId());
			}
		}
		if (ids.size() > 0)
			entities = mashRep.findEntitiesById(ids, null);
		else
			entities = new ArrayList<Entity>();
		entities.add(0, person);

		return entities;
	}

	private List<Entity> getLocationPrimaryLinks(String id) {
		Repository mashRep = ExplorerApp.get().getMashRepository();
		Location location = mashRep.findLocationById(id, null);
		List<Entity> entities;
		if (location.getOccupants() != null && location.getOccupants().size() > 0) {
			List<String> ids = new ArrayList<String>();

			for (Occupant person : location.getOccupants()) {
				ids.add(person.getPerson().getId());
			}
			entities = mashRep.findEntitiesById(ids, null);
		} else {
			entities = new ArrayList<Entity>();
		}


		entities.add(0, location);
		return entities;

	}

	private Network initNetwork(boolean isLocation, String id) {
		NetworkBuilder builder = new NetworkBuilder();
		NetworkState state = new NetworkState();
		state.edges = new HashSet<>();
		state.nodes = new HashSet<>();
		List<Entity> primaryLinks;
		if (isLocation) {
			primaryLinks = getLocationPrimaryLinks(id);
			builder.addNodesToNetwork(state, primaryLinks);

		} else {
			primaryLinks = getPersonPrimaryLinks(id);
			builder.addNodesToNetwork(state, primaryLinks);
		}
		fireEvent(new NetworkChangeEvent(NetworkPanel.this, state,primaryLinks));

		return new Network(state.nodes, state.edges);

	}

	public void addNetworkChangeEventListener(NetworkChangeListener listener) {
		addListener(listener);
	}


}

package mash.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.vaadin.ui.*;

import com.vaadin.ui.themes.ValoTheme;
import mash.graph.Network.ClickEvent;
import mash.graph.Network.ClickListener;
import mash.graph.util.NetworkBuilder;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.task.Task;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.ui.Images;
import org.activiti.explorer.ui.form.custom.TriageSearchValue;
import org.activiti.explorer.ui.mainlayout.ExplorerLayout;
import org.activiti.explorer.ui.search.util.Decorator;
import org.activiti.explorer.ui.util.ThemeImageColumnGenerator;

import com.mash.data.service.Repository;
import com.mash.model.catalog.Entity;
import com.mash.model.catalog.Location;
import com.mash.model.catalog.Occupant;
import com.mash.model.catalog.Person;
import com.mash.model.catalog.Relation;
import com.mash.model.catalog.Source;
import com.mash.model.catalog.SystemId;

public class NetworkPanel extends VerticalSplitPanel {


	private static final String TRIAGE_REASON = "triageReason";
	private RuntimeService runtimeService;
	private Network network = null;
	private Repository mashRep;
	private NetworkBuilder builder;
	private TabSheet detailTabSheet;
	private String taskName;

	public NetworkPanel() {
		this.runtimeService = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
		mashRep = ExplorerApp.get().getMashRepository();
		builder = new NetworkBuilder();
		detailTabSheet = new TabSheet();
		detailTabSheet.setId("detail-tab-sheet");
		detailTabSheet.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);

		setSecondComponent(detailTabSheet);
		detailTabSheet.setSizeFull();

		setSplitPosition(100, Unit.PERCENTAGE);
		setLocked(true);
	}

	public void setRootEntity(boolean isLocation, String id) {
		setFirstComponent(null);
		if (id != null) {
			network = initNetwork(isLocation, id);
			network.addClickListener(ClickEvent.Name.selectNode, getNodeSelectionListener());
			setFirstComponent(network);
		}
	}

	public void initTask(Task task) {
		setFirstComponent(null);
		taskName = task.getName();
		Object value = runtimeService.getVariable(task.getProcessInstanceId(), TRIAGE_REASON);
		if (value != null) {
			TriageSearchValue reason = TriageSearchValue.stringToObject((String) value);

			if (reason != null && reason.getNetworkState() != null) {
				network = new Network(reason.getNetworkState().nodes, reason.getNetworkState().edges);
				network.addClickListener(ClickEvent.Name.selectNode, getNodeSelectionListener());
				setFirstComponent(network);
			}
		}
	}


	private List<Entity> getPersonPrimaryLinks(String id) {

		List<String> ids = new ArrayList<String>();
		Person person = mashRep.findPersonById(id, ExplorerApp.get().getSecurityInfo(taskName));
		List<Entity> entities;

		if (person.getHomeAddress() != null && person.getHomeAddress().getLocation() != null)
			ids.add(person.getHomeAddress().getLocation().getId());

		if (person.getHousehold() != null && person.getHousehold().getRelations() != null) {
			for (Relation relation : person.getHousehold().getRelations()) {
				ids.add(relation.getPerson().getId());
			}
		}
		if (ids.size() > 0)
			entities = mashRep.findEntitiesById(ids, ExplorerApp.get().getSecurityInfo(taskName));
		else
			entities = new ArrayList<Entity>();
		entities.add(0, person);

		return entities;
	}

	private List<Entity> getLocationPrimaryLinks(String id) {

		Location location = mashRep.findLocationById(id, ExplorerApp.get().getSecurityInfo(taskName));
		List<Entity> entities;
		if (location.getOccupants() != null && location.getOccupants().size() > 0) {
			List<String> ids = new ArrayList<String>();

			for (Occupant person : location.getOccupants()) {
				ids.add(person.getPerson().getId());
			}
			entities = mashRep.findEntitiesById(ids, ExplorerApp.get().getSecurityInfo(taskName));
		} else {
			entities = new ArrayList<Entity>();
		}


		entities.add(0, location);
		return entities;

	}

	private Network initNetwork(boolean isLocation, String id) {

		NetworkState state = new NetworkState();
		state.edges = new HashSet<>();
		state.nodes = new HashSet<>();
		List<Entity> primaryLinks;
		if (isLocation) {
			primaryLinks = getLocationPrimaryLinks(id);
			builder.addNodesToNetwork(state, primaryLinks,true);

		} else {
			primaryLinks = getPersonPrimaryLinks(id);
			builder.addNodesToNetwork(state, primaryLinks,true);
		}
		populateDetail(primaryLinks.get(0));
		fireEvent(new NetworkChangeEvent(NetworkPanel.this, state,primaryLinks));

		return new Network(state.nodes, state.edges);

	}

	private void populateDetail(Entity entity)
	{
		 detailTabSheet.removeAllComponents();
		for (Source source : entity.getSources()) {
			detailTabSheet.addTab(createTabDetail(source),source.getAgency());
		}
	     setSecondComponent(detailTabSheet);

			setSplitPosition(175, Unit.PIXELS, true);
			setLocked(false);
	}

	private Component createTabDetail(Source source) {
		VerticalLayout tab =new VerticalLayout();
		tab.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		tab.setSizeFull();

		Table sourcesTable = new Table();
		sourcesTable.addStyleName(ExplorerLayout.STYLE_TASK_LIST);
		sourcesTable.addStyleName(ExplorerLayout.STYLE_SCROLLABLE);
		sourcesTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_EXPLICIT_DEFAULTS_ID);

	
		sourcesTable.addContainerProperty("System", String.class, null);
		sourcesTable.addContainerProperty("Id", String.class, null);

		for ( SystemId id : source.getSystemIds()) {
			sourcesTable.addItem(new Object[]{id.getSystem(), id.getSourceId()},String.format("%s|||%s", id.getSystem() , id.getSourceId()));
		}
		sourcesTable.setPageLength(source.getSystemIds().size());

		tab.addComponent(sourcesTable);
		sourcesTable.setHeight(100, Unit.PERCENTAGE);

		return tab;
	}

	public void addNetworkChangeEventListener(NetworkChangeListener listener) {
		addListener(listener);
	}

    public ClickListener getNodeSelectionListener()
    {
    	return new ClickListener() {

			@Override
			public void clicked(ClickEvent event) {
				if (event.selectedNodeIds.length > 0) {
					List<Entity> entities = expandNetwork(event);
					populateDetail(entities.get(0));
				}
			}

			private List<Entity> expandNetwork(ClickEvent event) {
				List<String> ids = new ArrayList<String>();
				ids.add(event.selectedNodeIds[0]);
				List<Entity> entities = mashRep.findEntitiesById(ids, ExplorerApp.get().getSecurityInfo(taskName));
				if (isNodeUnexplored(event.selectedNodeIds[0]))
				{
					NetworkState state = new NetworkState();
					Node expanded = builder.addNodesToNetwork(state, entities.get(0));
					network.updateNodes(expanded);
					network.add(state.nodes, state.edges);
					fireEvent(new NetworkChangeEvent(NetworkPanel.this, network.getState(),entities,true));

				}
				return entities;
			}

			private boolean isNodeUnexplored(String id) {
				for (Node node : network.getState().nodes) {
					if (node.id.equals(id))
					{
						return node.group.isUnexplored();
					}
				}
				return false;
			}
		};
    }
}

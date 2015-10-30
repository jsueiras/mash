package mash.graph;

import com.google.gson.Gson;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.JavaScriptFunction;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

import java.util.*;

@JavaScript({"js/network-connector.js"})
@StyleSheet({"css/network.css"})
public class Network extends AbstractJavaScriptComponent {
	private static final Gson GSON = new Gson();

	private Map<ClickEvent.Name, Set<ClickListener>> clickListeners =
			new HashMap<ClickEvent.Name, Set<ClickListener>>() {{
				Arrays.stream(ClickEvent.Name.values()).forEach(type -> put(type, new HashSet<ClickListener>()));
			}};

	public Network(Set<Node> nodes, Set<Edge> edges) {
		addStyleName("mash-network");
		setSizeFull();

		getState().nodes.addAll(nodes);
		getState().edges.addAll(edges);

		addFunction("server_onClick", new JavaScriptFunction() {
			@Override
			public void call(JsonArray arguments) {
				JsonObject json = arguments.getObject(0);
				ClickEvent.Name name = ClickEvent.Name.valueOf(json.getString("name"));
				for (ClickListener listener : clickListeners.get(name)) {

					JsonArray selectedNodeIds = json.getArray("selectedNodeIds");
					Set<String> selectedNodeIdSet = new HashSet<>();
					for (int i = 0; i < selectedNodeIds.length(); i++) {
						selectedNodeIdSet.add(selectedNodeIds.getString(i));
					}
					String[] selectedNodeIdArray = selectedNodeIdSet.toArray(new String[selectedNodeIdSet.size()]);

					JsonArray selectedEdgeIds = json.getArray("selectedEdgeIds");
					Set<String> selectedEdgeIdSet = new HashSet<>();
					for (int i = 0; i < selectedEdgeIds.length(); i++) {
						selectedEdgeIdSet.add(selectedEdgeIds.getString(i));
					}
					String[] selectedEdgeIdArray = selectedEdgeIdSet.toArray(new String[selectedEdgeIdSet.size()]);

					String atNodeId = json.hasKey(ClickEvent.AT_NODE_ID) ? json.getString(ClickEvent.AT_NODE_ID) :
							null;
					String atEdgeId = json.hasKey(ClickEvent.AT_EDGE_ID) ? json.getString(ClickEvent.AT_EDGE_ID) :
							null;
					ClickEvent event =
							new ClickEvent(name, atNodeId, atEdgeId, selectedNodeIdArray, selectedEdgeIdArray);
					listener.clicked(event);
				}
			}
		});
	}

	public void add(Set<Node> nodes, Set<Edge> edges) {
		NetworkState state = getState();

		Set<Node> nodesToAdd = new HashSet<>();
		for (Node node : nodes) {
			if (state.nodes.add(node)) {
				nodesToAdd.add(node);
			}
		}

		Set<Edge> edgesToAdd = new HashSet<>();
		for (Edge edge : edges) {
			if (state.edges.add(edge)) {
				edgesToAdd.add(edge);
			}
		}

		callFunction("add", GSON.toJson(nodesToAdd), GSON.toJson(edgesToAdd));
	}

	public void selectNodes(String... nodeIds) {
		callFunction("selectNodes", nodeIds);
	}

	public void updateNodes(Node... nodes) {
		NetworkState state = getState();

		Set<Node> nodesToUpdate = new HashSet<>();
		for (Node node : nodes) {
			if (state.nodes.contains(node)) {
				state.nodes.remove(node);
				state.nodes.add(node);
				nodesToUpdate.add(node);
			}
		}

		callFunction("updateNodes", GSON.toJson(nodesToUpdate));
	}

	@Override
	protected NetworkState getState() {
		NetworkState state = (NetworkState) super.getState();
		return state;
	}

	public void addClickListener(ClickEvent.Name name, ClickListener listener) {
		clickListeners.get(name).add(listener);
	}

	public boolean removeClickListener(ClickEvent.Name name, ClickListener listener) {
		return clickListeners.get(name).remove(listener);
	}

	public static interface ClickListener {
		public void clicked(ClickEvent event);
	}

	public static class ClickEvent {
		private static final String AT_NODE_ID = "atNodeId";
		private static final String AT_EDGE_ID = "atEdgeId";

		public enum Name {
			click, doubleClick, oncontext, hold, release,
			select, selectNode, selectEdge,
			dragStart, dragging, dragEnd;
		}

		public final Name name;
		public final String atNodeId;
		public final String atEdgeId;
		public final String[] selectedNodeIds;
		public final String[] selectedEdgeIds;

		public ClickEvent(Name name, String atNodeId, String atEdgeId, String[] selectedNodeIds,
						  String[] selectedEdgeIds) {
			this.name = name;
			this.atNodeId = atNodeId;
			this.atEdgeId = atEdgeId;
			this.selectedNodeIds = selectedNodeIds;
			this.selectedEdgeIds = selectedEdgeIds;
		}
	}
}

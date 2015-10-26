package mash.graph;

import com.mash.data.service.Repository;
import com.mash.model.catalog.Entity;
import com.mash.model.catalog.Location;
import com.mash.model.catalog.Occupant;
import com.mash.model.catalog.Person;
import com.mash.model.catalog.Relation;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.AbstractJavaScriptComponent;

import com.vaadin.ui.JavaScriptFunction;
import elemental.json.JsonArray;
import elemental.json.JsonException;
import elemental.json.JsonValue;
import mash.graph.util.NetworkBuilder;

import org.activiti.explorer.ExplorerApp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@JavaScript({"js/network-connector.js"})
@StyleSheet({"css/network.css"})
public class Network extends AbstractJavaScriptComponent {
	private Set<NodeSelectionListener> nodeSelectionListeners = new HashSet<NodeSelectionListener>();

	public Network(NetworkState state) {
		addStyleName("mash-network");
		setSizeFull();

		initNetwork(state);

		addFunction("onSelectNode", new JavaScriptFunction() {
			@Override
			public void call(JsonArray arguments) {
				for (NodeSelectionListener listener : nodeSelectionListeners) {
					listener.nodeSelected(
							new NodeSelectedEvent(arguments.getObject(0).getArray("nodes").get(0).asString()));
				}
			}
		});
	}

	private void initNetwork(NetworkState newState) {
		getState().nodes = newState.nodes;
		getState().edges = newState.edges;
	}


	@Override
	protected NetworkState getState() {
		NetworkState state = (NetworkState) super.getState();
		return state;
	}

	public static interface NodeSelectionListener {
		public void nodeSelected(NodeSelectedEvent event);
	}

	public static class NodeSelectedEvent {
		private final String nodeId;

		public NodeSelectedEvent(String nodeId) {
			this.nodeId = nodeId;
		}
	}
}

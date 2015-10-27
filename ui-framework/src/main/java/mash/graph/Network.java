package mash.graph;

import com.google.gson.Gson;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.server.JsonCodec;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.JavaScriptFunction;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonValue;

import java.util.HashSet;
import java.util.Set;

@JavaScript({"js/network-connector.js"})
@StyleSheet({"css/network.css"})
public class Network extends AbstractJavaScriptComponent {
	private static final Gson GSON = new Gson();

	private Set<NodeSelectionListener> nodeSelectionListeners = new HashSet<NodeSelectionListener>();

	public Network(Set<Node> nodes, Set<Edge> edges) {
		addStyleName("mash-network");
		setSizeFull();

		getState().nodes.addAll(nodes);
		getState().edges.addAll(edges);

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

	public void add(Set<Node> nodes, Set<Edge> edges) {
		NetworkState state = getState();
		state.nodes.addAll(nodes);
		state.edges.addAll(edges);
		callFunction("add", GSON.toJson(nodes), GSON.toJson(edges));
	}

	public void selectNodes(String... nodeIds) {
		callFunction("selectNodes", nodeIds);
	}

	@Override
	protected NetworkState getState() {
		NetworkState state = (NetworkState) super.getState();
		return state;
	}

	public void addNodeSelectionListener(NodeSelectionListener listener) {
		nodeSelectionListeners.add(listener);
	}

	public boolean removeNodeSelectionListener(NodeSelectionListener listener) {
		return nodeSelectionListeners.remove(listener);
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

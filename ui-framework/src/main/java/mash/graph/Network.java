package mash.graph;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.JavaScriptFunction;
import elemental.json.JsonArray;

import java.util.HashSet;
import java.util.Set;

@JavaScript({"js/network-connector.js"})
@StyleSheet({"css/network.css"})
public class Network extends AbstractJavaScriptComponent {
	private Set<NodeSelectionListener> nodeSelectionListeners = new HashSet<NodeSelectionListener>();

	public Network(NetworkState state) {
		addStyleName("mash-network");
		setSizeFull();

		getState().nodes = state.nodes;
		getState().edges = state.edges;

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

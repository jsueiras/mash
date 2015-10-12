package mash.graph;

import com.mash.model.catalog.Location;
import com.mash.model.catalog.Person;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.shared.ui.JavaScriptComponentState;
import com.vaadin.ui.AbstractJavaScriptComponent;
import org.activiti.explorer.ExplorerApp;

import java.util.HashSet;

@JavaScript({"webjars/visjs/4.8.2/vis.min.js", "js/network-connector.js"})
@StyleSheet({"webjars/visjs/4.8.2/vis.min.css", "css/network.css"})
public class Network extends AbstractJavaScriptComponent {

	public Network(boolean isLocation, String id) {
		addStyleName("mash-network");
		setSizeFull();

		if (id == null) {
			// clear the graph
		} else {
			if (isLocation) {
				Location location = ExplorerApp.get().getMashRepository().findLocationById(id);

			  NetworkState state = getState();
			  state.nodes = new HashSet<Node>();

			  Node node = new Node(location.getId());
			  node.label = String.format("%s %s, %s", location.getNumberOrName(), location.getStreet(), location.getCity());
			  state.nodes.add(node);
			} else {
				Person person = ExplorerApp.get().getMashRepository().findPersonById(id);

				NetworkState state = getState();
				state.nodes = new HashSet<Node>();

				Node node = new Node(person.getId());
				node.label = String.format("%s %s", person.getFirstName(), person.getLastName());
				state.nodes.add(node);
			}
		}
	}

	@Override
	protected NetworkState getState() {
		return (NetworkState) super.getState();
	}
}

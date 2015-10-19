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

import mash.graph.util.NetworkBuilder;

import org.activiti.explorer.ExplorerApp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@JavaScript({"js/network-connector.js"})
@StyleSheet({"css/network.css"})
public class Network extends AbstractJavaScriptComponent {

	public Network(NetworkState state) {
		addStyleName("mash-network");
		setSizeFull();

		initNetwork(state);
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
}

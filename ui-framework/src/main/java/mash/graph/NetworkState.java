package mash.graph;

import com.vaadin.shared.ui.JavaScriptComponentState;

import java.util.HashSet;
import java.util.Set;

public class NetworkState extends JavaScriptComponentState {
	public Set<Node> nodes = new HashSet<>();
	public Set<Edge> edges = new HashSet<>();
}

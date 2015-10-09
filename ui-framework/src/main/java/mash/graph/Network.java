package mash.graph;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.AbstractJavaScriptComponent;

import java.util.ResourceBundle;

@JavaScript({"webjars/visjs/4.8.2/vis.min.js", "js/network-connector.js"})
@StyleSheet({"webjars/visjs/4.8.2/vis.min.css", "css/network.css"})
public class Network extends AbstractJavaScriptComponent {

	public Network() {
		addStyleName("mash-network");
		setSizeFull();
	}
}

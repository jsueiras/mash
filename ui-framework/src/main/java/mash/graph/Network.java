package mash.graph;

import com.mash.model.catalog.Location;
import com.mash.model.catalog.Person;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.AbstractJavaScriptComponent;

import java.util.ResourceBundle;

import org.activiti.explorer.ExplorerApp;

@JavaScript({"webjars/visjs/4.8.2/vis.min.js", "js/network-connector.js"})
@StyleSheet({"webjars/visjs/4.8.2/vis.min.css", "css/network.css"})
public class Network extends AbstractJavaScriptComponent {

	public Network() {
		addStyleName("mash-network");
		setSizeFull();
	}
	public void changeEntity(boolean isLocation, String id)
	{
		if (id ==null) 
		{
			// clear the graph
		}	
		else
		{		
		if (isLocation)
		{
			Location location = ExplorerApp.get().getMashRepository().findLocationById(id);
			//createContent(location);
		}	
		else
		{
			Person person = ExplorerApp.get().getMashRepository().findPersonById(id);
			//createContent(person);
	
		}
		}
	}
	
}

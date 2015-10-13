package mash.graph;

import com.vaadin.ui.CssLayout;

public class NetworkPanel extends CssLayout {
	
	public void setRootEntity(boolean isLocation,String id)
	{
		  removeAllComponents();
	      addComponent(new Network(isLocation, id));

	}

}

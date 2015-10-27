package mash.graph;

import java.util.List;
import java.util.Map;

import com.mash.data.service.Query;
import com.mash.model.catalog.Entity;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Event;

public class NetworkChangeEvent extends Event {

  
	private NetworkState newState;
	private List<Entity> primaryLinks;
	
   
    
    
    public NetworkChangeEvent(Component source, NetworkState newState, List<Entity> primaryLinks) {
      super(source);
      this.newState = newState;
      this.primaryLinks = primaryLinks;
    }
    
  
    public NetworkState getNewState() {
		return newState;
	}
    
    public List<Entity> getPrimaryLinks() {
		return primaryLinks;
	}
    
    
    
  }
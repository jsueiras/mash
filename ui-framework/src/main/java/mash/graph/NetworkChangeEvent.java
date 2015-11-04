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
	private boolean append;
	
	
	 public NetworkChangeEvent(Component source, NetworkState newState, List<Entity> primaryLinks)
	 {
		 this(source,newState,primaryLinks,false);
	 }
   
    
    
    public NetworkChangeEvent(Component source, NetworkState newState, List<Entity> primaryLinks,boolean append) {
      super(source);
      this.newState = newState;
      this.primaryLinks = primaryLinks;
      this.append = append;
    }
    
  
    public NetworkState getNewState() {
		return newState;
	}
    
    public List<Entity> getPrimaryLinks() {
		return primaryLinks;
	}
    
    public boolean isAppend() {
		return append;
	}
   
    
    
    
  }
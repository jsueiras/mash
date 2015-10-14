package mash.graph;

import java.util.Map;

import com.mash.data.service.Query;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Event;

public class NetworkChangeEvent extends Event {

  
	private NetworkState newState;
   
    
    
    public NetworkChangeEvent(Component source, NetworkState newState) {
      super(source);
      this.newState = newState;
    }
    
  
    public NetworkState getNewState() {
		return newState;
	}
    
  }
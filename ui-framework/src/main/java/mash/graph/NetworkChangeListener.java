package mash.graph;

import com.vaadin.ui.Component.Event;
import com.vaadin.ui.Component.Listener;

public abstract class  NetworkChangeListener implements Listener {

	 
	  public final void componentEvent(Event event) {
	    if(event instanceof NetworkChangeEvent) {
	       handleNetworkChange((NetworkChangeEvent)event);
	    }
	  }

	protected abstract void handleNetworkChange(NetworkChangeEvent event) ;
}  
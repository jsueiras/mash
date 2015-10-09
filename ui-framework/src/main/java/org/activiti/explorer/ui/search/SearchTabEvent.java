package org.activiti.explorer.ui.search;

import java.util.Map;

import com.mash.data.service.Query;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Event;

public class SearchTabEvent extends Event {

    
    public static final String TYPE_CLEAR = "CLEAR";
    public static final String TYPE_SELECT = "SELECT";
    
    private String type;
 	private boolean isLocation;
	private String entityId;
    
    
    public SearchTabEvent(Component source, String type) {
      super(source);
      this.type = type;
    }
    
   
    
    public SearchTabEvent(Component source ,
			String type, boolean isLocation, String id) {
		this(source,type);
		this.isLocation = isLocation;
		this.entityId = id;
	}

	public String getType() {
      return type;
    }
    
  
 
    public boolean isLocation() {
		return isLocation;
	}
    
    public String getEntityId() {
		return entityId;
	}
    
    
  }
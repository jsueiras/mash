package org.activiti.explorer.ui.search;

import java.util.Map;

import com.mash.data.service.Query;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Event;

public class SearchFormEvent extends Event {

    
    public static final String TYPE_SUBMIT = "SUBMIT";
    public static final String TYPE_CANCEL = "CANCEL";
    
    private String type;
    private Query query;
	  
    
    public SearchFormEvent(Component source, String type) {
      super(source);
      this.type = type;
    }
    
    public SearchFormEvent(Component source, String type, Query query) {
      this(source, type);
      this.query = query;
    }
    
   

	public String getType() {
      return type;
    }
    
  
    public Query getQuery() {
		return query;
	}
    
 
    
    
  }
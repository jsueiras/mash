package org.activiti.explorer.ui.task;

import java.util.List;

import org.activiti.engine.ProcessEngines;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.ui.mainlayout.ExplorerLayout;
import org.activiti.explorer.ui.search.SearchFormEvent;
import org.activiti.explorer.ui.search.SearchFormEventListener;
import org.activiti.explorer.ui.search.SearchDetailPanel.SearchRequestEventListener;
import org.activiti.explorer.ui.search.person.Decorator;
import org.activiti.explorer.ui.task.TaskEventTextResolver;

import com.mash.data.service.Query;
import com.mash.model.catalog.Entity;
import com.mash.model.catalog.Location;
import com.mash.model.catalog.Person;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.graph.GraphExplorer;
import com.vaadin.graph.layout.JungCircleLayoutEngine;
import com.vaadin.graph.layout.JungFRLayoutEngine;
import com.vaadin.graph.layout.JungISOMLayoutEngine;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class TaskGraphPanel extends Panel{
	
	  
	  private VerticalLayout treeContainer;

	  

	public TaskGraphPanel() {
		       
		    
		    setHeight(100, UNITS_PERCENTAGE);
		    treeContainer = new VerticalLayout();
		    addStyleName(ExplorerLayout.STYLE_TASK_EVENT_PANEL);
		    setContent(treeContainer);
		    
	}
	  
	
	

		public void createContent(Entity entity) {
		       
	          treeContainer.removeAllComponents();
	          if (entity!=null)
	          treeContainer.addComponent(Decorator.getTreeComponent(entity));
	        
		}
	    

		 
		public void changeEntity(boolean isLocation, String id)
		{
			if (id ==null) 
			{
				treeContainer.removeAllComponents();
			}	
			else
			{		
			if (isLocation)
			{
				Location location = ExplorerApp.get().getMashRepository().findLocationById(id);
				createContent(location);
			}	
			else
			{
				Person person = ExplorerApp.get().getMashRepository().findPersonById(id);
				createContent(person);
		
			}
			}
		}

}

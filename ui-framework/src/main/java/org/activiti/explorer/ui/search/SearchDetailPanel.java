/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.explorer.ui.search;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.I18nManager;
import org.activiti.explorer.data.LazyLoadingContainer;
import org.activiti.explorer.data.LazyLoadingQuery;
import org.activiti.explorer.ui.Images;
import org.activiti.explorer.ui.custom.DetailPanel;
import org.activiti.explorer.ui.mainlayout.ExplorerLayout;
import org.activiti.explorer.ui.search.SearchFormEvent;
import org.activiti.explorer.ui.search.person.Decorator;
import org.activiti.explorer.ui.util.ThemeImageColumnGenerator;

import com.mash.data.service.Query;
import com.mash.data.service.Repository;
import com.mash.model.catalog.Entity;
import com.mash.model.catalog.Location;
import com.mash.model.catalog.Person;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;


/**
 * @author Joram Barrez
 */
public class SearchDetailPanel extends DetailPanel {

  private static final long serialVersionUID = 1L;
  
  protected ProcessDefinition processDefinition;
  protected I18nManager i18nManager;
  
  protected VerticalLayout detailPanelLayout;
  protected VerticalLayout detailContainer;
  protected VerticalLayout processDefinitionStartForm;
  
  protected Map<String, String> savedFormProperties;
  
  private LazyLoadingQuery lazyLoadingQuery;
  private LazyLoadingContainer taskListContainer;
  private Repository repository;
  private Table personTable;
  private Table locationTable;
  private boolean isLocation;

private HorizontalLayout resultsContainer;
private VerticalLayout treeContainer;
private SearchTabEventListener tabEventListener;
  
  public SearchDetailPanel(SearchTabEventListener tabListener) {
    this.i18nManager = ExplorerApp.get().getI18nManager();
    this.repository = ExplorerApp.get().getMashRepository();
    initUi(tabListener);
  }

 

protected void initUi(SearchTabEventListener tabListener) {
	  
	addListener(tabListener);  
    setSizeFull();
    addStyleName(Reindeer.LAYOUT_WHITE);
    detailPanelLayout = new VerticalLayout();
    detailPanelLayout.setWidth(100, UNITS_PERCENTAGE);
    detailPanelLayout.setMargin(true);
    setDetailContainer(detailPanelLayout);
   
   
    detailContainer = new VerticalLayout();
    detailContainer.setMargin(true);
    detailContainer.addStyleName(Reindeer.PANEL_LIGHT);
    detailPanelLayout.addComponent(detailContainer);
    detailContainer.setSizeFull();
     
   
    initForm();
    initResults();
  }
  
  private void initResults() {
	resultsContainer = new HorizontalLayout();
	resultsContainer.setSpacing(true);
    personTable = createPersonTable();
    locationTable=createLocationTable();
    locationTable.addValueChangeListener(getListSelectionListener());
    personTable.addValueChangeListener(getListSelectionListener());
    resultsContainer.addComponent(locationTable);
	resultsContainer.addComponent(personTable);
	resultsContainer.setVisible(false);
	detailContainer.addComponent(resultsContainer);
	 treeContainer = new VerticalLayout();
	detailContainer.addComponent( treeContainer);
	 
	
  }

 protected void initForm() {
     SearchForm search = new SearchForm();
     search.addListener(new SearchRequestEventListener());
  
	detailContainer.addComponent(search);
  }
  
  
  public class SearchRequestEventListener extends SearchFormEventListener
  {


	@Override
	protected void handleFormSubmit(SearchFormEvent event) {
		Query query = event.getQuery();
		if (!isLocationQuery(query))
		{		
			locationTable.setVisible(false);		
		  List<Person> results;
		
		try {
			results = repository.findPersons(query);
			  appendResults(results,personTable);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
    	  fireEvent(new SearchTabEvent(SearchDetailPanel.this, SearchTabEvent.TYPE_CLEAR,isLocation,null ));
  		
	      
		}
		else
		{		
		  personTable.setVisible(false);	
		  List<Location> results;
		try {
			results = repository.findLocations(query.getSampleLocation());
			 appendResults(results,locationTable);	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		}
	}

	

	private boolean isLocationQuery(Query query) {
		isLocation= (query.getLastName() == null || query.getLastName().length() == 0)
				&& (query.getFirstName() ==null || query.getFirstName().length() == 0)
				&& query.getDateOfBirthFrom() ==null
				&& query.getDateOfBirthTo()==null
				&& query.getGender()==null;
		return isLocation;
		
	}

	@Override
	protected void handleFormCancel(SearchFormEvent event) {
		ExplorerApp.get().showNotification("Cancel", "Cancel");
	
	}




	
	  
  }
  
  
  protected Table createPersonTable() {
	    Table personTable = new Table();
	    personTable.setPageLength(10);
	    personTable.addStyleName(ExplorerLayout.STYLE_TASK_LIST);
	    personTable.addStyleName(ExplorerLayout.STYLE_SCROLLABLE);
	    personTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_EXPLICIT_DEFAULTS_ID);
		 
	   
	    //this.lazyLoadingQuery = createLazyLoadingQuery();
	    //this.taskListContainer = new LazyLoadingContainer(lazyLoadingQuery, 30);
	    //taskTable.setContainerDataSource(taskListContainer);
	    
	    // Create column header
	    personTable.addGeneratedColumn("", new ThemeImageColumnGenerator(Images.TASK_22));
	    personTable.setColumnWidth("", 22);
	    
	    personTable.addContainerProperty("Name", String.class, null);
	    personTable.setColumnWidth("Name", 250);
	   
	    personTable.addContainerProperty("Address", String.class, null);
	    personTable.setColumnWidth("Address", 250);
	    
	    personTable.addContainerProperty("DOB", String.class, null);
	    personTable.setColumnWidth("DOB", 50);
		  
	   
	    return personTable;
	  }
  
  protected Table createLocationTable() {
	    Table locationTable = new Table();
	    locationTable.setPageLength(10);
	    locationTable.addStyleName(ExplorerLayout.STYLE_TASK_LIST);
	    locationTable.addStyleName(ExplorerLayout.STYLE_SCROLLABLE);
	    locationTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_EXPLICIT_DEFAULTS_ID);
		     
	    // Create column header
	    locationTable.addGeneratedColumn("", new ThemeImageColumnGenerator(Images.TASK_22));
	    locationTable.setColumnWidth("", 22);
	    
	   
	    locationTable.addContainerProperty("Address", String.class, null);
	    locationTable.setColumnWidth("Address", 250);
	    
	    locationTable.addContainerProperty("Postcode", String.class, null);
	    locationTable.setColumnWidth("Postcode", 80);
	
		   
	    return locationTable;
	  }

	private void appendResults(List<? extends Entity> results, Table table) {
	
    for (Entity entity : results) {
    	table.addItem(Decorator.getTableRow(entity), entity.getId());
	}
    resultsContainer.setVisible(true);
    table.setVisible(true);
 	
}

	private LazyLoadingQuery createLazyLoadingQuery() {
		// TODO Auto-generated method stub
		return null;
	}

  

	 protected ValueChangeListener getListSelectionListener() {
		    return new Property.ValueChangeListener() {
		      private static final long serialVersionUID = 1L;
		      public void valueChange(ValueChangeEvent event) {
		    	  String id = (String) event.getProperty().getValue();   
		    	  
		    	  fireEvent(new SearchTabEvent(SearchDetailPanel.this, SearchTabEvent.TYPE_SELECT,isLocation,id ));
		    	     
		    
		      }
		    };
		  }
	
	
	

}

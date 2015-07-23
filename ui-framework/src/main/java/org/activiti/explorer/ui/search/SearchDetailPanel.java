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

import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.I18nManager;
import org.activiti.explorer.Messages;
import org.activiti.explorer.data.LazyLoadingContainer;
import org.activiti.explorer.data.LazyLoadingQuery;
import org.activiti.explorer.ui.AbstractPage;
import org.activiti.explorer.ui.Images;
import org.activiti.explorer.ui.custom.DetailPanel;
import org.activiti.explorer.ui.form.FormPropertiesEventListener;
import org.activiti.explorer.ui.form.FormPropertiesForm;
import org.activiti.explorer.ui.form.FormPropertiesForm.FormPropertiesEvent;
import org.activiti.explorer.ui.mainlayout.ExplorerLayout;
import org.activiti.explorer.ui.search.SearchForm.SearchFormEvent;
import org.activiti.explorer.ui.util.ThemeImageColumnGenerator;

import com.mash.data.service.Repository;
import com.mash.model.catalog.Person;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;


/**
 * @author Joram Barrez
 */
public class SearchDetailPanel extends DetailPanel {

  private static final long serialVersionUID = 1L;
  
  protected ProcessDefinition processDefinition;
  protected AbstractPage parentPage;
  protected I18nManager i18nManager;
  
  protected VerticalLayout detailPanelLayout;
  protected HorizontalLayout detailContainer;
  protected VerticalLayout processDefinitionStartForm;
  
  protected Map<String, String> savedFormProperties;
  
  private LazyLoadingQuery lazyLoadingQuery;
  private LazyLoadingContainer taskListContainer;
  private Repository repository;
  private Table table;

  
  public SearchDetailPanel( AbstractPage parentPage) {
    this.parentPage = parentPage;
    this.i18nManager = ExplorerApp.get().getI18nManager();
    this.repository = ExplorerApp.get().getMashRepository();
  
    initUi();
  }

  protected void initUi() {
	  
	addListener(new SearchRequestEventListener());  
    setSizeFull();
    addStyleName(Reindeer.LAYOUT_WHITE);
    detailPanelLayout = new VerticalLayout();
    detailPanelLayout.setWidth(100, UNITS_PERCENTAGE);
    detailPanelLayout.setMargin(true);
    setDetailContainer(detailPanelLayout);
   
   
    detailContainer = new HorizontalLayout();
    detailContainer.addStyleName(Reindeer.PANEL_LIGHT);
    detailPanelLayout.addComponent(detailContainer);
    detailContainer.setSizeFull();
    
    initForm();
    initTable();
  }
  

  
 
  
  private void initTable() {
    table = createList();
	table.setVisible(false);
	detailContainer.addComponent(table);
	
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
		 List<Person> results=repository.findPersonsByName(event.getFormProperties().get("name"));
	     appendResults(results);		
	}

	@Override
	protected void handleFormCancel(SearchFormEvent event) {
		ExplorerApp.get().showNotification("Cancel", "Cancel");
	
	}
	
	
	
	  
  }
  
  
  protected Table createList() {
	    Table taskTable = new Table();
	    taskTable.addStyleName(ExplorerLayout.STYLE_TASK_LIST);
	    taskTable.addStyleName(ExplorerLayout.STYLE_SCROLLABLE);
	    
	   
	    //this.lazyLoadingQuery = createLazyLoadingQuery();
	    //this.taskListContainer = new LazyLoadingContainer(lazyLoadingQuery, 30);
	    //taskTable.setContainerDataSource(taskListContainer);
	    
	    // Create column header
	    taskTable.addGeneratedColumn("icon", new ThemeImageColumnGenerator(Images.TASK_22));
	    taskTable.setColumnWidth("icon", 22);
	    
	    taskTable.addContainerProperty("name", String.class, null);
	    taskTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
	    
	   
	    return taskTable;
	  }

	private void appendResults(List<Person> results) {
	
    for (Person person : results) {
    	table.addItem(new Object[] {person.getFirstName()}, "name");
	}
    table.setVisible(true);
	
}

	private LazyLoadingQuery createLazyLoadingQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	private ItemClickListener getListSelectionListener() {
		// TODO Auto-generated method stub
		return null;
	}

}

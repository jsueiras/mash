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

import java.util.HashMap;
import java.util.Map;

import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.data.LazyLoadingContainer;
import org.activiti.explorer.data.LazyLoadingQuery;
import org.activiti.explorer.navigation.ReportNavigator;
import org.activiti.explorer.navigation.UriFragment;
import org.activiti.explorer.ui.AbstractTablePage;
import org.activiti.explorer.ui.Images;
import org.activiti.explorer.ui.custom.ToolBar;
import org.activiti.explorer.ui.reports.ReportDetailPanel;
import org.activiti.explorer.ui.reports.ReportListQuery;
import org.activiti.explorer.ui.reports.ReportsMenuBar;
import org.activiti.explorer.ui.util.ThemeImageColumnGenerator;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;


/**
 * @author Joram Barrez
 */
public class RunSearchesPage extends AbstractTablePage {

  private static final long serialVersionUID = -5259331126409002997L;
  
  protected String reportId;
  
  protected Map<String,Component> searchPages;
  
  protected Table reportTable;
  
 
  public RunSearchesPage() {
     searchPages = new HashMap<String,Component>();
    
	searchPages.put("People", new SearchDetailPanel(this));
  }
  
  protected Table createList() {
    reportTable = new Table();
    
    // Column headers
    reportTable.addGeneratedColumn("icon", new ThemeImageColumnGenerator(Images.REPORT_22));
    reportTable.setColumnWidth("icon", 22);
    
    reportTable.addContainerProperty("name", String.class, null);
    reportTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
   
    for (String searchName : searchPages.keySet()) {
	
		reportTable.addItem(new Object[]{searchName},searchName);
	}
    
    // Listener to change right panel when clicked on a report
    reportTable.addValueChangeListener(new Property.ValueChangeListener() {
      
      private static final long serialVersionUID = 1L;
      
      public void valueChange(ValueChangeEvent event) {
        Item item = reportTable.getItem(event.getProperty().getValue()); // the value of the property is the itemId of the table entry
        if (item != null) {
          String searchName = item.toString();
          setDetailComponent(searchPages.get(searchName));
          
            
        } else {
          // Nothing selected
          setDetailComponent(null);
        }
      }
      
    });
    
    return reportTable;
  }

  protected ToolBar createMenuBar() {
    return new ReportsMenuBar();
  }
  

  
}

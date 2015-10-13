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

package org.activiti.explorer.ui.task;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Table;

import mash.graph.Network;
import mash.graph.NetworkPanel;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.task.Task;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.data.LazyLoadingContainer;
import org.activiti.explorer.data.LazyLoadingQuery;
import org.activiti.explorer.navigation.UriFragment;
import org.activiti.explorer.ui.AbstractTablePage;
import org.activiti.explorer.ui.Images;
import org.activiti.explorer.ui.custom.TaskListHeader;
import org.activiti.explorer.ui.custom.ToolBar;
import org.activiti.explorer.ui.mainlayout.ExplorerLayout;
import org.activiti.explorer.ui.search.SearchTabEvent;
import org.activiti.explorer.ui.search.SearchTabEventListener;
import org.activiti.explorer.ui.util.ThemeImageColumnGenerator;


/**
 * Abstract super class for all task pages (inbox, queued, archived, etc.),
 * Builds up the default UI: task list on the left, central panel and events on the right.
 *
 * @author Joram Barrez
 */
public abstract class TaskPage extends AbstractTablePage {

  private static final long serialVersionUID = 1L;

  protected transient TaskService taskService;

  protected String taskId;
  protected Table taskTable;
  protected LazyLoadingContainer taskListContainer;
  protected LazyLoadingQuery lazyLoadingQuery;
   protected NetworkPanel taskEventPanel;




  public TaskPage() {

    taskService =  ProcessEngines.getDefaultProcessEngine().getTaskService();
  }

  public TaskPage( String taskId) {
    this();
    this.taskId = taskId;
  }

  @Override
  protected void initUi() {
    super.initUi();
    if (taskId == null) {
      selectElement(0);
    } else {
      int index = taskListContainer.getIndexForObjectId(taskId);
      selectElement(index);
    }


  }

  @Override
  protected ToolBar createMenuBar() {
    return new TaskMenuBar(getSearchListener());
  }

  protected SearchTabEventListener getSearchListener() {
	return new SearchRequestEventListener();
}

  @Override
  protected Table createList() {
    taskTable = new Table();
    taskTable.addStyleName(ExplorerLayout.STYLE_TASK_LIST);
    taskTable.addStyleName(ExplorerLayout.STYLE_SCROLLABLE);

    // Listener to change right panel when clicked on a task
    taskTable.addListener(getListSelectionListener());

    this.lazyLoadingQuery = createLazyLoadingQuery();
    this.taskListContainer = new LazyLoadingContainer(lazyLoadingQuery, 30);
    taskTable.setContainerDataSource(taskListContainer);

    // Create column header
    taskTable.addGeneratedColumn("icon", new ThemeImageColumnGenerator(Images.TASK_22));
    taskTable.setColumnWidth("icon", 22);

    taskTable.addContainerProperty("name", String.class, null);
    taskTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);


    return taskTable;
  }

  protected ValueChangeListener getListSelectionListener() {
    return new Property.ValueChangeListener() {
      private static final long serialVersionUID = 1L;
      public void valueChange(ValueChangeEvent event) {
        Item item = taskTable.getItem(event.getProperty().getValue()); // the value of the property is the itemId of the table entry

        if(item != null) {
          String id = (String) item.getItemProperty("id").getValue();
          setDetailComponent(createDetailComponent(id));

        } else {
          // Nothing is selected
          setDetailComponent(null);
        }
      }
    };
  }


  public class SearchRequestEventListener extends SearchTabEventListener {
    @Override
    protected void handleFormSelect(SearchTabEvent event) {
      taskEventPanel.setRootEntity(event.isLocation(), event.getEntityId());
    }

    @Override
    protected void handleFormClear(SearchTabEvent event) {
      taskEventPanel.removeAllComponents();
     }
  }

  protected Component createDetailComponent(String id) {
	  Authentication.setAuthenticatedUserId(ExplorerApp.get().getLoggedInUser().getId());
    Task task = taskService.createTaskQuery().taskId(id).singleResult();
    Component detailComponent = new TaskDetailPanel(task, TaskPage.this);
    return detailComponent;
  }

  @Override
  protected Component getEventComponent() {
    return getTaskEventPanel();
  }

 public NetworkPanel getTaskEventPanel() {
    if(taskEventPanel == null) {
      taskEventPanel = new NetworkPanel();
      taskEventPanel.setSizeFull();
    }
    return taskEventPanel;
  }

  @Override
  public Component getSearchComponent() {
    return new TaskListHeader();
  }

  @Override
  public void refreshSelectNext() {

    // Selects new element in the table
    super.refreshSelectNext();

    // Update the counts in the header
    addMenuBar();
  }

  protected abstract LazyLoadingQuery createLazyLoadingQuery();

  protected abstract UriFragment getUriFragment(String taskId);

}

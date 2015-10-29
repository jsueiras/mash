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

import java.util.List;

import com.vaadin.server.FontAwesome;

import com.vaadin.server.Resource;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.I18nManager;
import org.activiti.explorer.Messages;
import org.activiti.explorer.ViewManager;
import org.activiti.explorer.identity.LoggedInUser;
import org.activiti.explorer.ui.Images;
import org.activiti.explorer.ui.custom.ToolBar;
import org.activiti.explorer.ui.custom.ToolbarEntry;
import org.activiti.explorer.ui.custom.ToolbarEntry.ToolbarCommand;
import org.activiti.explorer.ui.custom.ToolbarPopupEntry;
import org.activiti.explorer.ui.process.ProcessDefinitionPage;
import org.activiti.explorer.ui.process.listener.StartProcessInstanceClickListener;
import org.activiti.explorer.ui.search.SearchFormEventListener;
import org.activiti.explorer.ui.search.SearchPopupWindow;
import org.activiti.explorer.ui.search.SearchTabEventListener;
import org.activiti.explorer.ui.task.data.ArchivedListQuery;
import org.activiti.explorer.ui.task.data.InboxListQuery;
import org.activiti.explorer.ui.task.data.InvolvedListQuery;
import org.activiti.explorer.ui.task.data.QueuedListQuery;
import org.activiti.explorer.ui.task.data.TasksListQuery;
import org.activiti.explorer.ui.task.data.UnassignedListQuery;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import org.apache.batik.svggen.font.Font;

/**
 * The menu bar which is shown when 'Tasks' is selected in the main menu.
 *
 * @author Joram Barrez
 * @author Frederik Heremans
 */
public class TaskMenuBar extends ToolBar {

  private static final long serialVersionUID = 1L;

  public static final String ENTRY_TASKS = "tasks";
  public static final String ENTRY_INBOX = "inbox";
  public static final String ENTRY_UNASSIGNED = "unassigned";
  public static final String ENTRY_NEW = "new";


  public static final String ENTRY_QUEUED = "queued";
  public static final String ENTRY_INVOLVED = "involved";
  public static final String ENTRY_ARCHIVED = "archived";

  protected transient IdentityService identityService;
  protected ViewManager viewManager;
  protected I18nManager i18nManager;

  private SearchTabEventListener searchListener;

private TaskPage taskPage;

private RepositoryService repositoryService;

private ProcessDefinition triageDefinition;

private ProcessDefinition mashDefinition;




  public TaskMenuBar( TaskPage taskPage) {
    this.identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
    this.repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
    this.viewManager = ExplorerApp.get().getViewManager();
    this.i18nManager = ExplorerApp.get().getI18nManager();
    this.searchListener = taskPage.getSearchListener();
    this.taskPage = taskPage;
    this.addListener(searchListener);
    initProcessesDefinition();
    initItems();
    initActions();
  }

  protected void initItems() {
    setWidth("100%");
    LoggedInUser user = ExplorerApp.get().getLoggedInUser();

    // TODO: the counts should be done later by eg a Refresher component

//    ToolbarPopupEntry taskItem = addPopupEntry(ENTRY_TASKS, (i18nManager.getMessage(Messages.TASK_MENU_TASKS)));
//    
//    // Inbox
//    long inboxCount = new InboxListQuery(user.getId()).size();
//    
//    taskItem.addMenuItem( i18nManager.getMessage(Messages.TASK_MENU_INBOX) + " (" + inboxCount + ")", new ToolbarCommand() {
//      public void toolBarItemSelected() {
//        viewManager.showInboxPage();
//      }
//    });
//    
//  
//
//    long unassignedCount = new UnassignedListQuery(user.getId()).size();
//    taskItem.addMenuItem( i18nManager.getMessage(Messages.TASK_MENU_QUEUED) + " (" + unassignedCount + ")", new ToolbarCommand() {
//
//      public void toolBarItemSelected() {
//        viewManager.showUnassignedPage();
//      }
//    });
//   
    
  
     
  }

  protected void initActions() {
    Button newCaseButton = new Button();
    newCaseButton.setCaption("Search");
    newCaseButton.setIcon(FontAwesome.SEARCH);
    newCaseButton.setHtmlContentAllowed(true);
    addButton(newCaseButton);

    newCaseButton.addListener(new ClickListener() {
      public void buttonClick(ClickEvent event) {
        SearchPopupWindow searchPopupWindow = new SearchPopupWindow();
        searchPopupWindow.addSearchListener(searchListener);
        viewManager.showPopupWindow(searchPopupWindow);
      }
    });

    addProcessButton( "New Triage", FontAwesome.PLUS, triageDefinition);
    addProcessButton( "Open Triage", FontAwesome.FOLDER_OPEN, mashDefinition);


  }

private void addProcessButton(String label, Resource icon, ProcessDefinition processDef) {
	Button newTriage = new Button();
	newTriage.setIcon(icon);
    newTriage.setCaption(label);
    newTriage.setHtmlContentAllowed(true);
    addButton(newTriage);

    ProcessDefinitionPage processDefinitionPage;

	newTriage.addClickListener(new StartProcessInstanceClickListener(processDef, taskPage));
}

private void initProcessesDefinition() {

	triageDefinition = repositoryService
    .createProcessDefinitionQuery()
    .latestVersion()
    .active().processDefinitionKey("triage").list().get(0);

	mashDefinition = repositoryService
		    .createProcessDefinitionQuery()
		    .latestVersion()
		    .active().processDefinitionKey("mash").list().get(0);

}

}

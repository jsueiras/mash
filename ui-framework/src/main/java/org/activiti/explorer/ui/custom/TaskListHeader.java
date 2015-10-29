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

package org.activiti.explorer.ui.custom;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.I18nManager;
import org.activiti.explorer.Messages;
import org.activiti.explorer.ViewManager;
import org.activiti.explorer.ui.mainlayout.ExplorerLayout;
import org.apache.bcel.generic.NEW;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Reindeer;

import elemental.html.EntriesCallback;


/**
 * Header that is shown on top of each task list,
 * which allows the quick creation of new tasks.
 * 
 * @author Frederik Heremans
 * @author Joram Barrez
 */
public class TaskListHeader extends Panel {

  private static final long serialVersionUID = 1L;

  protected I18nManager i18nManager;
  protected transient TaskService taskService;
  
  protected HorizontalLayout layout;
  protected TextField inputField;

private ViewManager viewManager;
  
  public static final String ENTRY_INBOX = "inbox";
  public static final String ENTRY_UNASSIGNED = "unassigned";
 
  
  public TaskListHeader(String pageSelected) {
    this.i18nManager = ExplorerApp.get().getI18nManager();
    this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
    this.viewManager = ExplorerApp.get().getViewManager();

    addStyleName(Reindeer.PANEL_LIGHT);
    addStyleName(ExplorerLayout.STYLE_SEARCHBOX);
    
    layout = new HorizontalLayout();
    layout.setHeight(36, UNITS_PIXELS);
    layout.setWidth(99, UNITS_PERCENTAGE); // 99, otherwise the Panel will display scrollbars
    layout.setSpacing(true);
    //layout.setMargin(false, true, false, true);
    setContent(layout);
    
    initInputField(pageSelected);
    //initKeyboardListener();
    // initSortMenu();
  }   

  protected void initInputField(String pageSelected) {
    // Csslayout is used to style inputtext as rounded
   
     ComboBox list = new ComboBox();
     list.addItem(ENTRY_INBOX);
      list.setItemCaption(ENTRY_INBOX,i18nManager.getMessage(Messages.TASK_MENU_INBOX));
      
     list.addItem(ENTRY_UNASSIGNED);
     list.setItemCaption(ENTRY_UNASSIGNED,i18nManager.getMessage(Messages.TASK_MENU_QUEUED));
     list.setNullSelectionAllowed(false);
     list.setValue(pageSelected);
     
     
     Property.ValueChangeListener listener = new Property.ValueChangeListener() {
    	    public void valueChange(ValueChangeEvent event) {
    	      String window = (String) event.getProperty().getValue();
    	      if (ENTRY_UNASSIGNED.equals(window))
    	      {
    	    	 viewManager.showUnassignedPage();
    	      }	 
    	      else
    	      {
    	    	  viewManager.showInboxPage();
    	      } 	  
    	    }
    	};
    	list.addValueChangeListener(listener);
		
		
     layout.addComponent(list);
     
     
    
    //layout.setComponentAlignment(csslayout);
    //layout.setExpandRatio(csslayout, 1.0f);
  }
  
  protected void initKeyboardListener() {
    addActionHandler(new Handler() {
      public void handleAction(Action action, Object sender, Object target) {
        if (inputField.getValue() != null && !"".equals(inputField.getValue().toString())) {
          
          // Create task
          Task task = taskService.newTask();
          task.setName(inputField.getValue().toString());
          task.setOwner(ExplorerApp.get().getLoggedInUser().getId());
          taskService.saveTask(task);
          
          // Switch to the new task
          ExplorerApp.get().getViewManager().showTasksPage(task.getId());
        }
      }
      public Action[] getActions(Object target, Object sender) {
        return new Action[] {new ShortcutAction("enter", ShortcutAction.KeyCode.ENTER, null)};
      }
    });
  }
  
  protected void initSortMenu() {
    MenuBar menuBar = new MenuBar();
    menuBar.addStyleName(ExplorerLayout.STYLE_SEARCHBOX_SORTMENU);
    
    //TODO: Adding types of sorting manually and listener/events
    MenuItem rootItem = menuBar.addItem("Sort by", null);
    rootItem.addItem("Id", null);
    rootItem.addItem("Name", null);
    rootItem.addItem("Due date", null);
    rootItem.addItem("Creation date", null);
    
    layout.addComponent(menuBar);
    layout.setComponentAlignment(menuBar, Alignment.MIDDLE_RIGHT);
  }
  
  @Override
  public void focus() {
    inputField.focus();
  }
  
  
}

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
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.I18nManager;
import org.activiti.explorer.Messages;
import org.activiti.explorer.ViewManager;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;


/**
 * Header that is shown on top of each task list,
 * which allows the quick creation of new tasks.
 *
 * @author Frederik Heremans
 * @author Joram Barrez
 */
public class TaskListHeader extends ComboBox {

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

    addStyleName("task-list-header");
    setTextInputAllowed(false);

    setHeightUndefined();
    setWidth(100, Unit.PERCENTAGE);

    addItem(ENTRY_INBOX);
    setItemCaption(ENTRY_INBOX, i18nManager.getMessage(Messages.TASK_MENU_INBOX));

    addItem(ENTRY_UNASSIGNED);
    setItemCaption(ENTRY_UNASSIGNED, i18nManager.getMessage(Messages.TASK_MENU_QUEUED));
    setNullSelectionAllowed(false);
    setValue(pageSelected);

    Property.ValueChangeListener listener = new Property.ValueChangeListener() {
      @Override
      public void valueChange(Property.ValueChangeEvent event) {
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
    addValueChangeListener(listener);
  }

  @Override
  public void focus() {
    inputField.focus();
  }


}

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

import java.util.Date;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.I18nManager;
import org.activiti.explorer.Messages;
import org.activiti.explorer.ui.custom.PopupWindow;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Popup window to create a new task
 *
 * @author Joram Barrez
 */
public class SearchPopupWindow extends PopupWindow {

  private static final long serialVersionUID = 1L;


  SearchDetailPanel searchPanel;

  public SearchPopupWindow() {
    setId("search-popup-window");
    setModal(true);
    setResizable(true);
    setCaption("Search");
    setWidth(40, Unit.EM);
    setHeight(80, Unit.PERCENTAGE);

    initPanel();
  }

  private void initPanel() {
    searchPanel = new SearchDetailPanel();
    searchPanel.setId("search-panel");
    setContent(searchPanel);
  }

public void addSearchListener(SearchTabEventListener listener)
{
	searchPanel.addListener(listener);
}


}

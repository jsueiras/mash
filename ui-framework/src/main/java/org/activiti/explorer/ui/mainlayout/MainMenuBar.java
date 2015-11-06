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

package org.activiti.explorer.ui.mainlayout;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.activiti.explorer.Environments;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.I18nManager;
import org.activiti.explorer.Messages;
import org.activiti.explorer.ViewManager;
import org.activiti.explorer.identity.LoggedInUser;
import org.activiti.explorer.ui.Images;
import org.activiti.explorer.ui.profile.ChangePasswordPopupWindow;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ChameleonTheme;
import com.vaadin.ui.themes.Reindeer;


/**
 * @author Joram Barrez
 * @author Frederik Heremans
 */
@SuppressWarnings("serial")
public class MainMenuBar extends HorizontalLayout {

  private static final long serialVersionUID = 1L;

  protected ViewManager viewManager;
  protected I18nManager i18nManager;
  protected Map<String, Button> menuItemButtons;
  protected String currentMainNavigation;

  public MainMenuBar() {

    this.viewManager = ExplorerApp.get().getViewManager();
    this.i18nManager = ExplorerApp.get().getI18nManager();

    menuItemButtons = new HashMap<String, Button>();
    init();
  }

  /**
   * Highlights the given main navigation in the menubar.
   */
  public synchronized void setMainNavigation(String navigation) {

    currentMainNavigation = navigation;

    Button current = menuItemButtons.get(navigation);
    if(current != null) {
      current.addStyleName(ExplorerLayout.STYLE_ACTIVE);
    }
  }

  protected void init() {
    setHeightUndefined();
    setWidth(100, Unit.PERCENTAGE);

    initTitle();
    initProfileButton();
  }

  protected void initTitle() {
    Label title = new Label("Multi Agency Safeguarding Hub");
    title.addStyleName(ExplorerLayout.STYLE_APPLICATION_LOGO);
    title.setSizeUndefined();
    addComponent(title);
    setComponentAlignment(title, Alignment.MIDDLE_LEFT);
    setExpandRatio(title, 1);
  }

  protected Button addMenuButton(String type, String label, Resource icon, boolean active, float width) {
    Button button = new Button(label);
    button.addStyleName(type);
    button.addStyleName(ExplorerLayout.STYLE_MAIN_MENU_BUTTON);
    button.setIcon(icon);

    addComponent(button);
    setComponentAlignment(button, Alignment.MIDDLE_RIGHT);
    setExpandRatio(button, 0);

    return button;
  }

  protected void initProfileButton() {
    final LoggedInUser user = ExplorerApp.get().getLoggedInUser();

    // User name + link to profile
    MenuBar profileMenu = new MenuBar();
    profileMenu.addStyleName(ExplorerLayout.STYLE_HEADER_PROFILE_BOX);
    profileMenu.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
    MenuItem rootItem = profileMenu.addItem(user.getFirstName() + " " + user.getLastName(), null);
    rootItem.setIcon(FontAwesome.USER);
    rootItem.setStyleName(ExplorerLayout.STYLE_HEADER_PROFILE_MENU);



    // Change password
    rootItem.addItem(i18nManager.getMessage(Messages.PASSWORD_CHANGE), new Command() {
      public void menuSelected(MenuItem selectedItem) {
        ExplorerApp.get().getViewManager().showPopupWindow(new ChangePasswordPopupWindow());
      }
    });

    rootItem.addSeparator();

    // Logout
    rootItem.addItem(i18nManager.getMessage(Messages.HEADER_LOGOUT), new Command() {
      public void menuSelected(MenuItem selectedItem) {
        ExplorerApp.get().close();
      }
    });

    addComponent(profileMenu);
    setComponentAlignment(profileMenu, Alignment.TOP_RIGHT);
    setExpandRatio(profileMenu, 1.0f);
  }



  // Listener classes
  private class ShowTasksClickListener implements ClickListener {
    public void buttonClick(ClickEvent event) {
      ExplorerApp.get().getViewManager().showInboxPage();
    }
  }

  private class ShowProcessDefinitionsClickListener implements ClickListener {
    public void buttonClick(ClickEvent event) {
       ExplorerApp.get().getViewManager().showDeployedProcessDefinitionPage();
    }
  }

  private class ShowReportsClickListener implements ClickListener {
    public void buttonClick(ClickEvent event) {
       ExplorerApp.get().getViewManager().showRunReportPage();
    }
  }

  private class ShowManagementClickListener implements ClickListener {
    public void buttonClick(ClickEvent event) {
     // ExplorerApp.get().getViewManager().showDatabasePage();
    }
  }
}

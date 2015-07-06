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
package org.activiti.explorer.ui;

import org.activiti.explorer.I18nManager;
import org.activiti.explorer.Messages;
import org.activiti.explorer.navigation.UriFragment;
import org.activiti.explorer.ui.mainlayout.ExplorerLayout;
import org.activiti.explorer.ui.mainlayout.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.ui.Component;
import com.vaadin.ui.Window;

/**
 * @author Joram Barrez
 */
public class MainWindow extends Window {

  private static final long serialVersionUID = 1L;
  
  @Autowired
  protected I18nManager i18nManager;
 
  // UI
  protected MainLayout mainLayout;
  protected UriFragment currentUriFragment;
  protected boolean showingLoginPage;

  public MainWindow() {
  //  setTheme(ExplorerLayout.THEME);
  }
  
  @Override
  public void attach() {
    super.attach();
    setCaption(i18nManager.getMessage(Messages.APP_TITLE));
  }

  public void showLoginPage() {
    showingLoginPage = true;
    addStyleName(ExplorerLayout.STYLE_LOGIN_PAGE);
    //setContent(new LoginPage());
  }
  
  public void showDefaultContent() {
    showingLoginPage = false;
    removeStyleName(ExplorerLayout.STYLE_LOGIN_PAGE);
    addStyleName("Default style"); // Vaadin bug: must set something or old style (eg. login page style) is not overwritten
    
    // init general look and feel
    mainLayout = new MainLayout();
    setContent(mainLayout);

    // init hidden components
    //initHiddenComponents();
  }
  
  // View handling
  
  public void switchView(Component component) {
    mainLayout.setMainContent(component);
  }
  
  public void setMainNavigation(String navigation) {
    mainLayout.setMainNavigation(navigation);
  }
  
  // URL handling
  
  
  
  public UriFragment getCurrentUriFragment() {
    return currentUriFragment;
  }

  

  
  
  public boolean isShowingLoginPage() {
    return showingLoginPage;
  }
  
  
  
  public void setI18nManager(I18nManager i18nManager) {
    this.i18nManager = i18nManager;
  }
}

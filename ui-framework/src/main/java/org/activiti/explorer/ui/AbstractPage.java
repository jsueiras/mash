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

import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;
import org.activiti.explorer.ui.custom.ToolBar;


/**
 * Superclass for all Explorer pages
 */
public abstract class AbstractPage extends CustomComponent {

  private static final long serialVersionUID = 1L;

  protected ToolBar toolBar;
  protected AbstractSelect select;
  protected boolean showEvents;
  private VerticalLayout mainLayout = new VerticalLayout();
  protected HorizontalLayout content;
  private VerticalLayout sideBar = new VerticalLayout();
  private HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();

  // Overriding attach(), so we can construct the components first, before the UI is built,
  // that way, all member fields of subclasses are initialized properly
  @Override
  public void attach() {
   initUi();
  }

  /**
   * Override this method (and call super()) when you want to influence the UI.
   */
  protected void initUi() {
    showEvents = getEventComponent() != null;

    addMainLayout();
    setSizeFull();
    addMenuBar();
    addSearch();
    addSelectComponent();
     addEventComponent();
  }

  protected void addEventComponent() {
	Component eventComponent = getEventComponent();
	if (eventComponent !=null) {
      eventComponent.setId("event-component");
      splitPanel.setSecondComponent(eventComponent);
      eventComponent.setSizeFull();
    }
  }

  /**
   * Subclasses are expected to provide their own menuBar.
   */
  protected void addMenuBar() {

    // Remove any old menu bar
    String activeEntry = null;
    if (toolBar != null) {
      activeEntry = toolBar.getCurrentEntryKey();
      mainLayout.removeComponent(toolBar);
    }

    // Create menu bar
    ToolBar menuBar = createMenuBar();
    if (menuBar != null) {
      toolBar = createMenuBar();
      toolBar.setId("tool-bar");
      toolBar.addStyleName("v-panel-caption");
      mainLayout.addComponentAsFirst(toolBar);
      mainLayout.setExpandRatio(toolBar, 0);

      if (activeEntry != null) {
        toolBar.setActiveEntry(activeEntry);
      }
    }
  }

  public ToolBar getToolBar() {
    return toolBar;
  }

  protected abstract ToolBar createMenuBar();

  protected void addMainLayout() {
    content = new HorizontalLayout();
    content.setId("content");

    content.addStyleName(Reindeer.SPLITPANEL_SMALL);
    content.setSizeFull();

    setCompositionRoot(mainLayout);
    mainLayout.setSizeFull();

    mainLayout.addComponent(content);
    mainLayout.setExpandRatio(content, 1);

    content.addComponent(sideBar);
    content.setExpandRatio(sideBar, 0);
    sideBar.setWidth(20, Unit.EM);
    sideBar.setHeight(100, Unit.PERCENTAGE);
    sideBar.setId("side-bar");

    content.addComponent(splitPanel);
    content.setExpandRatio(splitPanel, 1);
    splitPanel.setSizeFull();
    splitPanel.setSplitPosition(505, Unit.PIXELS);

    mainLayout.setId("outer-panel");
    splitPanel.setId("inner-panel");
  }

  protected void addSearch() {
    Component searchComponent = getSearchComponent();
    if(searchComponent != null) {
      searchComponent.setId("search-component");
      sideBar.addComponentAsFirst(searchComponent);
      sideBar.setExpandRatio(searchComponent, 0);
      searchComponent.setWidth(100, Unit.PERCENTAGE);
      searchComponent.setHeightUndefined();
    }
  }

  protected void addSelectComponent() {
    AbstractSelect select = createSelectComponent();
    if (select != null) {
      select.setId("select");
      sideBar.addComponent(select);
      sideBar.setExpandRatio(select, 1);
      select.setSizeFull();
    }
  }

  /**
   * Returns an implementation of {@link AbstractSelect},
   * which will be displayed on the left side of the page,
   * allowing to select elements from eg. a list, tree, etc.
   */
  protected abstract AbstractSelect createSelectComponent();

  /**
   * Refreshes the elements of the list, and selects the next
   * one (useful when the selected element is deleted).
   */
  public abstract void refreshSelectNext();

  /**
   * Select a specific element from the selection component.
   */
  public abstract void selectElement(int index);

  protected void setDetailComponent(Component detail) {
    if(splitPanel.getFirstComponent() != null) {
      splitPanel.setFirstComponent(null);
    }
    if(detail != null) {
      detail.setId("detail");
      splitPanel.setFirstComponent(detail);
    }
  }

  protected Component getDetailComponent() {
    return splitPanel.getFirstComponent();
  }

  /**
   * Override to get the search component to display above the table. Return null
   * when no search should be displayed.
   */
  public Component getSearchComponent() {
    return null;
  }

  /**
   * Get the component to display the events in.
   *
   * Return null by default: no event-component will be used,
   * in that case the main UI will be two columns instead of three.
   *
   * Override in case the event component must be shown:
   * three columns will be used then.
   */
  protected Component getEventComponent() {
    return null;
  }

}

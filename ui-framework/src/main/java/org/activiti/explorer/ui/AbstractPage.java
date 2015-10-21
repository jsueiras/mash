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
import org.activiti.explorer.ui.custom.ToolBar;


/**
 * Superclass for all Explorer pages
 */
public abstract class AbstractPage extends CustomComponent {

  private static final long serialVersionUID = 1L;

  protected ToolBar toolBar;
  protected VerticalLayout grid;
  protected AbstractSelect select;
  protected boolean showEvents;
  private HorizontalLayout outerPanel = new HorizontalLayout();
  private HorizontalSplitPanel innerPanel = new HorizontalSplitPanel();

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
      innerPanel.setSecondComponent(eventComponent);
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
      grid.removeComponent(toolBar);
    }

    // Create menu bar
    ToolBar menuBar = createMenuBar();
    if (menuBar != null) {
      toolBar = createMenuBar();
      toolBar.setId("tool-bar");
      grid.addComponent(toolBar, 0);
      grid.setExpandRatio(toolBar, 0);

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
    grid = new VerticalLayout();

    grid.addStyleName(Reindeer.SPLITPANEL_SMALL);
    grid.setWidthUndefined();
    grid.setHeight(100, Unit.PERCENTAGE);

    setCompositionRoot(outerPanel);
    outerPanel.setSizeFull();

    outerPanel.addComponent(grid);
    outerPanel.setExpandRatio(grid, 0);

    outerPanel.addComponent(innerPanel);
    outerPanel.setExpandRatio(innerPanel, 1);

    outerPanel.setId("outer-panel");
    innerPanel.setId("inner-panel");
  }

  protected void addSearch() {
    Component searchComponent = getSearchComponent();
    if(searchComponent != null) {
      searchComponent.setId("search-component");
      grid.addComponent(searchComponent, 1);
      grid.setExpandRatio(searchComponent, 0);
      searchComponent.setWidth(100, Unit.PERCENTAGE);
    }
  }

  protected void addSelectComponent() {
    AbstractSelect select = createSelectComponent();
    if (select != null) {
      select.setId("select");
      grid.addComponent(select, 2);
      grid.setExpandRatio(select, 1);
      select.setWidth(100, Unit.PERCENTAGE);
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
    if(innerPanel.getFirstComponent() != null) {
      innerPanel.setFirstComponent(null);
    }
    if(detail != null) {
      detail.setId("detail");
      innerPanel.setFirstComponent(detail);
    }
  }

  protected Component getDetailComponent() {
    return innerPanel.getFirstComponent();
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

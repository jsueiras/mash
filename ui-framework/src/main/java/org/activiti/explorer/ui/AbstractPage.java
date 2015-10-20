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
  private HorizontalSplitPanel outerSplitPanel = new HorizontalSplitPanel();
  private HorizontalSplitPanel innerSplitPanel = new HorizontalSplitPanel();

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
    //if(showEvents) {
     addEventComponent();
    //}
  }

  protected void addEventComponent() {
	Component eventComponent = getEventComponent();
	if (eventComponent !=null) {
      eventComponent.setId("event-component");
      innerSplitPanel.setSecondComponent(eventComponent);
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
      grid.addComponent(toolBar);
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
    grid.setSizeFull();

    setCompositionRoot(outerSplitPanel);
    outerSplitPanel.setFirstComponent(grid);
    outerSplitPanel.setSecondComponent(innerSplitPanel);

    outerSplitPanel.setSplitPosition(20, Unit.PERCENTAGE);

    outerSplitPanel.setId("outer-split-panel");
    innerSplitPanel.setId("inner-split-panel");
  }

  protected void addSearch() {
    Component searchComponent = getSearchComponent();
    if(searchComponent != null) {
      searchComponent.setId("search-component");
      grid.addComponent(searchComponent);
      grid.setExpandRatio(searchComponent, 0);
    }
  }

  protected void addSelectComponent() {
    AbstractSelect select = createSelectComponent();
    if (select != null) {
      select.setId("select");
      grid.addComponent(select);
      grid.setExpandRatio(select, 1);
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
    if(innerSplitPanel.getFirstComponent() != null) {
      innerSplitPanel.setFirstComponent(null);
    }
    if(detail != null) {
      detail.setId("detail");
      innerSplitPanel.setFirstComponent(detail);
    }
  }

  protected Component getDetailComponent() {
    return innerSplitPanel.getFirstComponent();
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

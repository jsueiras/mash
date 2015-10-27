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

import org.activiti.explorer.ui.custom.PopupWindow;

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
    setHeight(95, Unit.PERCENTAGE);

    searchPanel = new SearchDetailPanel();
    searchPanel.setId("search-panel");
    searchPanel.setSizeFull();
    setContent(searchPanel);
  }

  public void addSearchListener(SearchTabEventListener listener)
{
	searchPanel.addListener(listener);
}


}

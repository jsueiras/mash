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

import com.vaadin.data.util.PropertysetItem;


/**
 * @author Joram Barrez
 */
public  class SearchResultItem extends PropertysetItem implements Comparable<SearchResultItem>{

  private static final long serialVersionUID = 1L;
  
  public SearchResultItem() {
    }
  
 

  public int compareTo(SearchResultItem other) {
    String taskId = (String) getItemProperty("id").getValue();
    String otherTaskId = (String) other.getItemProperty("id").getValue();
    return taskId.compareTo(otherTaskId);
  }
  
}

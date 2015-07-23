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

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.data.AbstractLazyLoadingQuery;

import com.mash.data.service.Repository;
import com.vaadin.data.Item;


/**
 * @author Joram Barrez
 */
public abstract class AbstractSearchQuery extends AbstractLazyLoadingQuery {
  
  protected String userId;
  protected transient Repository repository;
  
  public AbstractSearchQuery(String userId) {
    this.userId = userId;
    this.repository = ExplorerApp.get().getMashRepository();
  }

  public int size() {
    return (int) getQuery().count();
  }

  public List<Item> loadItems(int start, int count) {
    List<Task> tasks = getQuery().listPage(start, count);
    List<Item> items = new ArrayList<Item>();
    
    return items;
  }

 
  public void setSorting(Object[] propertyId, boolean[] ascending) {
    throw new UnsupportedOperationException();
  }
  
  protected abstract TaskQuery getQuery();
  
}

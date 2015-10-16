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
package org.activiti.explorer.ui.task.data;

import java.util.List;

import org.activiti.engine.identity.Group;
import org.activiti.engine.task.TaskQuery;
import org.activiti.explorer.ExplorerApp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.cglib.core.Transformer;


/**
 * @author Joram Barrez
 */
public class UnassignedListQuery extends AbstractTaskListQuery {
  
  public UnassignedListQuery(String userId) {
		super(userId);
		// TODO Auto-generated constructor stub
	}

@Override
  protected TaskQuery getQuery() {
    List<Group> candidateGroups = ExplorerApp.get().getLoggedInUser().getGroups();
    List<String> ids = CollectionUtils.transform(candidateGroups, new Transformer() {
		
		@Override
		public String transform(Object arg0) {
			// TODO Auto-generated method stub
			return ((Group)arg0).getId();
		}
	});
	return taskService.createTaskQuery().taskUnassigned().taskCandidateGroupIn(ids).orderByTaskId().asc();
  }
  
}

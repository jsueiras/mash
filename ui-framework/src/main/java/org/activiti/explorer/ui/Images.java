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

import com.vaadin.server.ThemeResource;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;


/**
 * Contains statically created {@link Resource} instances for all the images.
 * 
 * @author Joram Barrez
 */
public class Images {
  
  private static final String IMAGE_PATH = "img/";
// General
  public static final Resource WARNING = new ThemeResource(IMAGE_PATH + "warning.png");
  public static final Resource DELETE = new ThemeResource(IMAGE_PATH + "delete.png");
  public static final Resource EXECUTE = new ThemeResource(IMAGE_PATH + "execute.png");
  public static final Resource USER_ADD = new ThemeResource(IMAGE_PATH + "user_add.png");
  public static final Resource SUCCESS = new ThemeResource(IMAGE_PATH + "tick.png");
  public static final Resource EDIT = new ThemeResource(IMAGE_PATH + "edit.png");
  public static final Resource SAVE = new ThemeResource(IMAGE_PATH + "save.png");
  public static final Resource MAGNIFIER_16 = new ThemeResource(IMAGE_PATH + "magnifier-16.png");
  
  public static final Resource MAIN_MENU_TASKS = new ThemeResource(IMAGE_PATH + "mm-tasks.png");
  public static final Resource MAIN_MENU_PROCESS = new ThemeResource(IMAGE_PATH + "mm-process.png");
  public static final Resource MAIN_MENU_MANAGE = new ThemeResource(IMAGE_PATH + "mm-manage.png");
  public static final Resource MAIN_MENU_REPORTS = new ThemeResource(IMAGE_PATH + "mm-reports.png");
  
  // Task
  public static final Resource TASK_16 =  new ThemeResource(IMAGE_PATH + "task-16.png");
  public static final Resource TASK_22 = new ThemeResource(IMAGE_PATH + "task-22.png");
  public static final Resource TASK_50 = new ThemeResource(IMAGE_PATH + "task-50.png");
  public static final Resource TASK_FINISHED_22 = new ThemeResource(IMAGE_PATH + "task-finished-22.png");
  public static final Resource TASK_DUE_DATE_16 = new ThemeResource(IMAGE_PATH + "duedate-16.png");
  
  // Accounts
  public static final Resource SKYPE = new ThemeResource(IMAGE_PATH + "skype.png");
  public static final Resource GOOGLE = new ThemeResource(IMAGE_PATH + "google.png");
  public static final Resource ALFRESCO = new ThemeResource(IMAGE_PATH + "alfresco.gif");
  public static final Resource IMAP = new ThemeResource(IMAGE_PATH + "imap.png");
  
  // Database
  public static final Resource DATABASE_HISTORY = new ThemeResource(IMAGE_PATH + "database-history-22.png");
  public static final Resource DATABASE_IDENTITY = new ThemeResource(IMAGE_PATH + "database-identity-22.png");
  public static final Resource DATABASE_REPOSITORY = new ThemeResource(IMAGE_PATH + "database-repository-22.png");
  public static final Resource DATABASE_RUNTIME = new ThemeResource(IMAGE_PATH + "database-runtime-22.png");
  public static final Resource DATABASE_50 = new ThemeResource(IMAGE_PATH + "database-50.png");
  public static final Resource DATABASE_22 = new ThemeResource(IMAGE_PATH + "database-22.png");
  
  // Deployment
  public static final Resource DEPLOYMENT_50 = new ThemeResource(IMAGE_PATH + "deployment-50.png");
  public static final Resource DEPLOYMENT_22 = new ThemeResource(IMAGE_PATH + "deployment-22.png");
  
  // Jobs
  public static final Resource JOB_50 = new ThemeResource(IMAGE_PATH + "job-50.png");
  public static final Resource JOB_22 = new ThemeResource(IMAGE_PATH + "job-22.png");
  
  // User administration
  public static final Resource USER_16 = new ThemeResource(IMAGE_PATH + "user-16.png");
  public static final Resource USER_22 = new ThemeResource(IMAGE_PATH + "user-22.png");
  public static final Resource USER_32 = new ThemeResource(IMAGE_PATH + "user-32.png");
  public static final Resource USER_50 = new ThemeResource(IMAGE_PATH + "user-50.png");
  
  public static final Resource GROUP_16 = new ThemeResource(IMAGE_PATH + "group-16.png");
  public static final Resource GROUP_22 = new ThemeResource(IMAGE_PATH + "group-22.png");
  public static final Resource GROUP_32 = new ThemeResource(IMAGE_PATH + "group-32.png");
  public static final Resource GROUP_50 = new ThemeResource(IMAGE_PATH + "group-50.png");
  
  // Related content
  public static final Resource RELATED_CONTENT_URL = new ThemeResource(IMAGE_PATH + "page_white_world.png");
  public static final Resource RELATED_CONTENT_FILE = new ThemeResource(IMAGE_PATH + "page_white_get.png");
  public static final Resource RELATED_CONTENT_PDF = new ThemeResource(IMAGE_PATH + "page_white_acrobat.png");
  public static final Resource RELATED_CONTENT_PICTURE = new ThemeResource(IMAGE_PATH + "picture.png");

  // Process
  public static final Resource PROCESS_50 = new ThemeResource(IMAGE_PATH + "process-50.png");
  public static final Resource PROCESS_22 = new ThemeResource(IMAGE_PATH + "process-22.png");
  public static final Resource PROCESS_EDITOR_BPMN = new ThemeResource(IMAGE_PATH + "button-bpmn-48.png");
  public static final Resource PROCESS_EDITOR_TABLE = new ThemeResource(IMAGE_PATH + "button-table-48.png");
  
  // Reports
  public static final Resource REPORT_50 = new ThemeResource(IMAGE_PATH + "report-50.png");
  public static final Resource REPORT_22 = new ThemeResource(IMAGE_PATH + "report-22.png");
  
}

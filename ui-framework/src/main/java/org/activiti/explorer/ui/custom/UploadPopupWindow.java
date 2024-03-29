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
package org.activiti.explorer.ui.custom;

import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.I18nManager;

import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.themes.Reindeer;


/**
 * @author Joram Barrez
 */
public class UploadPopupWindow extends PopupWindow { 
  
  private static final long serialVersionUID = 1L;
  
  // Services
  protected I18nManager i18nManager;
  protected UploadComponent uploadComponent;


  public UploadPopupWindow(String caption, String description, Receiver receiver) {
    this.i18nManager = ExplorerApp.get().getI18nManager();

    init(caption, description, receiver);
    
    uploadComponent.addFinishedListener(new FinishedListener() {
      
      private static final long serialVersionUID = 1L;

      public void uploadFinished(FinishedEvent event) {
        close();
      }
    });
  }

  // UI initialisation ----------------------------------------------------------------------------
  protected void init(String caption, String description, Receiver receiver) {
    uploadComponent = ExplorerApp.get().getComponentFactory(UploadComponentFactory.class).create();
    uploadComponent.setReceiver(receiver);
    uploadComponent.setDescription(description);
    uploadComponent.setSizeFull();
    initWindow(caption);
  }

  protected void initWindow(String caption) {
    // Fixed width/height since otherwise the layout can be screwed by the drag and drop
    setWidth("300px");
    setHeight("300px");
    addStyleName(Reindeer.WINDOW_LIGHT);
    setModal(true);
    center();
    setCaption(caption);
    
    setContent(uploadComponent);
  }
  
  
  // Upload Listeners ----------------------------------------------------------------------------
  public void addFinishedListener(FinishedListener finishedListener) {
    uploadComponent.addFinishedListener(finishedListener);
  }
  
  public void addStartedListener(StartedListener startedListener) {
    uploadComponent.addStartedListener(startedListener);
  }
  
  public void addFailedListener(FailedListener failedListener) {
    uploadComponent.addFailedListener(failedListener);
  }
  
  public void addProgressListener(ProgressListener progressListener) {
    uploadComponent.addProgressListener(progressListener);
  }  
}

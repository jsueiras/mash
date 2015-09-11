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

package org.activiti.explorer.ui.form.custom;

import java.util.Map;
import java.util.Map.Entry;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.explorer.I18nManager;
import org.activiti.explorer.Messages;
import org.activiti.explorer.form.custom.MemoFormType;
import org.activiti.explorer.ui.form.AbstractFormPropertyRenderer;

import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/**
 * @author Frederik Heremans
 */
public class MemoFormPropertyRenderer extends AbstractFormPropertyRenderer {

  public MemoFormPropertyRenderer() {
    super(MemoFormType.class);
  }

  @Override
  public Field getPropertyField(FormProperty formProperty) {
	  TextArea textField = new TextArea(getPropertyLabel(formProperty));
   
    textField.setRequired(formProperty.isRequired());
    textField.setEnabled(formProperty.isWritable());
    textField.setRequiredError(getMessage(Messages.FORM_FIELD_REQUIRED, getPropertyLabel(formProperty)));

    if (formProperty.getValue() != null) {
      textField.setValue(formProperty.getValue());
    }
    
     MemoFormType type = (MemoFormType) formProperty.getType();
   
     System.out.println("Name     " + type.getName());
     System.out.println("Rows     " + type.getRows());
      
    textField.setRows(type.getRows());
    textField.setColumns(type.getColumns());
   

    return textField;
  }

}

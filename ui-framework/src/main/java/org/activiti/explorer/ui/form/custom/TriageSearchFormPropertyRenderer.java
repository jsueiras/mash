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

import mash.graph.NetworkChangeListener;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.form.EnumFormType;
import org.activiti.explorer.I18nManager;
import org.activiti.explorer.Messages;
import org.activiti.explorer.form.custom.TriageSearchFormType;
import org.activiti.explorer.ui.form.AbstractFormPropertyRenderer;
import org.activiti.explorer.ui.search.SearchTabEventListener;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Field;

/**
 * @author Frederik Heremans
 * @author Joram Barrez
 */
public class TriageSearchFormPropertyRenderer extends AbstractFormPropertyRenderer {

  /**
	 * 
	 */
	private static final long serialVersionUID = 3100658513017375578L;

public TriageSearchFormPropertyRenderer() {
    super(TriageSearchFormType.class);
  }

 
  @Override
  public TriageSearchField getPropertyField(FormProperty formProperty) {
	  
	  Map<String, String> values = (Map<String, String>) formProperty.getType().getInformation("values"); 
	  String value = formProperty.getValue(); 
	 TriageSearchField comboBox = new TriageSearchField(values,formProperty.getValue(),getPropertyLabel(formProperty));
    comboBox.setRequired(formProperty.isRequired());
    comboBox.setRequiredError(getMessage(Messages.FORM_FIELD_REQUIRED, getPropertyLabel(formProperty)));
    comboBox.setEnabled(formProperty.isWritable());
    comboBox.setNullSelectionAllowed(false);

      return comboBox;
  }
  
  @Override
  public TriageSearchField getPropertyField(FormProperty formProperty, Listener listener) {
	  TriageSearchField triageSearch = getPropertyField(formProperty);
	  triageSearch.addSearchListener((SearchTabEventListener) listener);
	 return triageSearch;
  }
  @Override
  public NetworkChangeListener getNetworkChangeListener(Field field)
  {
	  return ((TriageSearchField)field).getNetworkChangeListener();
  }
  

	
}

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

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.form.FormPropertyHandler;
import org.activiti.engine.impl.form.FormPropertyImpl;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.I18nManager;
import org.activiti.explorer.Messages;
import org.activiti.explorer.ui.form.FormPropertiesComponent;
import org.activiti.explorer.ui.form.FormPropertyRenderer;
import org.activiti.explorer.ui.form.StringFormPropertyRenderer;
import org.activiti.explorer.ui.mainlayout.ExplorerLayout;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import com.mash.data.service.Query;
import com.mash.model.catalog.Location;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;


/**
 * Form that renders form-properties and allows posting the filled in value. Performs
 * validation as well. Exposes {@link SearchFormEvent}s which allow listening for 
 * submission and cancellation of the form.
 * 
 * @author Frederik Heremans
 */
public class SearchForm extends VerticalLayout {
  
  private static final long serialVersionUID = -3197331726904715949L;

  // Services
  protected transient FormService formService;
  protected I18nManager i18nManager;

  // UI
  protected Label formTitle;
  protected Button submitFormButton;
  protected Button cancelFormButton;
  
  public SearchForm() {
    super();
    this.setSpacing(true);
    formService = ProcessEngines.getDefaultProcessEngine().getFormService();
    this.i18nManager = ExplorerApp.get().getI18nManager();
    
    addStyleName(ExplorerLayout.STYLE_DETAIL_BLOCK);
    addStyleName(ExplorerLayout.STYLE_FORM_PROPERTIES);
    
    initTitle();
    initFormFields();
    initButtons();
    initListeners();
  }
  
  
 
  
  public void setFormHelp(String caption) {
    formTitle.setValue(caption);
    formTitle.setVisible(caption != null);
  }
  
  /**
   * Clear all (writable) values in the form.
   */
  public void clear() {
    //formPropertiesComponent.setFormProperties(formPropertiesComponent.getFormProperties());
  }

  protected void initTitle() {
    formTitle = new Label();
    formTitle.addStyleName(ExplorerLayout.STYLE_H4);
    formTitle.setVisible(false);
    addComponent(formTitle);
  }
 
  protected void initButtons() {
    submitFormButton = new Button();
    cancelFormButton = new Button();
    submitFormButton.setCaption("Search");
    cancelFormButton.setCaption("Clear");
    
    HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    buttons.setWidth(100, UNITS_PERCENTAGE);
    buttons.addStyleName(ExplorerLayout.STYLE_DETAIL_BLOCK);
    buttons.addComponent(submitFormButton);
    buttons.setComponentAlignment(submitFormButton, Alignment.BOTTOM_RIGHT);
    
    buttons.addComponent(cancelFormButton);
    buttons.setComponentAlignment(cancelFormButton, Alignment.BOTTOM_RIGHT);
    
    Label buttonSpacer = new Label();
    buttons.addComponent(buttonSpacer);
    buttons.setExpandRatio(buttonSpacer, 1.0f);
    addComponent(buttons);
  }

  protected void initFormFields() {
	  HorizontalLayout group = new HorizontalLayout();
	  group.setSpacing(true);
	
	 group.addComponent(getTextField("First Name","firstName"));
	 group.addComponent(getTextField("Last Name","lastName"));
	 addComponent(group);
	 
	 group = new HorizontalLayout();
	 group.setSpacing(true);
     
	 group.addComponent(getDateField("DOB From" , "dateOfBirthFrom", "dd-MM-yyyy"));
	 group.addComponent(getDateField("DOB To" , "dateOfBirthTo", "dd-MM-yyyy"));
	 group.addComponent(genderCombo());
	 addComponent(group);
		 
	 group = new HorizontalLayout();
	 group.setSpacing(true);
	 group.addComponent(getTextField("Number","sampleLocation.numberOrName"));
	 group.addComponent(getTextField("Street","sampleLocation.street"));
	 addComponent(group);
	 
	 group = new HorizontalLayout();
	 group.setSpacing(true);
	 group.addComponent(getTextField("City","sampleLocation.city"));
	 group.addComponent(getTextField("PostCode","sampleLocation.postcode"));
	 addComponent(group);
	
  }




private ComboBox genderCombo() {
	ComboBox gender = new ComboBox("Gender");
	  gender.setId("gender");
	  gender.setRequired(false);
	  gender.setNullSelectionAllowed(true);
      gender.addItem("M");
      gender.setItemCaption("M","Male");
      gender.addItem("F");
      gender.setItemCaption("F","Female");
	return gender;
}




private TextField getTextField(String label, String id) {
	TextField lastName = new TextField(label);
	lastName.setRequired(false);
	lastName.setEnabled(true);
	lastName.setRequiredError("Search required");
	lastName.setWidth(250,Unit.PIXELS);
	lastName.setId(id);
	return lastName;
}

private PopupDateField getDateField(String label, String id,String datePattern)
{
    PopupDateField dateField = new PopupDateField(label);
    dateField.setId(id);
    dateField.setDateFormat(datePattern);
    dateField.setRequired(false);
    dateField.setEnabled(true);
    return dateField;
}
  

  public  Map<String, Object> getFormPropertyValues() throws InvalidValueException {
	    // Commit the form to ensure validation is executed
	  
	    Map<String, Object> formPropertyValues = new HashMap<String, Object>();
	    Query queryBean = new Query(); 
	    queryBean.setSampleLocation(new Location());
	    populateQueryBean(queryBean,this);
	    formPropertyValues.put("queryBean", queryBean);
	    return formPropertyValues;
	  }




private void populateQueryBean(Query queryBean, AbstractOrderedLayout root) {
	// Get values from fields defined for each form property
	
	for (int i=0;i<root.getComponentCount();i++) {
	   Component component = root.getComponent(i);
	  if(component instanceof Field) {
	    Field field = (Field) component;
	
				try {
					PropertyUtils.setNestedProperty(queryBean,field.getId(),field.getValue());
				} catch (IllegalAccessException | InvocationTargetException
						| NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
	  }
	  else if (component instanceof AbstractOrderedLayout)
	  {
		  populateQueryBean(queryBean, (AbstractOrderedLayout) component);
	  }	  
	   
	}
}
  

protected void initListeners() {
    submitFormButton.addClickListener(new ClickListener() {
      
      private static final long serialVersionUID = -6091586145870618870L;
    
      public void buttonClick(ClickEvent event) {
        // Extract the submitted values from the form. Throws exception when validation fails.
        try {
          Map<String, Object> formProperties = getFormPropertyValues();
          fireEvent(new SearchFormEvent(SearchForm.this, SearchFormEvent.TYPE_SUBMIT, formProperties));
          submitFormButton.setComponentError(null);
        } catch(InvalidValueException ive) {
          // Error is presented to user by the form component
        }
      }
    });
    
    cancelFormButton.addClickListener(new ClickListener() {
      
      private static final long serialVersionUID = -8980500491522472381L;

      public void buttonClick(ClickEvent event) {
        fireEvent(new SearchFormEvent(SearchForm.this, SearchFormEvent.TYPE_CANCEL));
        submitFormButton.setComponentError(null);
      }
    });
  }
  
  public void hideCancelButton() {
    cancelFormButton.setVisible(false);
  }
  
  protected void addEmptySpace(ComponentContainer container) {
    Label emptySpace = new Label("&nbsp;", Label.CONTENT_XHTML);
    emptySpace.setSizeUndefined();
    container.addComponent(emptySpace);
  }
  
  /**
   * Event indicating a form has been submitted or cancelled. When submitted,
   * the values of the form-properties are available.
   * 
   *
   */
  public class SearchFormEvent extends Event {

    private static final long serialVersionUID = -410814526942034125L;
    
    public static final String TYPE_SUBMIT = "SUBMIT";
    public static final String TYPE_CANCEL = "CANCEL";
    
    private String type;
    private Map<String, Object> formProperties;
    
    public SearchFormEvent(Component source, String type) {
      super(source);
      this.type = type;
    }
    
    public SearchFormEvent(Component source, String type, Map<String, Object> formProperties) {
      this(source, type);
      this.formProperties = formProperties;
    }
    
    public String getType() {
      return type;
    }
    
    public Map<String, Object> getFormProperties() {
      return formProperties;
    }
  }
}

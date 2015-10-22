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

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
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
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;


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
    addGroup(getTextField("First Name", "firstName"), getTextField("Last Name", "lastName"));
    addGroup(getDateField("Born on or after", "dateOfBirthFrom", "dd-MM-yyyy"),
            getDateField("Born before or on", "dateOfBirthTo", "dd-MM-yyyy"), genderCombo());
    addGroup(getTextField("Number", "sampleLocation.numberOrName"), getTextField("Street", "sampleLocation.street"));
    addGroup(getTextField("City", "sampleLocation.city"), getTextField("Postcode", "sampleLocation.postcode"));
  }

  private void addGroup(Component... contents) {
    HorizontalLayout group = new HorizontalLayout();
	addStyleName("group");
    group.setSpacing(true);
    group.setSizeUndefined();

    for (Component component : contents) {
      group.addComponent(component);
      if ((component.getWidthUnits() == Unit.PERCENTAGE) && (component.getWidth() == 100)) {
        group.setExpandRatio(component, 1);
        group.setWidth(100, Unit.PERCENTAGE);
      }
      else {
        group.setExpandRatio(component, 0);
      }
    }
    addComponent(group);
  }


  private OptionGroup genderCombo() {
    OptionGroup gender = new OptionGroup("Gender");
    gender.setId("gender");
	gender.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
    gender.setRequired(false);
    gender.setNullSelectionAllowed(true);
    gender.addItem("F");
    gender.setItemCaption("F", "Female");
    gender.addItem("M");
    gender.setItemCaption("M", "Male");
    return gender;
}

private TextField getTextField(String label, String id) {
	TextField lastName = new TextField(label);
	lastName.setRequired(false);
	lastName.setEnabled(true);
	lastName.setRequiredError("Search required");
	lastName.setWidth(100, Unit.PERCENTAGE);
	lastName.setId(id);
	return lastName;
}

private PopupDateField getDateField(String label, String id,String datePattern)
{
    PopupDateField dateField = new PopupDateField(label);
    dateField.setId(id);
	dateField.setWidth(9, Unit.EM);
    dateField.setDateFormat(datePattern);
    dateField.setRequired(false);
    dateField.setEnabled(true);
    return dateField;
}


  public  Query getSearchQuery() throws InvalidValueException {
	    // Commit the form to ensure validation is executed

	    Query queryBean = new Query();
	    queryBean.setSampleLocation(new Location());
	    populateQueryBean(queryBean,this);

	    return queryBean;
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
          Query queryBean= getSearchQuery();
          fireEvent(new SearchFormEvent(SearchForm.this, SearchFormEvent.TYPE_SUBMIT,queryBean ));
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


}

package org.activiti.explorer.ui.form.custom;

import org.activiti.engine.form.AbstractFormType;

public class PersonFormType extends AbstractFormType {
	 
	  public static final String TYPE_NAME = "person";
	 
	  public String getName() {
	    return TYPE_NAME;
	  }
	 
	  public Object convertFormValueToModelValue(String propertyValue) {
	    Integer month = Integer.valueOf(propertyValue);
	    return month;
	  }
	 
	  public String convertModelValueToFormValue(Object modelValue) {
	    if (modelValue == null) {
	      return null;
	    }
	    return modelValue.toString();
	  }
	 
	}
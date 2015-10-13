package org.activiti.explorer.form.custom;

import java.util.Map;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.form.AbstractFormType;


public class TriageSearchFormType extends AbstractFormType{
	 
	  public static final String TYPE_NAME = "triageSearch";
	
	  protected Map<String, String> values;

	   public TriageSearchFormType(Map<String, String> values) {
	    this.values = values;
	  }

	  public String getName() {
	    return TYPE_NAME;
	  }
	  
	  @Override
	  public Object getInformation(String key) {
	    if (key.equals("values")) {
	      return values;
	    }
	    return null;
	  }

	  @Override
	  public Object convertFormValueToModelValue(String propertyValue) {
	    validateValue(propertyValue);
	    return propertyValue;
	  }

	  @Override
	  public String convertModelValueToFormValue(Object modelValue) {
	    if(modelValue != null) {
	      if(!(modelValue instanceof String)) {
	        throw new ActivitiIllegalArgumentException("Model value should be a String");
	      }
	      validateValue((String) modelValue);
	    }
	    return (String) modelValue;
	  }
	  
	  protected void validateValue(String value) {
	    if(value != null) {
	      if(values != null && !values.containsKey(value)) {
	        throw new ActivitiIllegalArgumentException("Invalid value for enum form property: " + value);
	      }
	    }
	  }

	 
	

	 

	
	 
	}
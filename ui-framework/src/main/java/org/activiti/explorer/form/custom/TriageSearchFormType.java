package org.activiti.explorer.form.custom;

import java.util.Map;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.form.AbstractFormType;
import org.activiti.explorer.ui.form.custom.TriageSearchValue;


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
	  public TriageSearchValue convertFormValueToModelValue(String propertyValue) {
	   // validateValue(propertyValue);
	    return new TriageSearchValue(propertyValue, null);
	  }

	  @Override
	  public String convertModelValueToFormValue(Object modelValue) {
	    if(modelValue != null) {
	      if(!(modelValue instanceof TriageSearchValue)) {
	        throw new ActivitiIllegalArgumentException("Model value should be a String");
	      }
	      validateValue((TriageSearchValue) modelValue);
	    }
	    return getValue(modelValue);
	  }

	private String getValue(Object modelValue) {
		if (modelValue!=null) return ((TriageSearchValue) modelValue).getValue();
		else return null;
	}
	  
	  protected void validateValue(TriageSearchValue value) {
	   
	  }

	 
	

	 

	
	 
	}
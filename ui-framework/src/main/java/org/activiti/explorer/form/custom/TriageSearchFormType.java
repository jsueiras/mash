package org.activiti.explorer.form.custom;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.form.AbstractFormType;
import org.activiti.explorer.ui.form.custom.TriageSearchValue;
import org.apache.commons.codec.binary.Base64;


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
	   // validateValue(propertyValue);
	    return propertyValue;
	  }

	  @Override
	  public String convertModelValueToFormValue(Object modelValue) {
	    if(modelValue != null) {
	      if(!(modelValue instanceof String)) {
	        throw new ActivitiIllegalArgumentException("Model value should be a String");
	      }
	    
	      return   (String) modelValue;
	    }
	    return null;
	   
	  }


	  
	  protected void validateValue(TriageSearchValue value) {
	   
	  }

	 
	
	 


	
	 
	}
package org.activiti.explorer.form.custom;

import java.util.Map;

import org.activiti.engine.form.AbstractFormType;


public class MemoFormType extends AbstractFormType{
	 
	  public static final String TYPE_NAME = "memo";
	private int rows;
	private int columns;
	 
	  
	  public String getName() {
	    return TYPE_NAME + rows +"x" + columns;
	  }
	  
	public MemoFormType(int rows, int columns) {
		  this.rows = rows;
		  this.columns = columns;
     }

	@Override
	public Object convertFormValueToModelValue(String propertyValue) {
		// TODO Auto-generated method stub
		return propertyValue;
	}

	@Override
	public String convertModelValueToFormValue(Object modelValue) {
		// TODO Auto-generated method stub
		return (String) modelValue;
	}
	 
	public int getRows() {
		return rows;
	}
	
	public int getColumns() {
		return columns;
	}
	
	 
	}
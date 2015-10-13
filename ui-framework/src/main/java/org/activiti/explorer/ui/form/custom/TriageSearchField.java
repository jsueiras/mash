package org.activiti.explorer.ui.form.custom;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.ui.Images;
import org.activiti.explorer.ui.search.SearchPopupWindow;
import org.activiti.explorer.ui.search.SearchTabEventListener;
import org.springframework.aop.target.HotSwappableTargetSource;

import com.vaadin.client.ui.Field;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class TriageSearchField extends CustomField<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, String> values;
	private String selectedValue;
	private SearchPopupWindow searchPopupWindow;
	private ComboBox comboBox;
	private  Button searchButton;
	private SearchTabEventListener searchListener;
	
	public TriageSearchField(Map<String,String> values, String currentValue) {
		this.values = values;
		this.selectedValue = currentValue;
		comboBox = new ComboBox();
	}

	@Override
	protected Component initContent() {
	
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		 initCombo();
		 initActions();
		 horizontalLayout.addComponent(comboBox);
		 horizontalLayout.addComponent(searchButton);
		
		return horizontalLayout;
	}
	
	  protected  void initActions() {
		    searchButton = new Button();
		    searchButton.setCaption("Search");
		    searchButton.setIcon(Images.TASK_16);
		    searchButton.setEnabled(false);
		  
		    
		    searchButton.addClickListener(new ClickListener() {
		     

			public void buttonClick(ClickEvent event) {
		        searchPopupWindow = new SearchPopupWindow();
		        searchPopupWindow.addSearchListener(searchListener);
		        ExplorerApp.get().getViewManager().showPopupWindow(searchPopupWindow);
		      }
		    });
		  
		  }

	private void initCombo() {
		  Object firstItemId = null;
		    Object itemToSelect = null;
		   
		for (Entry<String, String> enumEntry : values.entrySet()) {
			// Add value and label (if any)
	        comboBox.addItem(enumEntry.getKey());
	        
	        if (firstItemId == null) {
	          firstItemId = enumEntry.getKey(); // select first element
	        }
	        
	        
	        if (selectedValue != null && selectedValue.equals(enumEntry.getKey())) {
	          itemToSelect = enumEntry.getKey(); // select first element
	        }
	        
	        if (enumEntry.getValue() != null) {
	          comboBox.setItemCaption(enumEntry.getKey(), enumEntry.getValue());
	        }
		}
		
		comboBox.addValueChangeListener(new ValueChangeListener(){

			@Override
			public void valueChange(
					com.vaadin.data.Property.ValueChangeEvent event) {
				if (event.getProperty().getValue()!= null) searchButton.setEnabled(true);
				
			}});
		
	}

	@Override
	public Class<? extends String> getType() {
		
		return String.class;
	}
	
	public void addSearchListener(SearchTabEventListener listener)
	{
		this.searchListener = listener;
	}

	public void setNullSelectionAllowed(boolean b) {
		comboBox.setNullSelectionAllowed(b);
	}
	
	@Override
	public void setRequired(boolean b)
	{
		comboBox.setRequired(b);
	}
	
	@Override
	public void setEnabled(boolean b)
	{
		comboBox.setEnabled(b);
	}
	
	 @Override
	 public String getValue(){
		 return (String) comboBox.getValue();
	 }


	
}

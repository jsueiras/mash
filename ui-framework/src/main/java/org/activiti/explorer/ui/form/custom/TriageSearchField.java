package org.activiti.explorer.ui.form.custom;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import mash.graph.NetworkChangeEvent;
import mash.graph.NetworkState;

import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.ui.Images;
import org.activiti.explorer.ui.search.SearchPopupWindow;
import org.activiti.explorer.ui.search.SearchTabEvent;
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
import com.vaadin.ui.Component.Event;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class TriageSearchField extends CustomField<TriageSearchValue> {

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
	private String label;
	private NetworkState networkState;
	
	public TriageSearchField(Map<String,String> values, String currentValue, String label) {
		this.values = values;
		this.selectedValue = currentValue;
		this.label = label;
		comboBox = new ComboBox();
		
	}

	@Override
	protected Component initContent() {
	
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		 initCombo();
		 initActions();
		 horizontalLayout.addComponent(new Label(label));
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
	public Class<? extends org.activiti.explorer.ui.form.custom.TriageSearchValue> getType() {
		
		return TriageSearchValue.class;
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
	 public TriageSearchValue getValue(){
		 return new TriageSearchValue( (String)comboBox.getValue(), networkState);
	 }


	 
	 public class NetworkChangeListener implements Listener {

		  private static final long serialVersionUID = 7560512657831865244L;

		  public final void componentEvent(Event event) {
		    if(event instanceof NetworkChangeEvent) {
		        networkState = ((NetworkChangeEvent)event).getNewState();
		    }
		  }
	 }  
	 
  
	
}

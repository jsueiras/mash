package org.activiti.explorer.ui.form.custom;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import mash.graph.NetworkChangeEvent;
import mash.graph.NetworkChangeListener;
import mash.graph.NetworkState;

import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.ui.Images;
import org.activiti.explorer.ui.search.SearchPopupWindow;
import org.activiti.explorer.ui.search.SearchTabEvent;
import org.activiti.explorer.ui.search.SearchTabEventListener;
import org.springframework.aop.target.HotSwappableTargetSource;

import com.vaadin.client.ui.Field;
import com.vaadin.server.FontAwesome;
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
	private String label;
	private NetworkState networkState;

	public TriageSearchField(Map<String,String> values, String currentValue, String label) {
		this.values = values;
		 if (currentValue!=null)
		 {
			 TriageSearchValue searchValue = TriageSearchValue.stringToObject(currentValue);
			 selectedValue = searchValue.getValue();
			 networkState =searchValue.getNetworkState();
		 }

		setCaption(label);
		comboBox = new ComboBox();

	}

	@Override
	protected Component initContent() {


		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		horizontalLayout.setWidth(100, Unit.PERCENTAGE);
		initCombo();
		initActions();

		horizontalLayout.addComponent(comboBox);
		horizontalLayout.setExpandRatio(comboBox, 1);
		comboBox.setWidth(100, Unit.PERCENTAGE);

		horizontalLayout.addComponent(searchButton);
		horizontalLayout.setExpandRatio(searchButton, 0);

		return horizontalLayout;
	}

	  protected  void initActions() {
		    searchButton = new Button();
		    searchButton.setCaption("Search");
		    searchButton.setIcon(FontAwesome.SEARCH);
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
		 return new TriageSearchValue( (String)comboBox.getValue(), networkState).objectToString();
	 }


	 public NetworkChangeListener getNetworkChangeListener()
	 {
		 return new NetworkChangeListener() {

		@Override
		protected void handleNetworkChange(NetworkChangeEvent event) {
			networkState = event.getNewState();
		}


	   };
	 }



}

package org.activiti.explorer.ui.form.custom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mash.graph.NetworkChangeEvent;
import mash.graph.NetworkChangeListener;
import mash.graph.NetworkState;

import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.ui.Images;
import org.activiti.explorer.ui.form.custom.TriageSearchValue.TriagePersonSummary;
import org.activiti.explorer.ui.search.SearchPopupWindow;
import org.activiti.explorer.ui.search.SearchTabEvent;
import org.activiti.explorer.ui.search.SearchTabEventListener;
import org.apache.commons.collections.functors.TransformerClosure;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.target.HotSwappableTargetSource;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.cglib.core.Predicate;
import org.springframework.cglib.core.Transformer;

import com.mash.model.catalog.Entity;
import com.mash.model.catalog.Location;
import com.mash.model.catalog.Person;
import com.mash.model.catalog.Relation;
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
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class TriageSearchField extends CustomField<String> {

		private static final long serialVersionUID = 1L;
	private Map<String, String> values;
	private String selectedValue;
	private SearchPopupWindow searchPopupWindow;
	private ComboBox comboBox;
	private  Button searchButton;
	private SearchTabEventListener searchListener;
	private String label;
	private NetworkState networkState;
	private List<TriagePersonSummary> subjects;
	protected Table table;

	public TriageSearchField(Map<String,String> values, String currentValue, String label) {
		this.subjects = new ArrayList<TriageSearchValue.TriagePersonSummary>();
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


		VerticalLayout overallLayout = new VerticalLayout();
		overallLayout.setSpacing(true);
		overallLayout.setWidth(100, Unit.PERCENTAGE);
		initCombo();
		initActions();
	    initTable();
		overallLayout.addComponent(comboBox);
		overallLayout.setExpandRatio(comboBox, 1);
		comboBox.setWidth(100, Unit.PERCENTAGE);
		comboBox.setTextInputAllowed(false);

		overallLayout.addComponent(searchButton);
		overallLayout.setExpandRatio(searchButton, 0);
		overallLayout.addComponent(table);

		return overallLayout;
	}

	  protected  void initActions() {
		    searchButton = new Button();
		    searchButton.setCaption("Search subjects");
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

	private void initTable()
	{
		table = TableBuilder.createPersonTable();


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
		 return new TriageSearchValue( (String)comboBox.getValue(), networkState,subjects).objectToString();
	 }


	 public NetworkChangeListener getNetworkChangeListener()
	 {
		 return new NetworkChangeListener() {


			private static final long serialVersionUID = -8340156883977266632L;

		@Override
		protected void handleNetworkChange(NetworkChangeEvent event) {
			networkState = event.getNewState();
		    summarizePersonData(event.getPrimaryLinks(),event.isAppend());
		    TableBuilder.appendResults(subjects, table);
	   }

	 };
	 }

     private void summarizePersonData(List<Entity> links,boolean append)
     {
    	  final String mainEntityId;
    	  if (append && subjects.size()>0)
    	  {
    		  mainEntityId=subjects.get(0).getId();
    	  }
    	  else
    	  {
    		  mainEntityId= links.get(0).getId();
    	  }
    		List<Entity> copy = new ArrayList<Entity>();
    		copy.addAll(links);

    	  Collection persons = CollectionUtils.filter(copy, new Predicate() {
			@Override
			public boolean evaluate(Object arg0) {
				// TODO Auto-generated method stub
				return arg0 instanceof Person;
			}
		});
    	  if (persons.size()>0)
    	  {
			List<TriagePersonSummary> results= CollectionUtils.transform(
	    			    persons, new SummaryTransformer(mainEntityId));
	    	   if (append)
	    		   this.subjects.addAll(results);
	    	   else
	    		   this.subjects = results;
    	  }

        }

     private final class SummaryTransformer implements Transformer {

 		private String mainEntityId;

		public SummaryTransformer(String mainEntityId) {
			this.mainEntityId = mainEntityId;
		}

		private String tranformAddress(Person person)
 		{
 		    String address = "";
 		   if (person.getHomeAddress() !=null && person.getHomeAddress().getLocation()!=null )
 		   {
 			   Location location= person.getHomeAddress().getLocation();
 			   address = String.format("%s %s %s", getValue(location.getNumberOrName()),getValue( location.getStreet()), getValue(location.getPostcode()));
 		   }
 		   return address;
 		}

 		private String getValue(String s)
 		{
 			return (s!=null)?s:"";
 		}

 		private String getRelation(Person person)
 		{
 		   for (Relation relation : person.getHousehold().getRelations()) {
			    if (relation.getPerson().getId().equals(mainEntityId))
			    {
			    	return relation.getType();
			    }
		     }
 		   return "";
 		}

 		@Override
 		public Object transform(Object arg0) {
 			// TODO Auto-generated method stub
 			Person person = (Person)arg0;
 			TriagePersonSummary summary = new TriagePersonSummary();
 			summary.setFirstName(person.getFirstName());
 			summary.setLastName(person.getLastName());
 			if (person.getDateOfBirth()!=null)
 			{
 				summary.setDateOfBirth(person.getDateOfBirth().toGregorianCalendar().getTime());
 			}
 			summary.setHomeAddress(tranformAddress(person));
 			summary.setRelationship(getRelation(person));
 			return summary;
 		}
 	}

 	/**
 	 *
 	 */


}

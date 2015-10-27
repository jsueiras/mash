package org.activiti.explorer.ui.form.custom;

import java.text.SimpleDateFormat;
import java.util.List;

import org.activiti.explorer.ui.Images;
import org.activiti.explorer.ui.form.custom.TriageSearchValue.TriagePersonSummary;
import org.activiti.explorer.ui.mainlayout.ExplorerLayout;
import org.activiti.explorer.ui.search.util.Decorator;
import org.activiti.explorer.ui.util.ThemeImageColumnGenerator;

import com.mash.model.catalog.Address;
import com.mash.model.catalog.Entity;
import com.mash.model.catalog.Location;
import com.mash.model.catalog.Person;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Table;

public class TableBuilder {
	
	protected static Table createPersonTable() {
		Table personTable = new Table();
		personTable.setWidth(100, Unit.PERCENTAGE);
		personTable.setPageLength(10);
		personTable.addStyleName(ExplorerLayout.STYLE_TASK_LIST);
		personTable.addStyleName(ExplorerLayout.STYLE_SCROLLABLE);
		personTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_EXPLICIT_DEFAULTS_ID);

	// Create column header
		personTable.addGeneratedColumn("", new ThemeImageColumnGenerator(Images.TASK_22));
		personTable.setColumnWidth("", 22);

		personTable.addContainerProperty("Name", String.class, null);
		personTable.setColumnWidth("Name", 250);

		personTable.addContainerProperty("Address", String.class, null);
		personTable.setColumnWidth("Address", 250);

		personTable.addContainerProperty("DOB", String.class, null);
		personTable.setColumnWidth("DOB", 50);

		personTable.addContainerProperty("Relationship", String.class, null);
		personTable.setColumnWidth("Relationship", 50);


		return personTable;
	}
	
	protected static void appendResults(List<TriagePersonSummary> results, Table table) {

		table.removeAllItems();
		for (TriagePersonSummary entity : results) {
			table.addItem(getTableRow(entity), entity.getId());
		}
		
		table.setVisible(true);
		if (results.size() < 10) {
			table.setPageLength(results.size());
		}
	}

	
	
	 private static String getValue(String val)
	 {
		 if (val!=null) return val;
		 return "";
	 }
	 
	 private static String getName(TriagePersonSummary person){
		return getValue(person.getFirstName()) + " " + getValue(person.getLastName()); 
	 }
	 
	
	 
	 public static Object[] getTableRow(TriagePersonSummary person) {
	
		 return new Object[] {getName(person),person.getHomeAddress(),getDateOfBirth(person),getValue(person.getRelationship())};
	}

		private static String getDateOfBirth(TriagePersonSummary person) {
			// TODO Auto-generated method stub
			 if (person.getDateOfBirth() != null) {
			      // Try parsing the current value
			      SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	               return dateFormat.format(person.getDateOfBirth());
			    
			    }
			return "" ;
		}

}

package org.activiti.explorer.ui.search.person;

import java.text.SimpleDateFormat;

import com.mash.model.catalog.Address;
import com.mash.model.catalog.Entity;
import com.mash.model.catalog.Household;
import com.mash.model.catalog.Location;
import com.mash.model.catalog.Occupant;
import com.mash.model.catalog.Person;
import com.mash.model.catalog.Relation;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Tree;

public class Decorator {
	
	
	 private static String getValue(String val)
	 {
		 if (val!=null) return val;
		 return "";
	 }
	 
	 public static String getName(Person person){
		return getValue(person.getFirstName()) + " " + getValue(person.getLastName()); 
	 }
	 
	 public static String getSummaryAddress(Person person){
		 
	     Address homeAddress = person.getHomeAddress();
	     if (homeAddress== null) return "";
		Location location = homeAddress.getLocation();
		return getSummaryLocation(location);
	}

	private static String getSummaryLocation(Location location) {
		if (location== null) return "";
		return getValue(location.getNumberOrName()) +  " " + getValue(location.getStreet()) + ", " + getValue(location.getCity());
	}
	 
	 public static Tree getTreeComponent(Person person)
	 {
		 Tree tree = new Tree("Person Details");
		
		       
	        String personId = appendPerson( tree,person,null,true);
	        String householdId = "household" + personId;
			tree.addItem(householdId );
	        tree.setItemCaption(householdId ,"Household");
	        tree.setParent(householdId, personId);
	        
	        appendHouseHold(tree,householdId,person.getHousehold());
	        tree.expandItemsRecursively(personId);
	        return tree;
	 }

	private static String appendPerson( Tree tree,Person person,String parentId,boolean showAddress) {
		// Create the tree nodes
		String personId = person.getId();
		if (parentId!=null) personId= personId+"_"+parentId;
	
		Item root = tree.addItem(personId);
		tree.setItemCaption(personId, getName(person));
			
		if (parentId!= null) tree.setParent(personId, parentId);	
		
		if (showAddress)
		{		
		String addressId = personId +"address";
		tree.addItem(addressId);
		tree.setItemCaption(addressId, getSummaryAddress(person));
		tree.setParent(addressId,personId);
		}
		return personId;
	}
	
	 private static void appendHouseHold(Tree tree, String householdId,
			Household household) {
		 Integer i=0;
		for (Relation relation : household.getRelations()) {
			
			String itemId = householdId + i;
			tree.addItem(itemId)	;
			tree.setParent(itemId, householdId);
			tree.setItemCaption(itemId, relation.getType());
			
			appendPerson(tree,relation.getPerson(),itemId,true);
			i++;
	 }
		
	}



	public static Object[] getTableRow(Entity entity) {
		if (entity instanceof Person)
		{		
			Person person = (Person) entity;
		   return new Object[] {getName(person),getSummaryAddress(person),getDateOfBirth(person)};
		}
		else
		{
		   Location location = (Location) entity;
		return new Object[] {getSummaryLocation(location),getValue(location.getPostcode())}; 
		}	
	}

	private static String getDateOfBirth(Person person) {
		// TODO Auto-generated method stub
		 if (person.getDateOfBirth() != null) {
		      // Try parsing the current value
		      SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
               return dateFormat.format(person.getDateOfBirth().toGregorianCalendar().getTime());
		    
		    }
		return "" ;
	}

	public static Component getTreeComponent(Location location) {
		 Tree tree = new Tree("Location Details");
		 
		   String locationId = location.getId();
		   Item root = tree.addItem(locationId);
			tree.setItemCaption(locationId, getSummaryLocation(location));
		
	        String householdId = "ocuppants" + locationId;
			tree.addItem(householdId );
	        tree.setItemCaption(householdId ,"Occupants");
	        tree.setParent(householdId, locationId);
	        
	        for (Occupant occupant : location.getOccupants()) {
				appendPerson(tree, occupant.getPerson(), householdId, false);
	           
			}
	        
	        tree.expandItemsRecursively(locationId);
	        return tree;
	
	}

	public static Component getTreeComponent(Entity entity) {
		if (entity instanceof Person)
		{
			return getTreeComponent((Person) entity);
		}	
		else
		{
			return getTreeComponent((Location) entity);
		}		
	    		
	}

}

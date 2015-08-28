package org.activiti.explorer.ui.search.person;

import com.mash.model.catalog.Person;
import com.vaadin.data.Item;
import com.vaadin.ui.Tree;

public class Decorator {
	
	 public static String getName(Person person){
		return person.getFirstName() + " " + person.getLastName(); 
	 }
	 
	 public static String getSummaryAddress(Person person){
	     return person.getHomeAddress().getNumberOrName() +  " " + person.getHomeAddress().getStreet() + ", " + person.getHomeAddress().getCity();
	}
	 
	 public static Tree getTreeComponent(Person person)
	 {
		 Tree tree = new Tree("My Tree");
		       
	        // Create the tree nodes
	        String personId = person.getId();
			Item root = tree.addItem(personId);
	        tree.setItemCaption(personId, getName(person));
	        String itemId = "household" + personId;
			tree.addItem(itemId );
	        tree.setItemCaption(itemId ,"Household");
	        tree.setParent(itemId, personId);
	       
	        return tree;
	 }

}

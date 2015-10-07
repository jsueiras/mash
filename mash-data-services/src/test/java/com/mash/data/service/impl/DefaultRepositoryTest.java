package com.mash.data.service.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.mash.data.service.Query;
import com.mash.data.service.Repository;
import com.mash.model.catalog.Act;
import com.mash.model.catalog.Location;
import com.mash.model.catalog.Person;

public class DefaultRepositoryTest {
	
	

	@Test
	public void testGetPerson() {
		
	   Repository rep = new DefaultRepository();
	   Person p = rep.findPersonById("homer");
	   assertNotNull(p);
	   
	}
	
	@Test
	public void testGetPeople() {
		
	   Repository rep = new DefaultRepository();
	   Query query = new Query();
	   query.setFirstName("Simpson");
	   List<Person> p = rep.findPersons(query);
	   assertNotNull(p);
	   
	}
	
	

	@Test
	public void testGetLocation() {
		
	   Repository rep = new DefaultRepository();
	   Location p = rep.findLocationById("homerAddress");
	   assertNotNull(p);
	   assertTrue(p.getOccupants().size()>3);
	}
	
	@Test
	public void testGetLocations() {
		
	   Repository rep = new DefaultRepository();
	   Query query = new Query();
	   Location sampleLocation = new Location();
	  
	   sampleLocation.setCity("Springfield");
	   List<Location> p = rep.findLocations(sampleLocation);
	   assertNotNull(p);
	   
	}
	
	
	

}

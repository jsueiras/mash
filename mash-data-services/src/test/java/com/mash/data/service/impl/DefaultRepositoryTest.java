package com.mash.data.service.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
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
		Person p = rep.findPersonById("homer",null);
		assertNotNull(p);

	}

	@Test
	public void testGetPeople() throws IOException {

		Repository rep = new DefaultRepository();
		Query query = new Query();
		query.setLastName("Simpson");
		List<Person> p = rep.findPersons(query,null);
		assertNotNull(p);

	}
	
	@Test
	public void testGetSampson() throws IOException {

		Repository rep = new DefaultRepository();
		Query query = new Query();
		query.setLastName("Sampson");
		List<Person> p = rep.findPersons(query,null);
		List<String> ids = new ArrayList<String>();
		for (Person person : p) {
			ids.add(person.getId());
			
		}
		rep.findEntitiesById(ids, null);
		assertNotNull(p);

	}



	@Test
	public void testGetLocation() {

		Repository rep = new DefaultRepository();
		Location p = rep.findLocationById("springfield742",null);
		assertNotNull(p);
		assertTrue(p.getOccupants().size()>3);
	}

	@Test
	public void testGetLocations() throws IOException {

		Repository rep = new DefaultRepository();
		Query query = new Query();
		Location sampleLocation = new Location();

		sampleLocation.setCity("Springfield");
		List<Location> p = rep.findLocations(sampleLocation,null);
		assertNotNull(p);

	}




}
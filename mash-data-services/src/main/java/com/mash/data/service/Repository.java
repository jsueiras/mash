package com.mash.data.service;

import java.io.IOException;
import java.util.List;

import com.mash.model.catalog.Entity;
import com.mash.model.catalog.Location;
import com.mash.model.catalog.Person;
import com.mash.model.catalog.Referral;

public interface Repository {
	
	public Person findPersonById(String id);
	List<Person> findPersons(Query query) throws IOException;
	
	public List<Entity> findEntitiesById(List<String> ids);

	public Location findLocationById(String id);
	public List<Location> findLocations(Location sample) throws IOException;

	Referral findReferralById(String id);
	public Referral saveReferral(Referral referral);
}

package com.mash.data.service;

import java.io.IOException;
import java.util.List;

import com.mash.model.catalog.Entity;
import com.mash.model.catalog.Location;
import com.mash.model.catalog.Person;
import com.mash.model.catalog.Referral;

public interface Repository {
	
	public Person findPersonById(String id, SecurityInfo info);
	List<Person> findPersons(Query query, SecurityInfo info) throws IOException;

	public List<Entity> findEntitiesById(List<String> ids, SecurityInfo info);

	public Location findLocationById(String id, SecurityInfo info);
	public List<Location> findLocations(Location sample, SecurityInfo info) throws IOException;

	Referral findReferralById(String id, SecurityInfo info);
	public Referral saveReferral(Referral referral, SecurityInfo info);
}

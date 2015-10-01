package com.mash.data.service;

import java.util.List;

import com.mash.model.catalog.Act;
import com.mash.model.catalog.Entity;
import com.mash.model.catalog.Location;
import com.mash.model.catalog.Person;
import com.mash.model.catalog.Referral;

public interface Repository {
	
	public Person findPersonById(String id);
	
	public List<Entity> findEntitiesById(List<String> ids);

	public Location findLocationById(String id);

	
	public List<Person> findPersons(Query query);
	
	public List<Location> findLocations(Location sample);


	Referral findReferralById(String id);
	
	public Referral saveReferral(Referral referral);
	


}

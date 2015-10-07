package com.mash.data.service;

import java.util.ArrayList;

import org.springframework.http.MediaType;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.websocket.server.PathParam;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.marklogic.client.DatabaseClientFactory.Authentication;
import com.mash.model.catalog.Act;
import com.mash.model.catalog.Acts;
import com.mash.model.catalog.Address;
import com.mash.model.catalog.Crime;
import com.mash.model.catalog.Household;
import com.mash.model.catalog.Location;
import com.mash.model.catalog.ObjectFactory;
import com.mash.model.catalog.Parent;
import com.mash.model.catalog.Person;
import com.mash.model.catalog.Persons;
import com.mash.model.catalog.Referral;
import com.mash.model.catalog.Relation;
import com.mash.data.service.marklogic.JAXBStore;



@RestController
@RequestMapping("/service/data")
public class DataController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
	  private JAXBStore store = new JAXBStore("localhost",8000,"admin","admin", Authentication.DIGEST);
  
    	
  

    	@RequestMapping(value = "/referral"
    			,method = RequestMethod.POST)
    	ResponseEntity<?> addReferral( @RequestBody Referral ref) {
    	
    					String id;
						try {
							printReferral(ref);
							id = store.saveReferral(ref);
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("hola");
							e.printStackTrace(System.out);
							throw new PersistenceException(e);
						}

    					HttpHeaders httpHeaders = new HttpHeaders();
    					httpHeaders.setLocation(ServletUriComponentsBuilder
    							.fromCurrentRequest().path("/referral/{id}")
    							.buildAndExpand(id).toUri());
    					return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
    				

    	}

    	@RequestMapping(value = "/person/{id}", method = RequestMethod.GET ,
    			produces = {
    			MediaType.APPLICATION_XML_VALUE }
    	)
    	Person readPerson (@PathVariable String id) {
    		return getPersonMock();
    		
    	}

		private Person getPersonMock() {
			Person result=  new Person();
			result.setId("xxxxxx");
			result.setFirstName("first");
			result.setLastName("dfdfsda");
			result.setHomeAddress( new Address());
			result.getHomeAddress().setLocation(new Location());
			result.getHomeAddress().getLocation().setCity("Sutton");
			result.setHousehold(createHouseHold());
			
			return result;
		}
		
		private Person getParentMock() {
			Person result=  new Person();
			result.setFirstName("first");
			result.setLastName("dfdfsda");
			result.setHomeAddress( new Address());
			result.getHomeAddress().setLocation(new Location());
			result.getHomeAddress().getLocation().setCity("Sutton");
			
			
			return result;
		}

		private Household createHouseHold() {
			Household h = new Household();
			 List<JAXBElement<? extends Relation>> relations = h.getRelations();
		  		ObjectFactory of = new ObjectFactory();
               Parent p = of.createParent();
               p.setPerson(getParentMock());
			 
			relations.add(of.createParent(p));
			return h;
	      	
		}

    	@RequestMapping(value = "/person", method = RequestMethod.GET,
    			produces = {
    			MediaType.APPLICATION_XML_VALUE }
    		    )
    	Persons getPeople() {
    		
    		Persons  list = new Persons();
    		list.getPersons().add(getPersonMock());
    		return list;
    		
    	}

 
    	  @ResponseStatus(HttpStatus.NOT_FOUND)
    	    class UserNotFoundException extends RuntimeException {

    	    	public UserNotFoundException(String userId) {
    	    		super("could not find Person '" + userId + "'.");
    	    	}
    	    }
    	  
    	  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  	    class PersistenceException extends RuntimeException {

			public PersistenceException(Exception e) {
				super(e);
			}

  	    
  	    }

    	  private void printReferral(Referral ref) throws JAXBException
     	 {
     		 System.err.print("hola");
     		 
     		 JAXBContext jaxbContext = JAXBContext.newInstance(Referral.class);
     			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
     	 
     			// output pretty printed
     			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
     	 
     			jaxbMarshaller.marshal(ref, System.out);
     		 
     	 }
    	  

      	@RequestMapping(value = "/act", method = RequestMethod.GET
      			,produces = {
    			MediaType.APPLICATION_XML_VALUE }
    		    )
      	Acts getReferrals() {
      		ObjectFactory of = new ObjectFactory();

      		Acts  list = new Acts();
      	
      		Crime crime = new Crime();
      		crime.setId("crime");
      		Referral referral = new Referral();
      		crime.setId("referral");
      		referral.setId("subject");
      		 List<JAXBElement<? extends Act>> acts = list.getActs();
			JAXBElement<Crime> crimeElement = of.createCrime(crime);
      		 
      		 
      		acts.add(crimeElement);
      		acts.add(of.createReferral(referral));
      		return list;
      		
      	}
    	
  
}

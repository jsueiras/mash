package com.mash.data.service.example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.websocket.server.PathParam;

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

import com.mash.model.catalog.Location;
import com.mash.model.catalog.Person;



@RestController
@RequestMapping("/service/data")
public class DataController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    
    	
  
  
    	@RequestMapping(method = RequestMethod.POST)
    	ResponseEntity<?> add( @RequestBody Person input) {
    	
    					Person result = input;

    					HttpHeaders httpHeaders = new HttpHeaders();
    					httpHeaders.setLocation(ServletUriComponentsBuilder
    							.fromCurrentRequest().path("/person/{id}")
    							.buildAndExpand(result.getId()).toUri());
    					return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
    				

    	}

    	@RequestMapping(value = "/person/{id}", method = RequestMethod.GET)
    	Person readBookmark (@PathVariable String id) {
    		return getPersonMock();
    		
    	}

		private Person getPersonMock() {
			Person result=  new Person();
			result.setFirstName("first");
			result.setLastName("dfdfsda");
			result.setHomeAddress( new Location());
			result.getHomeAddress().setCity("Sutton");
			return result;
		}

    	@RequestMapping(value = "/person/", method = RequestMethod.GET)
    	Collection<Person> readBookmarks() {
    		List<Person>  list = new ArrayList<Person>();
    		list.add(getPersonMock());
    		return list;
    		
    	}

 
    	  @ResponseStatus(HttpStatus.NOT_FOUND)
    	    class UserNotFoundException extends RuntimeException {

    	    	public UserNotFoundException(String userId) {
    	    		super("could not find Person '" + userId + "'.");
    	    	}
    	    }

  
}

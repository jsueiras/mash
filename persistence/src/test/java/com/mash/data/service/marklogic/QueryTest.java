package com.mash.data.service.marklogic;

import static org.junit.Assert.*;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.marklogic.client.DatabaseClientFactory.Authentication;
import com.mash.model.catalog.Referral;

public class QueryTest {

	  private JAXBStore store = new JAXBStore("localhost",8000,"admin","admin", Authentication.DIGEST);
	    
	   
//	@Test
//	public void test() throws JAXBException {
//		
//		store.searchReferrals();
//		
//	}

}

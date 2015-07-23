package com.mash.data.service.marklogic;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.DatabaseClientFactory.Authentication;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.Format;
import com.marklogic.client.io.JAXBHandle;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.query.MatchDocumentSummary;
import com.marklogic.client.query.MatchLocation;
import com.marklogic.client.query.MatchSnippet;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.RawCombinedQueryDefinition;
import com.mash.model.catalog.Referral;

public class JAXBStore {
	
	
	String host;
	Integer port;
	String user;
	String password;
	private Authentication authType;
	

	    public JAXBStore(String host, Integer port, String user, String password,Authentication type )
	    {
	    	this.host= host;
	    	this.port = port;
	    	this.user = user;
	    	this.password = password;
	    	this.authType = type;
	    	
	    }
		
		public String saveReferral(Referral ref) throws JAXBException {
			
			// register the POJO classes
			DatabaseClientFactory.getHandleRegistry().register(
				JAXBHandle.newFactory(Referral.class)
				);

			// create the client
			DatabaseClient client = DatabaseClientFactory.newClient(
					host, port, user, password,authType);

			// create a manager for XML documents
			XMLDocumentManager docMgr = client.newXMLDocumentManager();

			
			// create an identifier for the document
			String docId =   java.util.UUID.randomUUID().toString();
			
			ref.setId(docId);
		
			// write the POJO as the document content
			docMgr.writeAs(docId, ref);

			
			// release the client
			client.release();
			return docId;
		}
		
		public Referral searchReferralById(String id) throws JAXBException
		{
			
			DatabaseClientFactory.getHandleRegistry().register(
					JAXBHandle.newFactory(Referral.class)
					);
			DatabaseClient client = DatabaseClientFactory.newClient(
					host, port, user, password,authType);
				// create a manager for searching
			QueryManager queryMgr = client.newQueryManager();

		  // create a manager for XML documents
		  XMLDocumentManager docMgr = client.newXMLDocumentManager();

           return docMgr.readAs(id, Referral.class);
		}
		
		public List<Referral> searchReferrals() throws JAXBException
		{
			List<Referral> list = new ArrayList<Referral>();
			// create the client
						
						
						DatabaseClientFactory.getHandleRegistry().register(
								JAXBHandle.newFactory(Referral.class)
								);
						DatabaseClient client = DatabaseClientFactory.newClient(
								host, port, user, password,authType);
			// create a manager for searching
			QueryManager queryMgr = client.newQueryManager();

			// create a manager for XML documents
			XMLDocumentManager docMgr = client.newXMLDocumentManager();

			// specify the query and options for the search criteria
			// in raw XML (raw JSON is also supported)
			String rawSearch = new StringBuilder()
	    	    .append("<search:search ")
	    	    .append(    "xmlns:search='http://marklogic.com/appservices/search'>")
	    	   // .append("<search:query>")
	  	       // .append(    "<search:term-query>")
	   	       // .append(        "<search:text>referral</search:text>")
	   	       // .append(    "</search:term-query>")
		       // .append("</search:query>")
		        .append("<search:options>")
            .append(    "<search:constraint name='Referral'>")
    	    .append(        "<search:value>")
    		.append(            "<search:element name='Referral' ns=''/>")
    	    .append(        "</search:value>")
            .append(    "</search:constraint>")
            .append("</search:options>")
	            .append("</search:search>")
	            .toString();

	        // create a search definition for the search criteria
	        RawCombinedQueryDefinition querydef
	                = queryMgr.newRawCombinedQueryDefinitionAs(Format.XML, rawSearch);

	        // create a handle for the search results
			SearchHandle resultsHandle = new SearchHandle();

			// run the search
			queryMgr.search(querydef, resultsHandle);

			System.out.println("(Strong Typed) Matched "+resultsHandle.getTotalResults()+
					" documents with structured query\n");

			// iterate over the result documents
			MatchDocumentSummary[] docSummaries = resultsHandle.getMatchResults();
			System.out.println("Listing "+docSummaries.length+" documents:\n");
			for (MatchDocumentSummary docSummary: docSummaries) {
				String uri = docSummary.getUri();
				int score = docSummary.getScore();
				
				// read the POJO from the document content
				Referral ref = docMgr.readAs(uri, Referral.class);
				list.add(ref);


			}
			return list;
		}

			

}

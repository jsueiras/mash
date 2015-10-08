package mash.document.utils.handler;

import java.util.Map;

import javax.xml.bind.JAXBException;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

public interface ElementHandler {
	
	 final String OPEN_TAG = "{";
	 final String END_TAG = "}";
	 final String VALUE_TAG = "${";
	 final String TABLE_TAG = "£{";
	 
	 void processElement(WordprocessingMLPackage template, Map<String,Object> context) throws Docx4JException, JAXBException;



}

package mash.document.utils.handler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;

public class TableHandler  extends BaseElementHandler {
	
	@Override
	public void processElement( WordprocessingMLPackage template, Map<String,Object> context) throws Docx4JException, JAXBException {
		List<Object> tables = getAllElementFromObject(template.getMainDocumentPart(), Tbl.class);
 
		for (Object object : tables) {
			Tbl tempTable = (Tbl) object;
		
		processTable(tempTable,context);
		}
	}

	
	private  void addRowsToTable(Tbl reviewtable, Tr templateRow, Collection collection, String prefix,int i) {
		
	  for (Object object : collection) {
		
	
		Tr workingRow = (Tr) XmlUtils.deepCopy(templateRow);
		Map<String,Text> textElements = getPlaceHolders(workingRow,TABLE_TAG);
		for (Map.Entry<String,Text> entry : textElements.entrySet()) {
		    Text text = entry.getValue();
		    String sufix = entry.getKey().substring(prefix.length()+1);
			text.setValue( getStringValue(sufix, object));
		}
 
		reviewtable.getContent().add(i++,workingRow);
		
	  }		
	}
	
	private Tbl getTemplateTable(List<Object> tables, String templateKey) throws Docx4JException, JAXBException {
		for (Iterator<Object> iterator = tables.iterator(); iterator.hasNext();) {
			Object tbl = iterator.next();
			List<?> textElements = getAllElementFromObject(tbl, Text.class);
			for (Object text : textElements) {
				Text textElement = (Text) text;
				if (textElement.getValue() != null && textElement.getValue().equals(templateKey))
					return (Tbl) tbl;
			}
		}
		return null;
	}
	
	

	private void processTable(Tbl tempTable, Map<String, Object> context) throws Docx4JException,
			JAXBException {
		
		   List<Object> rows = getAllElementFromObject(tempTable, Tr.class);
			Tr templateRow =null;
			int i=0;
			// first row is header, second row is content
			if (rows.size() >1) {
				// this is our template row
				templateRow = (Tr) rows.get(1);
				i=1;
			}
			else if (rows.size()>0)
			{
				templateRow = (Tr) rows.get(0);
			}
			else return;
			
			
		    String prefix = getCollectionPrefix(templateRow, TABLE_TAG);
			if (prefix == null) return;
			if (prefix.indexOf(".")>0) prefix = prefix.substring(0,prefix.indexOf("."));
		    
			Object collection = context.get(prefix);
			if (collection instanceof Collection)
			{
               addRowsToTable(tempTable, templateRow, (Collection)collection,prefix ,i);
			  // 4. remove the template row
			  tempTable.getContent().remove(templateRow);
			}
		}

	private Map<String,Text> getPlaceHolders(
			Tr templateRow,  String tag) {
		
		List<Object> texts = getAllElementFromObject(templateRow, Text.class);
		
		Map<String,Text> result = new HashMap<String,Text>();
		for (int i =0;i<texts.size();i++)
		{
			Text textElement = (Text) texts.get(i);
		
		     String value = textElement.getValue();
		     if (value.contains(tag.substring(0,1))) {
		    ResultPlaceholder placeHolder = getAssociatedValues(texts,i,true,TABLE_TAG);
			if (placeHolder!= null)
			{		
			   result.put(placeHolder.result,textElement);
			}	
		}
		}
		return result;
	}
	
	private String getCollectionPrefix(Tr templateRow,  String tag) {
		
		List<Object> texts = getAllElementFromObject(templateRow, Text.class);
		
		String result = null;
		for (int i =0;i<texts.size();i++)
		{
			Text textElement = (Text) texts.get(i);
		
		     String value = textElement.getValue();
		     if (value.contains(tag.substring(0,1))) {
		    ResultPlaceholder placeHolder = getAssociatedValues(texts,i,false,TABLE_TAG);
			if (placeHolder!= null)
			{		
			   return placeHolder.result.replace(tag, "").replace(END_TAG, "");
			}	
		}
		}
		return result;
	}

	
	

}

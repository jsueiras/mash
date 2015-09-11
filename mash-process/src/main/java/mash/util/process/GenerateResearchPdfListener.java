package mash.util.process;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.el.Expression;
import org.activiti.engine.task.Attachment;

public class GenerateResearchPdfListener implements TaskListener {
	
	
	private Expression varName;

	
	public Expression getVarName() {
		return varName;
	}
	
	public void setVarName(Expression varName) {
		this.varName = varName;
	}

	@Override
	public void notify(DelegateTask task) {
		
		//attachDocument(task);
	
			  
	}

	private void attachDocument(DelegateTask task,String title,String extension,ByteArrayInputStream content) {
		TaskService taskService = task.getExecution().getEngineServices().getTaskService();
	
		
		
		Attachment attachment = taskService.createAttachment(extension, task.getId(), task.getProcessInstanceId(), 
			          title +"." + extension, title,  content);
	}
	
	 private ByteArrayInputStream generatePdf(DelegateTask task)
	 {
	   
		Map<String,Object> processContext  = (Map<String, Object>) task.getExecution().getVariable(getVarName().getExpressionText());
		
		 
		return null;
		 
	 }
	 
	 static class  DataTransformer{
		 
		 static Map<String,Object> transform(Map<String,Object> data)
		 {
			 Map<String,Object> results = new HashMap<String, Object>();
			 
			 for (Map.Entry<String, Object> e : data.entrySet()) {
			    String key = e.getKey();
			    int indexOf = key.indexOf(".");
				if (indexOf>0)
			    {
			    	String prefix = key.substring(0,indexOf);
			    	String sufix =  key.substring(indexOf +1);
			    	groupData(prefix,sufix,e,results);
			    }	
			    else
			    {
			    	results.put(key, e.getValue());
			    }	
			 } 
			 
			 return results;
			 
			 
		 }

		private static void groupData(String prefix, String sufix,
				Entry<String, Object> e, Map<String, Object> results) {
			
			String index  = prefix.replaceAll("\\D+","");
			if (index.length() ==0)
			{		
			   Map<String, Object> innerMap = (Map<String, Object>) results.get(prefix);
			   if (innerMap == null) 
			   {	  
				   innerMap = new HashMap<String, Object>();
				   results.put(prefix, innerMap);
			   }
			   innerMap.put(sufix, e.getValue());
			}	
			else
			{
				int i = new Integer(index) -1;
				   String collectionName = prefix.substring(0,prefix.length()-index.length());
					
				  List<Map<String, Object>> innerCollection =  (List<Map<String, Object>>) results.get(collectionName);
				   if (innerCollection == null) 
				   {	  
					   innerCollection = new ArrayList<Map<String,Object>>();
					   results.put(collectionName, innerCollection);
				   }
				   Map<String, Object> innerMap = null;
				   if (i < innerCollection.size()) 
				   {	   
					   innerMap = innerCollection.get(i);
				   }
				   else 	   
				   for (int j = 0; j < i+2-innerCollection.size(); j++) {
					     innerCollection.add(null);
				   }
				   if (innerMap == null) 
				   {	  
					   innerMap = new HashMap<String, Object>();
					   innerCollection.set(i, innerMap);
				   }
				  
				  innerMap.put(sufix, e.getValue());
				
			}		
						
		}
	 } 
	 
	 
	 
	 
	


}

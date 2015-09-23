package mash.util.process;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mash.document.utils.DocumentRenderer;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.el.Expression;
import org.activiti.engine.task.Attachment;

public class GenerateDocumentListener implements TaskListener,JavaDelegate {
	
	private Expression docName;
	
	private Expression varName;
	
	private Expression templateName;
	
	private Expression parentIdVar;
	
	private Expression dateVar;

	
	
	
	public Expression getParentIdVar() {
		return parentIdVar;
	}

	public void setParentIdVar(Expression parentIdVar) {
		this.parentIdVar = parentIdVar;
	}

	public Expression getType() {
		return docName;
	}
	
	public void setType(Expression type) {
		this.docName = type;
	}
	
	public Expression getTemplateName() {
		return templateName;
	}
	
	public void setTemplateName(Expression templateName) {
		this.templateName = templateName;
	}

	
	public Expression getVarName() {
		return varName;
	}
	
	public void setVarName(Expression varName) {
		this.varName = varName;
	}

	@Override
	public void notify(DelegateTask task) {
		
		addAttachment(task.getExecution(),task.getId());
		  
	}
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		addAttachment(execution,null);	
	} 
	 

	private void addAttachment(DelegateExecution execution,String taskId) {
		
		String fullName = docName.getExpressionText();
		int indexOf = fullName.indexOf(".");
		String name = fullName.substring(0,indexOf);
		String extension = fullName.substring(indexOf +1);
		
		 ByteArrayOutputStream out = generateDocument(execution);
		
		
		
		attachDocument(execution, new ByteArrayInputStream(out.toByteArray()),taskId);
	}

	private void attachDocument(DelegateExecution execution,ByteArrayInputStream content,String taskId) {
		String fullName = docName.getExpressionText();
		int indexOf = fullName.indexOf(".");
		String name = fullName.substring(0,indexOf);
		String extension = fullName.substring(indexOf +1);
		String mime = "application/" + extension;
	
		
		TaskService taskService = execution.getEngineServices().getTaskService();
		removePreviousAttachment(execution, taskService);
		Attachment attachment = taskService.createAttachment(mime, taskId, execution.getProcessInstanceId(), 
				 fullName, name,  content);
		
		}

	private void removePreviousAttachment(DelegateExecution execution,
			TaskService taskService) {
		 List<Attachment> attachements = taskService.getProcessInstanceAttachments(execution.getProcessInstanceId());
		 for (Attachment attachment : attachements) {
			if (attachment.getName().equals(docName.getExpressionText()))
			{	
			    taskService.deleteAttachment(attachment.getId());
			    break;
			}    
		}
	}
	
	

	
	 private ByteArrayOutputStream generateDocument(DelegateExecution execution)
	 {
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
	   
		Map<String,Object> processContext  = getContext(execution);
		
		addDate(processContext);
		DocumentRenderer renderer = new DocumentRenderer();
		
		String fullName = docName.getExpressionText();
		int indexOf = fullName.indexOf(".");
		String extension = fullName.substring(indexOf +1);
		
		if ("pdf".equals(extension))
		{
			   renderer.writeAsPdf(getTemplateName().getExpressionText(), DataTransformer.transform(processContext), out);
		}	
		else
		{	
		   renderer.writeAsWord(getTemplateName().getExpressionText(), DataTransformer.transform(processContext), out);
		} 
		return out;
		 
	 }

	private void addDate(Map<String, Object> processContext) {
		String dateName = "date";
		if (dateVar!=null)
		{
			dateName= dateVar.getExpressionText();
		}		
		processContext.put(dateName, new Date());
		
	}

	private Map<String, Object> getContext(DelegateExecution execution) {
		if (parentIdVar != null && parentIdVar.getExpressionText().length() >0)
		{		
			String parentId = (String) execution.getVariable(parentIdVar.getExpressionText());
			return (Map<String, Object>) execution.getEngineServices().getRuntimeService().getVariable(parentId, varName.getExpressionText());
		}			
		else
		return (Map<String, Object>) execution.getVariable(varName.getExpressionText());
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

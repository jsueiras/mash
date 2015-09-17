package mash.util.process;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
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

public class AddAttachmentListener implements TaskListener, JavaDelegate {
	
	
	

	@Override
	public void notify(DelegateTask task) {
		
		
		
		attachDocument(task,"Sample","txt", new ByteArrayInputStream("hola".getBytes()));
	
			  
	}

	private void attachDocument(DelegateTask task,String title,String extension,ByteArrayInputStream content) {
		TaskService taskService = task.getExecution().getEngineServices().getTaskService();
	
		
		
		Attachment attachment = taskService.createAttachment(extension, task.getId(), task.getProcessInstanceId(), 
			          title +"." + extension, title,  content);
	}

	@Override
	public void execute(DelegateExecution execution) throws Exception {
	
		  TaskService taskService = execution.getEngineServices().getTaskService();
	
		
		
		Attachment attachment = taskService.createAttachment(".txt", null, execution.getProcessInstanceId(), 
			          "test.txt" , "Test",  new ByteArrayInputStream("hola".getBytes()));

		
	}
	
 
	


}

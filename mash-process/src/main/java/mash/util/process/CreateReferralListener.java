package mash.util.process;

import java.io.ByteArrayInputStream;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.el.Expression;
import org.activiti.engine.task.Attachment;

import com.mash.model.catalog.Referral;

public class CreateReferralListener implements ExecutionListener {
	
	
	

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
	   Referral referral  = new Referral();
		
	    String comment = (String) execution.getVariable("comments");
	    System.out.println ("Another comment " + comment);
	    if (comment!= null)
	    {	
	      referral.getComments().add(comment);
	      execution.setVariable("referral", referral);
	      System.out.println ("Another comment " + referral.getComments().size());
	    }  
	}
	


}

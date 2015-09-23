package mash.util.process;

import java.io.ByteArrayInputStream;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.el.Expression;
import org.activiti.engine.task.Attachment;

import com.mash.model.catalog.Referral;

public class StoreReferralService implements JavaDelegate {
	
	
	

	@Override
	public void execute(DelegateExecution execution) throws Exception {
	Referral referral = (Referral) execution.getVariable("referral");
		
		if (referral==null) referral = new Referral();
		
	    String comment = (String)  execution.getVariable("comments");
	     System.out.println ("Another comment " + comment);
	 	
	   
	    
	    if (comment!= null)
	    {	
	      referral.getComments().add(comment);
	    
	      System.out.println ("Another comment " + referral.getComments().size());
	    }  
	    
	    execution.setVariable("referral", referral);
			  
	
	}
	


}

package mash.util.process;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.el.Expression;
import org.activiti.engine.task.Attachment;

import com.mash.model.catalog.Referral;

public class FormToMapListener implements ExecutionListener,TaskListener,JavaDelegate {
	
	
	private Expression varName;

	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
	   storeVariables(execution);	 
	}

	private void storeVariables(DelegateExecution execution) {
		   Set<String> names =  new HashSet<String>( execution.getVariableNames());
		   names.remove(varName.getExpressionText());
		   Map<String,Object> previous = (Map<String, Object>) execution.getVariable(varName.getExpressionText());
		   
		   Map<String,Object> newVars = execution.getVariables(execution.getVariableNames());
		   
		   if (previous!=null)  newVars.putAll(previous); 
		   execution.setVariable(varName.getExpressionText(),newVars);
		  System.out.println(newVars.size()); 
		  
	}
	
	public Expression getVarName() {
		return varName;
	}
	
	public void setVarName(Expression varName) {
		this.varName = varName;
	}

	@Override
	public void notify(DelegateTask delegateTask) {
		storeVariables(delegateTask.getExecution());
	}

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		storeVariables(execution);
		
	}
	


}

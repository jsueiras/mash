package mash.util.process;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.el.Expression;
import org.activiti.explorer.ui.form.custom.TriageSearchValue;
import org.activiti.explorer.ui.form.custom.TriageSearchValue.TriagePersonSummary;

public class ExtractTriageValueListener implements ExecutionListener,TaskListener,JavaDelegate {
	
	
	private Expression varName;
	
	private Expression varMainPerson;
	
	private Expression varMainPersonName;
	
	private Expression varOthers;
	 

	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
	   processTriageSearch(execution);	 
	}

	private void processTriageSearch(DelegateExecution execution) {
		   
		   String triageValue = (String) execution.getVariable(varName.getExpressionText());
		   
		   TriageSearchValue triageObject = TriageSearchValue.stringToObject(triageValue);
		   
		   if (triageObject!=null &&  triageObject.getSubjects()!=null && triageObject.getSubjects().size()>0)
		   {
			   TriagePersonSummary mainPerson = triageObject.getSubjects().remove(0);
			   List<TriagePersonSummary> others = triageObject.getSubjects();
			   execution.setVariable(varMainPerson.getExpressionText() ,mainPerson);
			   execution.setVariable(varOthers.getExpressionText(), others);
			   execution.setVariable(varMainPersonName.getExpressionText(), String.format("%s %s", mainPerson.getFirstName(),mainPerson.getLastName()));
		   }
		  
			   
		  
		  
	}
	
	public Expression getVarName() {
		return varName;
	}
	
	public void setVarName(Expression varName) {
		this.varName = varName;
	}

	@Override
	public void notify(DelegateTask delegateTask) {
		processTriageSearch(delegateTask.getExecution());
	}

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		processTriageSearch(execution);
		
	}

	public Expression getVarMainPerson() {
		return varMainPerson;
	}

	public void setVarMainPerson(Expression varMainPerson) {
		this.varMainPerson = varMainPerson;
	}

	public Expression getVarOthers() {
		return varOthers;
	}

	public void setVarOthers(Expression varOthers) {
		this.varOthers = varOthers;
	}

	public Expression getVarMainPersonName() {
		return varMainPersonName;
	}

	public void setVarMainPersonName(Expression varMainPersonName) {
		this.varMainPersonName = varMainPersonName;
	}
	
	
	
	


}

package mash.util.process;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.el.Expression;
import org.activiti.engine.task.Attachment;

import com.mash.model.catalog.Referral;

public class RetrieveMapFromProcess implements ExecutionListener,TaskListener,JavaDelegate {
	
	
	private Expression varName;

	private Expression varprocessId;
	
	public Expression getVarName() {
		return varName;
	}
	
	public void setVarName(Expression varName) {
		this.varName = varName;
	}

	
	public Expression getVarprocessId() {
		return varprocessId;
	}

	public void setVarprocessId(Expression varprocessId) {
		this.varprocessId = varprocessId;
	}

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
	   retrieveVariable(execution);	 
	}

	private void retrieveVariable(DelegateExecution execution) {
		  
		 String processId = (String) execution.getVariable(varprocessId.getExpressionText());
		 HistoryService historyService = execution.getEngineServices().getHistoryService();
		List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().executionId(processId).list();
		 for (HistoricVariableInstance hVar : list) {
			execution.setVariable(hVar.getVariableName(),  hVar.getValue());
			 
		}
		
		 if (list!=null) System.out.println(list.size());
	}
	

	@Override
	public void notify(DelegateTask delegateTask) {
		retrieveVariable(delegateTask.getExecution());
	}

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		retrieveVariable(execution);
		
	}
	


}

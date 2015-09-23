package mash.util.process;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.el.Expression;
import org.activiti.engine.task.Attachment;

import com.mash.model.catalog.Referral;

public class ResearchLogListener implements TaskListener,JavaDelegate {
	
	private Expression agencyVar; 
	private Expression variableId;
    private Expression parentIdVar;
    private Expression eventlogVar;
    private Expression init;
    private Expression mapStoreVar;
    
	public Expression getMapStoreVar() {
		return mapStoreVar;
	}

	public void setMapStoreVar(Expression mapStoreVar) {
		this.mapStoreVar = mapStoreVar;
	}    
   
    
	
	public Expression getAgencyVar() {
		return agencyVar;
	}

	public void setAgencyVar(Expression agencyVar) {
		this.agencyVar = agencyVar;
	}

	public Expression getVariableId() {
		return variableId;
	}

	public void setVariableId(Expression variableId) {
		this.variableId = variableId;
	}

	public Expression getInit() {
		return init;
	}

	public void setInit(Expression init) {
		this.init = init;
	}

	public Expression getVariableIds() {
		return variableId;
	}

	public void setVariableIds(Expression variableIds) {
		this.variableId = variableIds;
	}

	public Expression getParentIdVar() {
		return parentIdVar;
	}

	public void setParentIdVar(Expression parentIdVar) {
		this.parentIdVar = parentIdVar;
	}

	public Expression getEventlogVar() {
		return eventlogVar;
	}

	public void setEventlogVar(Expression eventlogVar) {
		this.eventlogVar = eventlogVar;
	}

	

	

	private void storeEvent(DelegateExecution execution,String agency,String value) {
	    Map<String,Object> mainMap ;
	    Map<String,String> log ;
		if  (parentIdVar!= null && parentIdVar.getExpressionText().trim().length() >0)
		{
			RuntimeService rt = execution.getEngineServices().getRuntimeService();
			String parentId = (String) execution.getVariable(this.parentIdVar.getExpressionText());
			
			mainMap = (Map<String, Object>) rt.getVariable(parentId,mapStoreVar.getExpressionText());
		}	
		else
		{
			mainMap = (Map<String, Object>) execution.getVariable(mapStoreVar.getExpressionText());
			
		}		
	    log = (Map<String, String>) mainMap.get(eventlogVar.getExpressionText()); 
		log.put(agency, value);
	}

	
	
	

	@Override
	public void notify(DelegateTask delegateTask) {
		if ("true".equals( init.getExpressionText()))
		{
			initResearchMap(delegateTask.getExecution());
		}
		else
		{		
		    storeEvent(delegateTask.getExecution(),getAgency(delegateTask),(String) delegateTask.getVariable(variableId.getExpressionText()));
		}    
	}

	private String getAgency(DelegateTask delegateTask) {
		if (delegateTask.getCandidates().iterator().hasNext())
		{		
		   String groupId = delegateTask.getCandidates().iterator().next().getGroupId();
		   Group group = delegateTask.getExecution().getEngineServices().getIdentityService().createGroupQuery().groupId(groupId).list().get(0);
		   return group.getName();
		}   
		return getAgency(delegateTask.getExecution());
	}
	
	private String getAgency(DelegateExecution execution) {
			
		   String groupId = (String) execution.getVariable(agencyVar.getExpressionText());
		   Group group = execution.getEngineServices().getIdentityService().createGroupQuery().groupId(groupId).list().get(0);
		   return group.getName();
	  
	}

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		if ("true".equals( init.getExpressionText()))
		{
			initResearchMap(execution);
		}
		else
		{
			storeEvent(execution,getAgency(execution),(String) execution.getVariable(variableId.getExpressionText()));
		}
	}

	private void initResearchMap(DelegateExecution execution) {
		Map<String,String> log = new HashMap<String,String>();
		//execution.setVariable(eventlogVar.getExpressionText(), log);
		
		Map<String,Object> mainMap = (Map<String, Object>) execution.getVariable(mapStoreVar.getExpressionText());
		mainMap.put(eventlogVar.getExpressionText(), log);
		
	}


	
	
			
		
		
	
	


}

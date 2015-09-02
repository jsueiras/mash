package mash.util.process;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.identity.Group;

public class SetAgenciesServiceTask implements JavaDelegate{

	 
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		setAgencies(execution);		
		setDueDate(execution);
		
	}
	
	private void setDueDate(DelegateExecution execution) {
		Object varP = execution.getVariable("priority");
		if (varP!=null && ((String) varP).trim().length() >0 )
		{
		Date date = getDueDate(varP);
	    execution.setVariable("dueDate", date);
		}
		
	}

	private Date getDueDate(Object varP) {
		Priority priority =  Priority.valueOf((String) varP);
		
		Calendar cal = Calendar.getInstance(); // creates calendar
		cal.setTime(new Date());
	    cal.add(Calendar.HOUR_OF_DAY, priority.getHours()); // adds one hour
	    Date date = cal.getTime();
		return date;
	}

	private void setAgencies(DelegateExecution execution) {
		List<String> agencies = new ArrayList<String>();
	   IdentityService service = execution.getEngineServices().getIdentityService();
		List<Group> groups = service.createGroupQuery().list();
	    for (Group group: groups)
	    {
			if (group.getId().startsWith("r_"))
			{
				Boolean isRole = (Boolean) execution.getVariable(group.getId());
				if (isRole)
				{
					agencies.add(group.getId());
				}		
				
			}
		}
		
		if (agencies.size() >0)
		{
			execution.setVariable("multiagency", true);	
			agencies.add(null);
		}
		else
		{
			execution.setVariable("multiagency", false);	
		}	
		
		execution.setVariable("agencies", agencies);
	}
	
	 enum Priority {
		 
		 Red(2),Ambar(12);
		
		 int hours ;
		 Priority(int n)
		 {
			hours = n;
		 }
		 
		 public int getHours() {
			return hours;
		}
		
	}

}

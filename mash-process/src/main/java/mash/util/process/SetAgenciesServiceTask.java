package mash.util.process;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class SetAgenciesServiceTask implements JavaDelegate{

	private static final int AGENCY_NUM = 3;
	 
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		List<String> agencies = new ArrayList<String>();
		for (int i = 1; i <= AGENCY_NUM ; i++) {
			String val = (String) execution.getVariable("agency" + i);
			if (val!= null  && val.trim().length() >0)
			agencies.add(val);
		}
		if (agencies.size() >0)
		{
			execution.setVariable("multiagency", true);	
			agencies.add(null);
		}
		
		execution.setVariable("agencies", agencies);		
		
		
	}

}

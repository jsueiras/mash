package mash.util.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;

public class MyAgencyServiceTask implements JavaDelegate{

	private static final int AGENCY_NUM = 3;
	 
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		String researchGroup = null;
		String userId = (String) execution.getVariable("initiator");	
		IdentityService service = execution.getEngineServices().getIdentityService();
		List<Group> groups = service.createGroupQuery().groupMember(userId).list();
	    for (Group group: groups)
	    {
			if (group.getId().startsWith("t_"))
			{
				researchGroup = group.getId().replace("t_", "d_");
				break;
			}
		}
		execution.setVariable("myAgencyDecision", researchGroup);
		
	}

}

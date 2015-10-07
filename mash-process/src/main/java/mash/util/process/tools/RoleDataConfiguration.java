package mash.util.process.tools;
/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.util.IoUtil;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.task.Task;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


/**
 * @author Joram Barrez
 */

@Component  
public class RoleDataConfiguration {
  
  protected static final Logger LOGGER = LoggerFactory.getLogger(RoleDataConfiguration.class);

  @Autowired
  protected IdentityService identityService;
  
  
  protected RepositoryService repositoryService;
  
  
  protected RuntimeService runtimeService;
  
  
  protected TaskService taskService;
  
  
  protected ManagementService managementService;
  
  
  protected ProcessEngineConfigurationImpl processEngineConfiguration;
  
  @Autowired
  protected Environment environment;

private ProcessEngine processEngine;

private static final String[] assignmentIds = new String[] {"children_stf", "children_stk", "adults_stf", "adults_stk", "mentalhealth_north","mentalhealth_south","police"};

private static final String[] otherAssignmentIds = new String[]{"probation","nHS"};
private static final String[] assignmentGroups = new String[] {"Children Staffordshire", "Children Stoke", "Adults Staffordshire", "Adults Stoke", "Mental Health North","Mental Health South","Police"};
private static final String[] otherAssignmentGroups= new String[]{"Probation","NHS"};
 
   
  
  public RoleDataConfiguration(ProcessEngine processEngine) {
     this.processEngine = processEngine;
     this.identityService = processEngine.getProcessEngineConfiguration().getIdentityService();
     this.managementService = processEngine.getProcessEngineConfiguration().getManagementService();
     
}

protected void initGroups() {
	    int i =0;
    for (String groupName : assignmentGroups) {
      String groupId = assignmentIds[i];
      createGroup("t_" + groupId,"Triage " + groupName ,"assignment");
      createGroup("r_" + groupId,"Research " +groupName ,"assignment");
      createGroup("d_" + groupId, "Decision " +groupName ,"assignment");
      i++;
    }
    

  }
  
  protected void removeGroups() {
		    for (String groupId : assignmentIds) {
	   
		    	deleteGroup("t_" + groupId);
		    	deleteGroup("r_" + groupId);
		    	deleteGroup("d_" + groupId);
	     
	    }
	    

	  }
  protected void deleteGroup(String groupId) {
	    if (identityService.createGroupQuery().groupId(groupId).count() > 0) {
	     
	      identityService.deleteGroup(groupId);
	    }
	  }

  
  protected void createGroup(String groupId, String groupName ,String type) {
    if (identityService.createGroupQuery().groupId(groupId).count() == 0) {
      Group newGroup = identityService.newGroup(groupId);
      newGroup.setName(groupName);
      newGroup.setType(type);
      identityService.saveGroup(newGroup);
    }
  }

  protected void initUsers() {
//    createUser("kermit", "Kermit", "The Frog", "kermit", "kermit@activiti.org", 
//            "org/activiti/explorer/images/kermit.jpg",
//            Arrays.asList("management", "sales", "marketing", "engineering", "user", "admin"),
//            Arrays.asList("birthDate", "10-10-1955", "jobTitle", "Muppet", "location", "Hollywoord",
//                          "phone", "+123456789", "twitterName", "alfresco", "skype", "activiti_kermit_frog"));
//    
//    createUser("gonzo", "Gonzo", "The Great", "gonzo", "gonzo@activiti.org", 
//            "org/activiti/explorer/images/gonzo.jpg",
//            Arrays.asList("management", "sales", "marketing", "user"),
//            null);
//    createUser("fozzie", "Fozzie", "Bear", "fozzie", "fozzie@activiti.org", 
//            "org/activiti/explorer/images/fozzie.jpg",
//            Arrays.asList("marketing", "engineering", "user"),
//            null);
    
    for (String id : assignmentIds) {
    	String tempid = "t_" + id;
       createUser(tempid,tempid, null, "password",null,null, Arrays.asList(tempid),null);
       identityService.createMembership("kermit", tempid);
        tempid = "r_" + id;
    	 createUser(tempid,tempid, null, "password",null,null, Arrays.asList(tempid),null);
    	 identityService.createMembership("kermit", tempid);
    	 tempid = "d_" + id;
    	 createUser(tempid,tempid, null, "password",null,null, Arrays.asList(tempid),null);
    	 identityService.createMembership("kermit", tempid);
         
    }
  }
  
  protected void createUser(String userId, String firstName, String lastName, String password, 
          String email, String imageResource, List<String> groups, List<String> userInfo) {
    
    if (identityService.createUserQuery().userId(userId).count() == 0) {
    	
      // Following data can already be set by demo setup script
      
      User user = identityService.newUser(userId);
      user.setFirstName(firstName);
      user.setLastName(lastName);
      user.setPassword(password);
      user.setEmail(email);
      identityService.saveUser(user);
      
      if (groups != null) {
        for (String group : groups) {
          identityService.createMembership(userId, group);
        }
      }
    }
    
    // Following data is not set by demo setup script
      
    // image
    if (imageResource != null) {
      byte[] pictureBytes = IoUtil.readInputStream(this.getClass().getClassLoader().getResourceAsStream(imageResource), null);
      Picture picture = new Picture(pictureBytes, "image/jpeg");
      identityService.setUserPicture(userId, picture);
    }
      
    // user info
    if (userInfo != null) {
      for(int i=0; i<userInfo.size(); i+=2) {
        identityService.setUserInfo(userId, userInfo.get(i), userInfo.get(i+1));
      }
    }
    
  }
  
  protected void initProcessDefinitions() {
    
    String deploymentName = "Demo processes";
    List<Deployment> deploymentList = repositoryService.createDeploymentQuery().deploymentName(deploymentName).list();
    
    if (deploymentList == null || deploymentList.isEmpty()) {
      repositoryService.createDeployment()
        .name(deploymentName)
        .addClasspathResource("org/activiti/explorer/demo/process/createTimersProcess.bpmn20.xml")
        .addClasspathResource("org/activiti/explorer/demo/process/VacationRequest.bpmn20.xml")
        .addClasspathResource("org/activiti/explorer/demo/process/VacationRequest.png")
        .addClasspathResource("org/activiti/explorer/demo/process/FixSystemFailureProcess.bpmn20.xml")
        .addClasspathResource("org/activiti/explorer/demo/process/FixSystemFailureProcess.png")
        .addClasspathResource("org/activiti/explorer/demo/process/simple-approval.bpmn20.xml")
        .addClasspathResource("org/activiti/explorer/demo/process/Helpdesk.bpmn20.xml")
        .addClasspathResource("org/activiti/explorer/demo/process/Helpdesk.png")
        .addClasspathResource("org/activiti/explorer/demo/process/reviewSalesLead.bpmn20.xml")
        .deploy();
    }
    
    String reportDeploymentName = "Demo reports";
    deploymentList = repositoryService.createDeploymentQuery().deploymentName(reportDeploymentName).list();
    if (deploymentList == null || deploymentList.isEmpty()) {
      repositoryService.createDeployment()
        .name(reportDeploymentName)
        .addClasspathResource("org/activiti/explorer/demo/process/reports/taskDurationForProcessDefinition.bpmn20.xml")
        .addClasspathResource("org/activiti/explorer/demo/process/reports/processInstanceOverview.bpmn20.xml")
        .addClasspathResource("org/activiti/explorer/demo/process/reports/helpdeskFirstLineVsEscalated.bpmn20.xml")
        .addClasspathResource("org/activiti/explorer/demo/process/reports/employeeProductivity.bpmn20.xml")
        .deploy();
    }
    
  }


 
  
 
public IdentityService getIdentityService() {
	return identityService;
}

public void setIdentityService(IdentityService identityService) {
	this.identityService = identityService;
}

public RepositoryService getRepositoryService() {
	return repositoryService;
}

public void setRepositoryService(RepositoryService repositoryService) {
	this.repositoryService = repositoryService;
}

public RuntimeService getRuntimeService() {
	return runtimeService;
}

public void setRuntimeService(RuntimeService runtimeService) {
	this.runtimeService = runtimeService;
}

public TaskService getTaskService() {
	return taskService;
}

public void setTaskService(TaskService taskService) {
	this.taskService = taskService;
}

public ManagementService getManagementService() {
	return managementService;
}

public void setManagementService(ManagementService managementService) {
	this.managementService = managementService;
}

public ProcessEngineConfigurationImpl getProcessEngineConfiguration() {
	return processEngineConfiguration;
}

public void setProcessEngineConfiguration(
		ProcessEngineConfigurationImpl processEngineConfiguration) {
	this.processEngineConfiguration = processEngineConfiguration;
}
  
public static void main(String[] args) {
	  
	System.out.println("start");
	  ApplicationContext context = new ClassPathXmlApplicationContext("activiti-custom-context.xml");
	  
	
	 ProcessEngine processEngine = (ProcessEngine) context.getBean("processEngine");
		System.out.println("cofig loaded");

	  
	  RoleDataConfiguration data = new RoleDataConfiguration(processEngine);
	  
	  data.initGroups();
	  data.initUsers();
	  
	  System.out.println("end");
	
}

  
  
}

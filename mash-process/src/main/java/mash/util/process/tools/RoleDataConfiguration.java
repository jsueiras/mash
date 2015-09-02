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
  
  @Autowired
  protected RepositoryService repositoryService;
  
  @Autowired
  protected RuntimeService runtimeService;
  
  @Autowired
  protected TaskService taskService;
  
  @Autowired
  protected ManagementService managementService;
  
  @Autowired
  protected ProcessEngineConfigurationImpl processEngineConfiguration;
  
  @Autowired
  protected Environment environment;

private static final String[] assignmentIds = new String[] {"children_stf", "children_stk", "adults_stf", "adults_stk", "mentalhealth_north","mentalhealth_south","police"};

private static final String[] otherAssignmentIds = new String[]{"probation","nHS"};
private static final String[] assignmentGroups = new String[] {"Children Staffordshire", "Children Stoke", "Adults Staffordshire", "Adults Stoke", "Mental Health North","Mental Health South","Police"};
private static final String[] otherAssignmentGroups= new String[]{"Probation","NHS"};
 
 
  
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

  protected void generateReportData() {
    // Report data is generated in background thread
      
    Thread thread = new Thread(new Runnable() {
      
      public void run() {
        
        // We need to temporarily disable the job executor or it would interfere with the process execution
        processEngineConfiguration.getJobExecutor().shutdown();
        
        Random random = new Random();
        
        Date now = new Date(new Date().getTime() - (24 * 60 * 60 * 1000));
        processEngineConfiguration.getClock().setCurrentTime(now);
        
        for (int i=0; i<50; i++) {
          
          if (random.nextBoolean()) {
            runtimeService.startProcessInstanceByKey("fixSystemFailure");
          }
          
          if (random.nextBoolean()) {
            identityService.setAuthenticatedUserId("kermit");
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("customerName", "testCustomer");
            variables.put("details", "Looks very interesting!");
            variables.put("notEnoughInformation", false);
            runtimeService.startProcessInstanceByKey("reviewSaledLead", variables);
          }
          
          if (random.nextBoolean()) {
            runtimeService.startProcessInstanceByKey("escalationExample");
          }
          
          if (random.nextInt(100) < 20) {
            now = new Date(now.getTime() - ((24 * 60 * 60 * 1000) - (60 * 60 * 1000)));
            processEngineConfiguration.getClock().setCurrentTime(now);
          }
        }
        
        List<Job> jobs = managementService.createJobQuery().list();
        for (int i=0; i<jobs.size()/2; i++) {
          processEngineConfiguration.getClock().setCurrentTime(jobs.get(i).getDuedate());
          managementService.executeJob(jobs.get(i).getId());
        }
        
        List<Task> tasks = taskService.createTaskQuery().list();
        while (!tasks.isEmpty()) {
          for (Task task : tasks) {
            
            if (task.getAssignee() == null) {
              String assignee = random.nextBoolean() ? "kermit" : "fozzie";
              taskService.claim(task.getId(), assignee);
            }

            processEngineConfiguration.getClock().setCurrentTime(new Date(
                task.getCreateTime().getTime() + random.nextInt(60 * 60 * 1000)));
            
            taskService.complete(task.getId());
          }
          
          tasks = taskService.createTaskQuery().list();
        }

        processEngineConfiguration.getClock().reset();
        
        processEngineConfiguration.getJobExecutor().start();
        LOGGER.info("Demo report data generated");
      }
      
    });
    thread.start();
  }
  
  protected void initModelData() {
    createModelData("Demo model", "This is a demo model", "org/activiti/explorer/demo/model/test.model.json");
  }
  
  protected void createModelData(String name, String description, String jsonFile) {
    List<Model> modelList = repositoryService.createModelQuery().modelName("Demo model").list();
    
    if (modelList == null || modelList.isEmpty()) {
    
      Model model = repositoryService.newModel();
      model.setName(name);
      
      ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
      modelObjectNode.put("name", name);
      modelObjectNode.put("description", description);
      model.setMetaInfo(modelObjectNode.toString());
      
      repositoryService.saveModel(model);
      
      try {
        InputStream svgStream = this.getClass().getClassLoader().getResourceAsStream("org/activiti/explorer/demo/model/test.svg");
        repositoryService.addModelEditorSourceExtra(model.getId(), IOUtils.toByteArray(svgStream));
      } catch(Exception e) {
        LOGGER.warn("Failed to read SVG", e);
      }
      
      try {
        InputStream editorJsonStream = this.getClass().getClassLoader().getResourceAsStream(jsonFile);
        repositoryService.addModelEditorSource(model.getId(), IOUtils.toByteArray(editorJsonStream));
      } catch(Exception e) {
        LOGGER.warn("Failed to read editor JSON", e);
      }
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
	  
	  ApplicationContext context = new ClassPathXmlApplicationContext("activiti-custom-context.xml");
	  
	
	  RoleDataConfiguration data = context.getBean(RoleDataConfiguration.class);

	  
	  data.initGroups();
	  data.initUsers();
	
}

  
  
}

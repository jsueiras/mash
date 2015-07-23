package org.activiti.designer.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileInputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


public class ProcessTestMyProcess {

	private String filename = "C:\\Users\\Cacumen\\workspace\\mash-bpm-def\\src\\main\\resources\\diagrams\\ReferralHealth.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();
	
	TaskService taskService;

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("myProcess.bpmn20.xml",
				new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		taskService = activitiRule.getTaskService();
		//runtimeService.set
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("dept", "sales");
		//variableMap.put("helper", new CalculateDept());
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess", variableMap);
		waitAndCompleteUserTask();
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " "
				+ processInstance.getProcessDefinitionId());
	}
	
	 private void waitAndCompleteUserTask() throws InterruptedException
	    {
	        boolean taskIsAssignedToUser = false;
	        List<Task> taskList = getTaskList();
	        int numberOfUserTask = (int) taskList.size();
	        System.out.println("numberOfUserTask=" + numberOfUserTask);
	        int timeout = 0;
	        while ((numberOfUserTask == 0) && timeout < 1000)
	        {
	            taskList = getTaskList();
	 
	            numberOfUserTask = (int) taskList.size();
	            System.out.println("numberOfUserTask=" + numberOfUserTask);
	 
	            if (numberOfUserTask == 1)
	            {
	                taskIsAssignedToUser = true;
	            }
	            Thread.sleep(300);
	            timeout += 300;
	        }
	 
	        //assertTrue("No task was assigned to user", taskIsAssignedToUser);
	        Task userTask = taskList.get(0);
	        taskService.complete(userTask.getId());
	        //taskService.complete(userTask.getId());
	        Thread.sleep(300);
	    }

	private List<Task> getTaskList() {
		List<Task> taskList;
		taskList = taskService.createTaskQuery().processVariableValueEquals("dept", "sales").list();
		System.out.println(taskList.size());
		return taskList;
	}
	
}
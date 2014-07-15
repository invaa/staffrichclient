package org.staffrichclient.utils;

import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

import net.assino.app1msc.Employee;
import net.assino.app1msc.EmployeeList;
import net.assino.app1msc.Project;
import net.assino.app1msc.ProjectList;
import net.assino.app1msc.ProjectStage;
import net.assino.app1msc.ProjectStageList;
import net.assino.app1msc.Status;
import net.assino.app1msc.Task;
import net.assino.app1msc.TaskList;
import net.assino.app1msc.TaskManager;
import net.assino.app1msc.TaskManagerPortType;

import org.apache.commons.codec.binary.Base64;

public final class WSHandler {

private static TaskManagerPortType getPort() {
		ApplicationSettings as = new ApplicationSettings();
		
		String usernameCP251 = as.getUsername(); //"Замковый Александр";
		
		String utf8username = null;
		
		try {
			utf8username= new String(usernameCP251.getBytes("UTF-8"), "windows-1251");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String password = as.getPassword();
	    	
	    TaskManager tm = new TaskManager();
		TaskManagerPortType port = tm.getTaskManagerSoap();
	    
	     Map<String, Object> requestContext = ((BindingProvider)port).getRequestContext();
		 String authString = utf8username + ":" + password;
		 
		String authStringEnc = new String(Base64.encodeBase64(authString.getBytes()));
		 					 
			requestContext.put(MessageContext.HTTP_REQUEST_HEADERS,
					 Collections.singletonMap("Authorization",
					 Collections.singletonList("Basic " +
							 authStringEnc)));
			return port;
			
		
	}

public static TaskList getTaskList(String nameSubstr) {

  TaskManagerPortType port = getPort();

  EmployeeList foundEmployees = port.findEmployees(nameSubstr);
  Employee employee = null;
  
  if (foundEmployees.getEmployees().size() == 1) {
	  employee = foundEmployees.getEmployees().get(0);
  }
  
  GregorianCalendar gDateBegin = new GregorianCalendar(2014, 04, 01);
  XMLGregorianCalendar xmlGDateBegin = null;
try {
	xmlGDateBegin = DatatypeFactory.newInstance().newXMLGregorianCalendar(gDateBegin);
} catch (DatatypeConfigurationException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

  GregorianCalendar gDateEnd = new GregorianCalendar(2014, 05, 01);
  XMLGregorianCalendar xmlGDateEnd = null;
try {
	xmlGDateEnd = DatatypeFactory.newInstance().newXMLGregorianCalendar(gDateEnd);
} catch (DatatypeConfigurationException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

  
 TaskList taskList = port.getTasks(xmlGDateBegin, xmlGDateEnd, employee);
 
 return taskList;

}

public static TaskList getTaskList(GregorianCalendar gDateBegin, GregorianCalendar gDateEnd, String nameSubstr) {

	  TaskManagerPortType port = getPort();

	  EmployeeList foundEmployees = port.findEmployees(nameSubstr);
	  Employee employee = null;
	  
	  if (foundEmployees.getEmployees().size() == 1) {
		  employee = foundEmployees.getEmployees().get(0);
	  }
	  
	  XMLGregorianCalendar xmlGDateBegin = null;
	try {
		xmlGDateBegin = DatatypeFactory.newInstance().newXMLGregorianCalendar(gDateBegin);
	} catch (DatatypeConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	  XMLGregorianCalendar xmlGDateEnd = null;
	try {
		xmlGDateEnd = DatatypeFactory.newInstance().newXMLGregorianCalendar(gDateEnd);
	} catch (DatatypeConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	  
	 TaskList taskList = port.getTasks(xmlGDateBegin, xmlGDateEnd, employee);
	 
	 return taskList;

	}

public static ProjectList getProjects(String nameSubstr) {

	  TaskManagerPortType port = getPort();

	  return port.findProjects(nameSubstr);

	}

public static Project getProjectById(ProjectList projects, String Id) {
	
	for (Project project : projects.getProjects()) {
		if (project.getId().equals(Id)) { return project; }
	}
	
	return null;
}

public static Task getTaskById(TaskList tasks, String Id) {
	
	for (Task task : tasks.getTasks()) {
		if (task.getId().equals(Id)) { return task; }
	}
	
	return null;
}

public static ProjectStageList getProjectStages(Project project, String nameSubstr) {

	  TaskManagerPortType port = getPort();

	  return port.findProjectsStages(project, nameSubstr);

	}

public static Task createTask(Task task) {
	
	 TaskManagerPortType port = getPort();

	  return port.createTask(task);
}
	
public static EmployeeList getEmployees(String nameSubstr) {

	  TaskManagerPortType port = getPort();

	  return port.findEmployees(nameSubstr);

	}

public static ProjectStage getProjectStageById(
		ProjectStageList projectStageList, String Id) {
	
	for (ProjectStage stage : projectStageList.getProjectStages()) {
		if (stage.getId().equals(Id)) { return stage; }
	}
	
	return null;
}


public static Employee getEmployeeById(EmployeeList employeeList,
		String Id) {

	for (Employee employee : employeeList.getEmployees()) {
		if (employee.getId().equals(Id)) { return employee; }
	}
	
	return null;
}

public static boolean updateTask(Task task) {
	 TaskManagerPortType port = getPort();

	  return port.updateTask(task);
}


public static Employee getCurrentEmployee() {
	 TaskManagerPortType port = getPort();

	  return port.getCurrentUser();
}

public static Status getStatusInProgress() {
	 TaskManagerPortType port = getPort();

	  return port.getStatusInProgress();
}

public static Status getStatusDeployed() {
	 TaskManagerPortType port = getPort();

	  return port.getStatusDeployed();
}

public static Status getStatusDone() {
	 TaskManagerPortType port = getPort();

	  return port.getStatusDone();
}

public static Status getStatusPaused() {
	 TaskManagerPortType port = getPort();

	  return port.getStatusPaused();
}

public static Status getStatusInDevGroup() {
	 TaskManagerPortType port = getPort();

	  return port.getStatusInDevGroup();
}

public static boolean SetStatusInProgress(Task task) {
	 TaskManagerPortType port = getPort();

	  return port.setStatusInProgress(task);
}

public static boolean SetStatusPaused(Task task) {
	 TaskManagerPortType port = getPort();

	  return port.setStatusPaused(task);
}

public static boolean SetStatusDone(Task task) {
	 TaskManagerPortType port = getPort();

	  return port.setStatusDone(task);
}

public static boolean SetStatusDeployed(Task task) {
	 TaskManagerPortType port = getPort();

	  return port.setStatusDeployed(task);
}

public static boolean testConnector() {
	try { 
		TaskManagerPortType port = getPort();
		port.getCurrentUser();
	}
	catch (Exception ex) 
	{
		
		return false;
	}

	  return true;
}


}

package org.staff.utils;

import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

import org.staff.settings.ApplicationSettings;

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

public final class WSHandler {

	public static TaskManagerPortType getPortUsingAppSettings() {
		ApplicationSettings as = new ApplicationSettings();
		String usernameCP251 = as.getUsername().toLowerCase();
		String utf8username = Convertor.getUTF8Representation(usernameCP251);

		String password = as.getPassword();

		return getPort(utf8username, password);

	}

	public static TaskManagerPortType getPort(String utf8username,
			String password) {

		TaskManager tm = new TaskManager();
		TaskManagerPortType port = tm.getTaskManagerSoap();

		setBasicAuthentication(utf8username, password, port);

		return port;
	}

	private static void setBasicAuthentication(String utf8username,
			String password, TaskManagerPortType port) {
		Map<String, Object> requestContext = ((BindingProvider) port)
				.getRequestContext();
		String authString = utf8username + ":" + password;

		requestContext.put(
				MessageContext.HTTP_REQUEST_HEADERS,
				Collections.singletonMap(
						"Authorization",
						Collections.singletonList("Basic "
								+ Convertor.getBase64AuthString(authString))));
	}

	public static TaskList getTaskList(GregorianCalendar gDateBegin,
			GregorianCalendar gDateEnd, String nameSubstr) {

		TaskManagerPortType port = getPortUsingAppSettings();

		EmployeeList foundEmployees = port.findEmployees(nameSubstr);
		Employee employeeToFilter = null;

		if (foundEmployees.getEmployees().size() == 1) {
			employeeToFilter = foundEmployees.getEmployees().get(0);
		}

		TaskList taskList = port.getTasks(
				Convertor.gregorianCalenderToXMLGregorianCalender(gDateBegin),
				Convertor.gregorianCalenderToXMLGregorianCalender(gDateEnd),
				employeeToFilter);

		return taskList;

	}

	public static ProjectList getProjects(String nameSubstr) {

		TaskManagerPortType port = getPortUsingAppSettings();

		return port.findProjects(nameSubstr);

	}

	public static Project getProjectById(ProjectList projects, String Id) {

		for (Project project : projects.getProjects()) {
			if (project.getId().equals(Id)) {
				return project;
			}
		}

		return null;
	}

	public static Task getTaskById(TaskList tasks, String Id) {

		for (Task task : tasks.getTasks()) {
			if (task.getId().equals(Id)) {
				return task;
			}
		}

		return null;
	}

	public static ProjectStageList getProjectStages(Project project,
			String nameSubstr) {

		TaskManagerPortType port = getPortUsingAppSettings();

		return port.findProjectsStages(project, nameSubstr);

	}

	public static Task createTask(Task task) {

		TaskManagerPortType port = getPortUsingAppSettings();

		return port.createTask(task);
	}

	public static EmployeeList getEmployees(String nameSubstr) {

		TaskManagerPortType port = getPortUsingAppSettings();

		return port.findEmployees(nameSubstr);

	}

	public static ProjectStage getProjectStageById(
			ProjectStageList projectStageList, String Id) {

		for (ProjectStage stage : projectStageList.getProjectStages()) {
			if (stage.getId().equals(Id)) {
				return stage;
			}
		}

		return null;
	}

	public static Employee getEmployeeById(EmployeeList employeeList, String Id) {

		for (Employee employee : employeeList.getEmployees()) {
			if (employee.getId().equals(Id)) {
				return employee;
			}
		}

		return null;
	}

	public static boolean updateTask(Task task) {
		TaskManagerPortType port = getPortUsingAppSettings();

		return port.updateTask(task);
	}

	public static Employee getCurrentEmployee() {
		TaskManagerPortType port = getPortUsingAppSettings();

		return port.getCurrentUser();
	}

	public static Status getStatusInProgress() {
		TaskManagerPortType port = getPortUsingAppSettings();

		return port.getStatusInProgress();
	}

	public static Status getStatusDeployed() {
		TaskManagerPortType port = getPortUsingAppSettings();

		return port.getStatusDeployed();
	}

	public static Status getStatusDone() {
		TaskManagerPortType port = getPortUsingAppSettings();

		return port.getStatusDone();
	}

	public static Status getStatusPaused() {
		TaskManagerPortType port = getPortUsingAppSettings();

		return port.getStatusPaused();
	}

	public static Status getStatusInDevGroup() {
		TaskManagerPortType port = getPortUsingAppSettings();

		return port.getStatusInDevGroup();
	}

	public static boolean SetStatusInProgress(Task task) {
		TaskManagerPortType port = getPortUsingAppSettings();

		return port.setStatusInProgress(task);
	}

	public static boolean SetStatusPaused(Task task) {
		TaskManagerPortType port = getPortUsingAppSettings();

		return port.setStatusPaused(task);
	}

	public static boolean SetStatusDone(Task task) {
		TaskManagerPortType port = getPortUsingAppSettings();

		return port.setStatusDone(task);
	}

	public static boolean SetStatusDeployed(Task task) {
		TaskManagerPortType port = getPortUsingAppSettings();

		return port.setStatusDeployed(task);
	}

	public static boolean testConnector() {
		try {
			TaskManagerPortType port = getPortUsingAppSettings();
			port.getCurrentUser();
		} catch (Exception ex) {

			return false;
		}

		return true;
	}

	public static boolean testPortConnection(TaskManagerPortType port) {
		try {
			port.getCurrentUser();
		} catch (Exception ex) {
			return false;
		}

		return true;
	}

	public static boolean getAutoFillDailyReports() {
		try {
			TaskManagerPortType port = getPortUsingAppSettings();
			return port.getAutoFillDailyReports();
		} catch (Exception ex) {

			return false;
		}

	}

	public static boolean setAutoFillDailyReports(boolean value) {
		try {
			TaskManagerPortType port = getPortUsingAppSettings();
			return port.setAutoFillDailyReports(value);
		} catch (Exception ex) {

			return false;
		}

	}

}

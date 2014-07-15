package org.staffrichclient;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import net.assino.app1msc.Employee;
import net.assino.app1msc.EmployeeList;
import net.assino.app1msc.Project;
import net.assino.app1msc.ProjectList;
import net.assino.app1msc.ProjectStage;
import net.assino.app1msc.ProjectStageList;
import net.assino.app1msc.Status;
import net.assino.app1msc.Task;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.DateTime;
import org.staffrichclient.utils.WSHandler;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;

public class TaskDialog {

	private boolean isANewTask = true; 
	
	public boolean isANewTask() {
		return isANewTask;
	}

	public void setANewTask(boolean isANewTask) {
		this.isANewTask = isANewTask;
	}

	private Task task = new Task();
	private MainDialog parentDialog = null;
	
	public void setParentDialog(MainDialog parentDialog) {
		this.parentDialog = parentDialog;
	}
	
	private ProjectList projectList;
	private ProjectStageList projectStageList;
	private EmployeeList employeeList;
	
	protected Shell shlNewTaskeditTask;
	private Text textTaskName;
	private Text textEstimate;
	private Text textExternalId;
	private Text textTaskDescription;
	
	private Combo cmbProjectStages;
	private DateTime dDeadline;
	private Combo cmbInCharge;
	private Combo cmbAssignee;
	private Combo cmbProjects;
	private CLabel lblCurrentAssignee;
	private Combo cmbCurrentAssignee;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TaskDialog window = new TaskDialog();
			window.open();
			window.fillDialog();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		Image small = new Image(display, getClass().getResourceAsStream("logo.gif"));
		shlNewTaskeditTask.setImage(small);
		shlNewTaskeditTask.open();
		shlNewTaskeditTask.layout();
		while (!shlNewTaskeditTask.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlNewTaskeditTask = new Shell();
		shlNewTaskeditTask.addShellListener(new ShellAdapter() {
			@Override
			public void shellActivated(ShellEvent e) {
				//fillDialog();
			}
		});
		
		shlNewTaskeditTask.setSize(607, 383);
		shlNewTaskeditTask.setText(isANewTask ? "New task" : "Edit task");
		shlNewTaskeditTask.setLayout(new GridLayout(5, false));
		
		CLabel lblProject = new CLabel(shlNewTaskeditTask, SWT.NONE);
		lblProject.setText("Project*");
		
		cmbProjects = new Combo(shlNewTaskeditTask, SWT.NONE);
		cmbProjects.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		cmbProjects.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				projectStageList = null;
				int indexSelected = cmbProjects.getSelectionIndex();
				String projectId = (String) cmbProjects.getData(cmbProjects.getItem(indexSelected));
				
				Project project = WSHandler.getProjectById(projectList, projectId);
				
				task.setProject(project);
				
				projectStageList = null;
				projectStageList = WSHandler.getProjectStages(project, "");
				
				String value = "";
				cmbProjectStages.removeAll();
				for (ProjectStage projectStage : projectStageList.getProjectStages()) {
					value = projectStage.getName();
					cmbProjectStages.add(value);
					cmbProjectStages.setData(value, projectStage.getId());
				}
				
				task.setProjectStage(null);
			}
		});
		cmbProjects.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				//update project stages list
			}
		});
		
		CLabel lblDeadline = new CLabel(shlNewTaskeditTask, SWT.NONE);
		lblDeadline.setText("Deadline*");
		//TODO: Validate if is number
		
		dDeadline = new DateTime(shlNewTaskeditTask, SWT.BORDER);
		dDeadline.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GregorianCalendar dateOfDeadline = new GregorianCalendar(dDeadline.getYear(), dDeadline.getMonth(), dDeadline.getDay());
				XMLGregorianCalendar xmlDateOfDeadline = null;
				try {
					xmlDateOfDeadline = DatatypeFactory.newInstance().newXMLGregorianCalendar(dateOfDeadline);
				} catch (DatatypeConfigurationException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
				task.setDeadline(xmlDateOfDeadline);
			}
		});
		dDeadline.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(shlNewTaskeditTask, SWT.NONE);
		
		CLabel lblProjectStage = new CLabel(shlNewTaskeditTask, SWT.NONE);
		lblProjectStage.setText("Project stage");
		
		cmbProjectStages = new Combo(shlNewTaskeditTask, SWT.NONE);
		cmbProjectStages.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int indexSelected = cmbProjectStages.getSelectionIndex();
				if (indexSelected != -1) {
					String projectStageId = (String) cmbProjectStages.getData(cmbProjectStages.getItem(indexSelected));
					ProjectStage projectStage = WSHandler.getProjectStageById(projectStageList, projectStageId);				
					task.setProjectStage(projectStage);
				}
				
			}
		});
		cmbProjectStages.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		CLabel lblTaskName = new CLabel(shlNewTaskeditTask, SWT.NONE);
		lblTaskName.setText("Task name*");
		
		textTaskName = new Text(shlNewTaskeditTask, SWT.BORDER);
		textTaskName.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				task.setName(textTaskName.getText());
			}
		});
		textTaskName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		
		CLabel lblEstimate = new CLabel(shlNewTaskeditTask, SWT.NONE);
		lblEstimate.setText("Estimate");
		
		textEstimate = new Text(shlNewTaskeditTask, SWT.BORDER);
		textEstimate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Float estimateAsFloat = (float) 0.0;
				try {
					estimateAsFloat = Float.valueOf(textEstimate.getText());
				}
				catch (Exception ex) {
					//TODO: handle
				}
				task.setEstimate(estimateAsFloat);
			}
		});
		new Label(shlNewTaskeditTask, SWT.NONE);
		
		CLabel lblExternalid = new CLabel(shlNewTaskeditTask, SWT.NONE);
		lblExternalid.setText("ExternalId");
		
		textExternalId = new Text(shlNewTaskeditTask, SWT.BORDER);
		textExternalId.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				task.setExternalId(textExternalId.getText());
			}
		});
		textExternalId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		CLabel lblInCharge = new CLabel(shlNewTaskeditTask, SWT.NONE);
		lblInCharge.setText("In charge*");
		
		cmbInCharge = new Combo(shlNewTaskeditTask, SWT.NONE);
		cmbInCharge.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int indexSelected = cmbInCharge.getSelectionIndex();
				if (indexSelected != -1) {
					String inChargeId = (String) cmbInCharge.getData(cmbInCharge.getItem(indexSelected));
					Employee inCharge = WSHandler.getEmployeeById(employeeList, inChargeId);				
					task.setInCharge(inCharge);
				}
			}
		});
		cmbInCharge.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		
		CLabel lblAssignee = new CLabel(shlNewTaskeditTask, SWT.NONE);
		lblAssignee.setText("Assignee");
		
		cmbAssignee = new Combo(shlNewTaskeditTask, SWT.NONE);
		cmbAssignee.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int indexSelected = cmbAssignee.getSelectionIndex();
				if (indexSelected != -1) {
					String assigneeId = (String) cmbAssignee.getData(cmbAssignee.getItem(indexSelected));
					Employee assignee = WSHandler.getEmployeeById(employeeList, assigneeId);				
					task.setAssignee(assignee);	
				}
			}
		});
		cmbAssignee.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		
		lblCurrentAssignee = new CLabel(shlNewTaskeditTask, SWT.NONE);
		lblCurrentAssignee.setText("Current assignee");
		
		cmbCurrentAssignee = new Combo(shlNewTaskeditTask, SWT.NONE);
		cmbCurrentAssignee.setEnabled(false);
		cmbCurrentAssignee.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		
		CLabel lblTaskDescriptionru = new CLabel(shlNewTaskeditTask, SWT.NONE);
		lblTaskDescriptionru.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblTaskDescriptionru.setText("Task description (Ru)");
		
		textTaskDescription = new Text(shlNewTaskeditTask, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		textTaskDescription.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				task.setDescriptionRu(textTaskDescription.getText());
			}	
		});
		textTaskDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 3, 1));
		
		Button button = new Button(shlNewTaskeditTask, SWT.NONE);
		GridData gd_button = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
		gd_button.widthHint = 116;
		button.setLayoutData(gd_button);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if (!requiredFieldsFilled()) {
					MessageBox mb = new MessageBox(shlNewTaskeditTask);
					mb.setMessage("Not all required fields are filled! Fill all fields marked with '*'!");
					mb.open();
					
					return;
				}
				
				Task savedTask = new Task();
				if (!isANewTask) {
					savedTask = task;
				}	
					else
					{
					savedTask.setId("00000000-0000-0000-0000-000000000000");
					Status cs = new Status();
					cs.setId("00000000-0000-0000-0000-000000000000");
					cs.setName("");
					savedTask.setCurrentStatus(cs);
				}
				
				savedTask.setExternalId(textExternalId.getText());
				savedTask.setDescriptionRu(textTaskDescription.getText());
				
				GregorianCalendar dateOfDeadline = new GregorianCalendar(dDeadline.getYear(), dDeadline.getMonth(), dDeadline.getDay());
				XMLGregorianCalendar xmlDateOfDeadline = null;
				try {
					xmlDateOfDeadline = DatatypeFactory.newInstance().newXMLGregorianCalendar(dateOfDeadline);
				} catch (DatatypeConfigurationException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
				savedTask.setDeadline(xmlDateOfDeadline);
				
				Float estimateAsFloat = (float) 0.0;
				try {
					estimateAsFloat = Float.valueOf(textEstimate.getText());
				}
				catch (Exception ex) {
					//TODO: handle
				}
				savedTask.setEstimate(estimateAsFloat);
				savedTask.setName(textTaskName.getText()); 
				
				int indexSelected = cmbProjects.getSelectionIndex();
				if (indexSelected != -1) {
					String projectId = (String) cmbProjects.getData(cmbProjects.getItem(indexSelected));
					Project project = WSHandler.getProjectById(projectList, projectId);				
					savedTask.setProject(project);
				}
				
				indexSelected = cmbProjectStages.getSelectionIndex();
				if (indexSelected != -1) {
					String projectStageId = (String) cmbProjectStages.getData(cmbProjectStages.getItem(indexSelected));
					ProjectStage projectStage = WSHandler.getProjectStageById(projectStageList, projectStageId);				
					savedTask.setProjectStage(projectStage);
				}
				
				indexSelected = cmbInCharge.getSelectionIndex();
				if (indexSelected != -1) {
					String inChargeId = (String) cmbInCharge.getData(cmbInCharge.getItem(indexSelected));
					Employee inCharge = WSHandler.getEmployeeById(employeeList, inChargeId);				
					savedTask.setInCharge(inCharge);
				}
				
				indexSelected = cmbAssignee.getSelectionIndex();
				if (indexSelected != -1) {
					String assigneeId = (String) cmbAssignee.getData(cmbAssignee.getItem(indexSelected));
					Employee assignee = WSHandler.getEmployeeById(employeeList, assigneeId);				
					savedTask.setAssignee(assignee);	
				}
				indexSelected = cmbCurrentAssignee.getSelectionIndex();
				if (indexSelected != -1) {
					String currentAssigneeId = (String) cmbCurrentAssignee.getData(cmbCurrentAssignee.getItem(indexSelected));
					Employee currentAssignee = WSHandler.getEmployeeById(employeeList, currentAssigneeId);				
					savedTask.setCurrentAssignee(currentAssignee);
				}
				
				boolean result = false;
				
				if (isANewTask) {
					savedTask = WSHandler.createTask(savedTask); 
					
					if (!savedTask.getId().equals("00000000-0000-0000-0000-000000000000")) {
						
						task = savedTask;
						isANewTask = false;
						shlNewTaskeditTask.setText("Edit task");
						result = true;
					}
				}
				else {
					result = WSHandler.updateTask(savedTask);
					//TODO: check if true
				}
				
				shlNewTaskeditTask.close();
				 if (!(parentDialog == null)) {
			        	parentDialog.reloadTable();
			        }
			}

		});
		button.setText("Save");
		new Label(shlNewTaskeditTask, SWT.NONE);
		new Label(shlNewTaskeditTask, SWT.NONE);

		fillDialog();
	}
	
	private boolean requiredFieldsFilled() {

		if (cmbProjects.getText().equals("")
					|| textTaskName.getText().equals("")
						|| cmbInCharge.getText().equals(""))
		return false;
		else
		return true;
	}
	
	public void fillDialog() {
		projectList = null;
		projectList = WSHandler.getProjects("");
		cmbProjects.removeAll();
		String value = "";
		for (Project project : projectList.getProjects()) {
			value = project.getName();
			cmbProjects.add(value);
			cmbProjects.setData(value, project.getId());
		}

		employeeList = null;
		employeeList = WSHandler.getEmployees("");
		cmbAssignee.removeAll();
		cmbInCharge.removeAll();
		cmbCurrentAssignee.removeAll();
		value = "";
		for (Employee employee : employeeList.getEmployees()) {
			value = employee.getName();
			cmbInCharge.add(value);
			cmbInCharge.setData(value, employee.getId());
			cmbAssignee.add(value);
			cmbAssignee.setData(value, employee.getId());
			cmbCurrentAssignee.add(value);
			cmbCurrentAssignee.setData(value, employee.getId());
		}
		
		
		if (!isANewTask()) {
			//fill with Task values
			//task
			
			textExternalId.setText(task.getExternalId());
			textTaskDescription.setText(task.getDescriptionRu());
			cmbInCharge.select(cmbInCharge.indexOf(task.getInCharge().getName()));
			cmbAssignee.select(cmbAssignee.indexOf(task.getAssignee().getName()));
			cmbCurrentAssignee.select(cmbCurrentAssignee.indexOf(task.getCurrentAssignee().getName()));
			dDeadline.setDate(task.getDeadline().getYear(), task.getDeadline().getMonth() - 1, task.getDeadline().getDay());
			textEstimate.setText(String.valueOf(task.getEstimate()));
			textTaskName.setText(task.getName()); 
			cmbProjects.select(cmbProjects.indexOf(task.getProject().getName()));
			
			
			projectStageList = WSHandler.getProjectStages(task.getProject(), "");
			
			value = "";
			cmbProjectStages.removeAll();
			for (ProjectStage projectStage : projectStageList.getProjectStages()) {
				value = projectStage.getName();
				cmbProjectStages.add(value);
				cmbProjectStages.setData(value, projectStage.getId());
			}
			cmbProjectStages.select(cmbProjectStages.indexOf(task.getProjectStage().getName()));
			
		}
		
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
}

package org.staffrichclient;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import net.assino.app1msc.Employee;
import net.assino.app1msc.Status;
import net.assino.app1msc.Task;
import net.assino.app1msc.TaskList;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.staffrichclient.utils.WSHandler;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.DateTime;

public class MainDialog {

	private TaskList taskList;
	
	private static Status StatusInProgress;
	private static Status StatusDone;
	private static Status StatusPaused;
	private static Status StatusDeployed;
	private static Status StatusInDevGroup;
	private static Employee CurrentEmployee;
	
	private static Color GreenColor;
	private static Color RedColor;
	private static Color GrayColor;
	private static Color WhiteColor;
	
	private static Color HoverGreenColor;
	private static Color HoverRedColor;
	private static Color HoverGrayColor;
	private static Color HoverWhiteColor;
	
	protected Shell shlStaffClient;
	private Table tasksTable;
	private Button btnSettings;
	private Text textSearchName;
	private Button btnNewTask;
	
	private static boolean connectionOK = false;
	private Button btnAbout;
	private DateTime dDateBegin;
	private DateTime dDateEnd;
	
	private Task selectedTask = null;
	private Button btnHideDone;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainDialog window = new MainDialog();
			Initialize();
			window.open();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void Initialize() {
		connectionOK = WSHandler.testConnector();
		
		if (connectionOK) {
			StatusInProgress = WSHandler.getStatusInProgress();
			StatusDone = WSHandler.getStatusDone();
			StatusPaused = WSHandler.getStatusPaused();
			StatusDeployed = WSHandler.getStatusDeployed();
			StatusInDevGroup = WSHandler.getStatusInDevGroup(); 
			
			CurrentEmployee = WSHandler.getCurrentEmployee();
			
			Display display = new Display();
			GreenColor = new Color (display, 0, 204, 0);
			HoverGreenColor = new Color (display, 77, 153, 0);
			RedColor = new Color (display, 255, 77, 77);
			HoverRedColor = new Color (display, 153, 0, 77);
			GrayColor = new Color (display, 153, 153, 153);
			HoverGrayColor = new Color (display, 77, 77, 77);
			WhiteColor = display.getSystemColor(SWT.COLOR_WHITE);
			HoverWhiteColor = new Color (display, 153, 204, 255);
		}
		
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		
		Image small = new Image(display, getClass().getResourceAsStream("logo.gif"));
		shlStaffClient.setImage(small);
		shlStaffClient.open();
		shlStaffClient.layout();
		
		if (!connectionOK) {
			SettingsDialog sd = new SettingsDialog();
			sd.open();
		} else reloadTable();
		
		while (!shlStaffClient.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlStaffClient = new Shell();
		shlStaffClient.setSize(805, 300);
		shlStaffClient.setText("Staff client");
		shlStaffClient.setLayout(new GridLayout(8, false));
		
		btnNewTask = new Button(shlStaffClient, SWT.NONE);
		btnNewTask.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TaskDialog add = new TaskDialog();
				add.setANewTask(true);
				add.open();
				add.setParentDialog(getDialog());
			}
		});
		btnNewTask.setText("New task");
		
		Button btnReload = new Button(shlStaffClient, SWT.NONE);
		btnReload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				reloadTable();
			}
		});
		btnReload.setText("Reload");
		
		textSearchName = new Text(shlStaffClient, SWT.BORDER);
		GridData gd_textSearchName = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_textSearchName.widthHint = 134;
		textSearchName.setLayoutData(gd_textSearchName);
		if (connectionOK) {
			textSearchName.setText(WSHandler.getCurrentEmployee().getName());
		}
		
		dDateBegin = new DateTime(shlStaffClient, SWT.BORDER);
		
		dDateEnd = new DateTime(shlStaffClient, SWT.BORDER);
		
		btnHideDone = new Button(shlStaffClient, SWT.CHECK);
		btnHideDone.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				reloadTable();
			}
		});
		btnHideDone.setText("hide done");
		
		btnSettings = new Button(shlStaffClient, SWT.NONE);
		btnSettings.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				SettingsDialog sd = new SettingsDialog();
				sd.open();
				sd.setParentDialog(getDialog());
			}
		});
		btnSettings.setText("Settings");
		
		btnAbout = new Button(shlStaffClient, SWT.NONE);
		btnAbout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AboutWindow aw = new AboutWindow();
				aw.open();
			}
		});
		btnAbout.setText("About");
		
		tasksTable = new Table(shlStaffClient, SWT.BORDER | SWT.FULL_SELECTION);
		tasksTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {

				TaskDialog add = new TaskDialog();
				add.setANewTask(false);
				
				TableItem[] items = tasksTable.getSelection();
				
				boolean openWindow = false;
				
				for (TableItem item : items) {
					
					Task task = WSHandler.getTaskById(taskList, item.getText(0));
					add.setTask(task);
					//TODO: check if task is null
					openWindow = true;
					
					//we need only first item anyway
					break;
				}
				
				if (openWindow) add.open();
			}
			
			@Override
			public void mouseDown(MouseEvent event) { 
					if (event.button==3){ 
					
					selectedTask = getSelectedTask();
						
					Menu menu = new Menu (tasksTable.getShell(), SWT.POP_UP); 
					MenuItem item = new MenuItem (menu, SWT.PUSH); 
					
					if (!(selectedTask == null)) {
						
						String itemText = "It's not your task!";
						
						if ((CurrentEmployee.getId().equals(selectedTask.getCurrentAssignee().getId()) || CurrentEmployee.getId().equals(selectedTask.getAssignee().getId()))
							 	&& selectedTask.getCurrentStatus().getId().equals(StatusInProgress.getId())){
							itemText = "Pause task '"+selectedTask.getName()+"'";
							 item.addSelectionListener(new SelectionAdapter() {
									@Override
									public void widgetSelected(SelectionEvent e) {
										//TODO: change status and reload table
										WSHandler.SetStatusPaused(selectedTask);
										reloadTable();
									}
								});
							    }
							 else if ((CurrentEmployee.getId().equals(selectedTask.getCurrentAssignee().getId()) || CurrentEmployee.getId().equals(selectedTask.getAssignee().getId()))
									 	&& selectedTask.getCurrentStatus().getId().equals(StatusPaused.getId())){
								 itemText = "Resume task '"+selectedTask.getName()+"'";
								 
								 item.addSelectionListener(new SelectionAdapter() {
										@Override
										public void widgetSelected(SelectionEvent e) {
											//TODO: change status and reload table
											WSHandler.SetStatusInProgress(selectedTask);
											reloadTable();
										}
									});
								        }
							 else if ((CurrentEmployee.getId().equals(selectedTask.getCurrentAssignee().getId()) || CurrentEmployee.getId().equals(selectedTask.getAssignee().getId()))
									 	&& selectedTask.getCurrentStatus().getId().equals(StatusInDevGroup.getId())){
								 itemText = "Start task '"+selectedTask.getName()+"'";
								 
								 item.addSelectionListener(new SelectionAdapter() {
										@Override
										public void widgetSelected(SelectionEvent e) {
											//TODO: change status and reload table
											WSHandler.SetStatusInProgress(selectedTask);
											reloadTable();
										}
									});
								        }
						
							 else if ((CurrentEmployee.getId().equals(selectedTask.getCurrentAssignee().getId()) || CurrentEmployee.getId().equals(selectedTask.getAssignee().getId()))
									 	&& (selectedTask.getCurrentStatus().getId().equals(StatusDeployed.getId()) 
									 	|| selectedTask.getCurrentStatus().getId().equals(StatusDone.getId()))){
								 itemText = "Task is either deployed or done.";
								        }
							 else if (CurrentEmployee.getId().equals(selectedTask.getCurrentAssignee().getId()) || CurrentEmployee.getId().equals(selectedTask.getAssignee().getId())) itemText = "Task is not in progress.";
						
						item.setText (itemText); 
						
					}
					
					MenuItem itemMarkDone = new MenuItem (menu, SWT.PUSH);
					
					if (!(selectedTask == null)) {
						itemMarkDone.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								//TODO: change status and reload table
								WSHandler.SetStatusDone(selectedTask);
								reloadTable();
							}
						});
						itemMarkDone.setText("Set task status to 'done'");
				
					}
						
					//draws pop up menu: 
					Point pt = new Point (event.x, event.y); 
					pt = tasksTable.toDisplay (pt); 
					menu.setLocation (pt.x, pt.y); 
					menu.setVisible (true); 
				} 
			}
		});

		
		//Coloring the table selection		
			tasksTable.addListener(SWT.EraseItem, new Listener() {
				     public void handleEvent(Event event) {
				        
				    	Color color = HoverWhiteColor;
				    	 
				    	 selectedTask = getSelectedTask();
							
							if (!(selectedTask == null)) {
								//TODO: refactor
								
								if ((CurrentEmployee.getId().equals(selectedTask.getCurrentAssignee().getId()) || CurrentEmployee.getId().equals(selectedTask.getAssignee().getId()))
									 	&& selectedTask.getCurrentStatus().getId().equals(StatusInProgress.getId())){
										color = HoverGreenColor;
									    }
									 else if ((CurrentEmployee.getId().equals(selectedTask.getCurrentAssignee().getId()) || CurrentEmployee.getId().equals(selectedTask.getAssignee().getId()))
											 	&& selectedTask.getCurrentStatus().getId().equals(StatusPaused.getId())){
										 color = HoverRedColor;
										        }
									 else if ((CurrentEmployee.getId().equals(selectedTask.getCurrentAssignee().getId()) || CurrentEmployee.getId().equals(selectedTask.getAssignee().getId()))
											 	&& selectedTask.getCurrentStatus().getId().equals(StatusInDevGroup.getId())){
										 color = HoverWhiteColor;
										        }
								
									 else if ((CurrentEmployee.getId().equals(selectedTask.getCurrentAssignee().getId()) || CurrentEmployee.getId().equals(selectedTask.getAssignee().getId()))
											 	&& (selectedTask.getCurrentStatus().getId().equals(StatusDeployed.getId()) 
											 	|| selectedTask.getCurrentStatus().getId().equals(StatusDone.getId()))){
										 color = HoverGrayColor;
									 }							
							}
				    	 
				    	 event.detail &= ~SWT.HOT;
				        if ((event.detail & SWT.SELECTED) == 0) 
				          return; 
				        int clientWidth = ((Composite)event.widget).getClientArea().width;
				        GC gc = event.gc;
				        Color oldForeground = gc.getForeground();
				        Color oldBackground = gc.getBackground();
				        gc.setBackground(color);
				        gc.setForeground(color);
				        gc.fillGradientRectangle(0, event.y, clientWidth, event.height, true);
				        gc.setForeground(oldForeground);
				        gc.setBackground(oldBackground);
				        event.detail &= ~SWT.SELECTED;
				     }
				  });

		
		GridData gd_tasksTable = new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1);
		gd_tasksTable.widthHint = 783;
		tasksTable.setLayoutData(gd_tasksTable);
		tasksTable.setHeaderVisible(true);
		tasksTable.setLinesVisible(true);
		createTaskTableStructure();

	}
	
	private Task getSelectedTask() {
		//get the selected row
		selectedTask = null; 
		TableItem[] items = tasksTable.getSelection();
			
			for (TableItem item : items) {
				
				selectedTask = WSHandler.getTaskById(taskList, item.getText(0));
				
				//we need only first item anyway
				break;
			}
			return selectedTask;
		}
	
	protected void reloadTable() {
		fillTable(tasksTable);
		
		tasksTable.removeAll();
		taskList = null;
		
		GregorianCalendar grDateEnd = new GregorianCalendar(dDateEnd.getYear(), dDateEnd.getMonth(), dDateEnd.getDay());
		GregorianCalendar grDateBegin = new GregorianCalendar(dDateBegin.getYear(), dDateBegin.getMonth(), dDateBegin.getDay());
		
		taskList = WSHandler.getTaskList(grDateBegin, grDateEnd, textSearchName.getText());
				
		for (Task task : taskList.getTasks()) {
			
			//TODO: need to implement this on the server side
			if (btnHideDone.getSelection()&&(task.getCurrentStatus().getId().equals(StatusDeployed.getId()) 
				 	|| task.getCurrentStatus().getId().equals(StatusDone.getId())))
			{
				continue;
			}
			
			 TableItem item = new TableItem(tasksTable, SWT.NONE);
			 item.setText(0, task.getId());
			 item.setText(1, task.getName());					
			 item.setText(2, task.getProject().getName());
			 item.setText(3, task.getProjectStage().getName());
			 item.setText(4, task.getInCharge().getName());
			 item.setText(5, task.getCurrentStatus().getName());
			 item.setText(6, task.getDeadline().toString());
			 item.setText(7, task.getDescriptionRu().toString());
			 item.setText(8, Float.toString(task.getEstimate()));
			 item.setText(9, task.getExternalId());
			 item.setText(10, task.getAssignee().getName());
			 item.setText(11, task.getCurrentAssignee().getName());
			 
			 if ((CurrentEmployee.getId().equals(task.getCurrentAssignee().getId()) || CurrentEmployee.getId().equals(task.getAssignee().getId()))
			 	&& task.getCurrentStatus().getId().equals(StatusInProgress.getId())){
				 item.setBackground(GreenColor);
		        }
			 else if ((CurrentEmployee.getId().equals(task.getCurrentAssignee().getId()) || CurrentEmployee.getId().equals(task.getAssignee().getId()))
					 	&& task.getCurrentStatus().getId().equals(StatusPaused.getId())){
						 item.setBackground(RedColor);
				        }
			 else if ((CurrentEmployee.getId().equals(task.getCurrentAssignee().getId()) || CurrentEmployee.getId().equals(task.getAssignee().getId()))
					 	&& (task.getCurrentStatus().getId().equals(StatusDeployed.getId()) 
					 	|| task.getCurrentStatus().getId().equals(StatusDone.getId()))){
						 item.setBackground(GrayColor);
				        }
			 else item.setBackground(WhiteColor);
				 
			 
		 }
		tasksTable.setRedraw(true);
	}

	private void fillTable(Table table) {
		  table.setRedraw(false);
	}
	
	private MainDialog getDialog() {
		return this;
	}
	
	private void createTaskTableStructure() {
		TableColumn[] column = new TableColumn[12];
		  column[0] = new TableColumn(tasksTable, SWT.NONE);
		  column[0].setText("Id");
		  column[1] = new TableColumn(tasksTable, SWT.NONE);
		  column[1].setText("Name");
		  
		  column[2] = new TableColumn(tasksTable, SWT.NONE);
		  column[2].setText("Project");
		  column[3] = new TableColumn(tasksTable, SWT.NONE);
		  column[3].setText("ProjectStage");
		  column[4] = new TableColumn(tasksTable, SWT.NONE);
		  column[4].setText("InCharge");
		  column[5] = new TableColumn(tasksTable, SWT.NONE);
		  column[5].setText("Current status");
		  column[6] = new TableColumn(tasksTable, SWT.NONE);
		  column[6].setText("Deadline");
		  column[7] = new TableColumn(tasksTable, SWT.NONE);
		  column[7].setText("DescriptionRu");
		  column[8] = new TableColumn(tasksTable, SWT.NONE);
		  column[8].setText("Estimate");
		  column[9] = new TableColumn(tasksTable, SWT.NONE);
		  column[9].setText("ExternalId");
		  column[10] = new TableColumn(tasksTable, SWT.NONE);
		  column[10].setText("Assignee");
		  column[11] = new TableColumn(tasksTable, SWT.NONE);
		  column[11].setText("Current assignee");
		  
		  fillTable(tasksTable);
		  for (int i = 0, n = column.length; i < n; i++) {
		  column[i].pack();
		  }
		  tasksTable.setRedraw(true);
	}
}

package org.staff.richclient;

import java.util.GregorianCalendar;


import net.assino.app1msc.Task;
import net.assino.app1msc.TaskList;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.staff.settings.Constants;
import org.staff.settings.colortheme.MainDialogColorTheme;
import org.staff.utils.ErrorVisualizer;
import org.staff.utils.WSHandler;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

public class MainDialog {

	Clipboard clipboard;
	
	private TaskList taskList;
	
	protected static Shell shlStaffClient;
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
			//e.printStackTrace();
			ErrorVisualizer.showMessage(shlStaffClient, e);
		}
	}

	private static void Initialize() {
		connectionOK = WSHandler.testConnector();
		
		if (connectionOK) {
			setConstants();
			Display display = new Display();
			setDefaultColorTheme(display);
		}
		
	}

	/**
	 * 
	 */
	public static void setConstants() {
		Constants.setStatusInProgress(WSHandler.getStatusInProgress());
		Constants.setStatusDone(WSHandler.getStatusDone());
		Constants.setStatusPaused(WSHandler.getStatusPaused());
		Constants.setStatusDeployed(WSHandler.getStatusDeployed());
		Constants.setStatusInDevGroup(WSHandler.getStatusInDevGroup()); 	
		Constants.setCurrentEmployee(WSHandler.getCurrentEmployee());
	}

	/**
	 * @param display
	 */
	public static void setDefaultColorTheme(Display display) {
		MainDialogColorTheme.setGreenColor(new Color (display, 0, 204, 0));
		MainDialogColorTheme.setHoverGreenColor(new Color (display, 77, 153, 0));
		MainDialogColorTheme.setRedColor(new Color (display, 255, 77, 77));
		MainDialogColorTheme.setHoverRedColor(new Color (display, 153, 0, 77));
		MainDialogColorTheme.setGrayColor(new Color (display, 153, 153, 153));
		MainDialogColorTheme.setHoverGrayColor(new Color (display, 77, 77, 77));
		MainDialogColorTheme.setWhiteColor(display.getSystemColor(SWT.COLOR_WHITE));
		MainDialogColorTheme.setHoverWhiteColor(new Color (display, 153, 204, 255));
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		clipboard = new Clipboard(display);
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
		shlStaffClient.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				//We need to save some filter values here
				//btnHideDone.getSelection()
				//textSearchName
				//dDateBegin
				//dDateEnd
				
				//TODO: save settings
				//ApplicationSettings as = new ApplicationSettings();

			}
		});
		shlStaffClient.setSize(805, 300);
		shlStaffClient.setText("Staff client");
		shlStaffClient.setLayout(new GridLayout(8, false));
		
		btnNewTask = new Button(shlStaffClient, SWT.NONE);
		btnNewTask.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TaskDialog add = new TaskDialog();
				
				try {
					add.setANewTask(true);
					add.open();
					add.setParentDialog(getDialog());
				}
				finally {
					reloadTable();
				}
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
		
		dDateBegin = new DateTime(shlStaffClient, SWT.DROP_DOWN);
		
		dDateEnd = new DateTime(shlStaffClient, SWT.DROP_DOWN);
		
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
				sd.setConnectionOK(connectionOK);
				sd.setParentDialog(getDialog());
				sd.open();				
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
		tasksTable.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				if(((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 'c'))
                {
					selectedTask = getSelectedTask();
					 
					if (!(selectedTask == null))
					clipboard.setContents(new Object[] { selectedTask.getDescriptionRu() },
				              new Transfer[] { TextTransfer.getInstance() });
                }
			}
		});
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
						
						if ((Constants.getCurrentEmployee().getId().equals(selectedTask.getCurrentAssignee().getId()) || Constants.getCurrentEmployee().getId().equals(selectedTask.getAssignee().getId()))
							 	&& selectedTask.getCurrentStatus().getId().equals(Constants.getStatusInProgress().getId())){
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
							 else if ((Constants.getCurrentEmployee().getId().equals(selectedTask.getCurrentAssignee().getId()) || Constants.getCurrentEmployee().getId().equals(selectedTask.getAssignee().getId()))
									 	&& selectedTask.getCurrentStatus().getId().equals(Constants.getStatusPaused().getId())){
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
							 else if ((Constants.getCurrentEmployee().getId().equals(selectedTask.getCurrentAssignee().getId()) || Constants.getCurrentEmployee().getId().equals(selectedTask.getAssignee().getId()))
									 	&& selectedTask.getCurrentStatus().getId().equals(Constants.getStatusInDevGroup().getId())){
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
						
							 else if ((Constants.getCurrentEmployee().getId().equals(selectedTask.getCurrentAssignee().getId()) || Constants.getCurrentEmployee().getId().equals(selectedTask.getAssignee().getId()))
									 	&& (selectedTask.getCurrentStatus().getId().equals(Constants.getStatusDeployed().getId()) 
									 	|| selectedTask.getCurrentStatus().getId().equals(Constants.getStatusDone().getId()))){
								 itemText = "Task is either deployed or done.";
								        }
							 else if (Constants.getCurrentEmployee().getId().equals(selectedTask.getCurrentAssignee().getId()) || Constants.getCurrentEmployee().getId().equals(selectedTask.getAssignee().getId())) itemText = "Task is not in progress.";
						
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
				
						
						if (!(selectedTask.getExternalId().equals(""))) {
							MenuItem itemOpenTicket = new MenuItem (menu, SWT.PUSH);
							itemOpenTicket.addSelectionListener(new SelectionAdapter() {
								@Override
								public void widgetSelected(SelectionEvent e) {

									Program.launch("http://hotline.assino.net/otrs/index.pl?Action=AgentTicketZoom;TicketNumber="+selectedTask.getExternalId());
								}
							});
							itemOpenTicket.setText("Open OTRS ticket in browser.");
						}
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
				        
				    	Color color = MainDialogColorTheme.getHoverWhiteColor();
				    	 
				    	 selectedTask = getSelectedTask();
							
							if (!(selectedTask == null)) {
								//TODO: refactor
								
								if ((Constants.getCurrentEmployee().getId().equals(selectedTask.getCurrentAssignee().getId()) || Constants.getCurrentEmployee().getId().equals(selectedTask.getAssignee().getId()))
									 	&& selectedTask.getCurrentStatus().getId().equals(Constants.getStatusInProgress().getId())){
										color = MainDialogColorTheme.getHoverGreenColor();
									    }
									 else if ((Constants.getCurrentEmployee().getId().equals(selectedTask.getCurrentAssignee().getId()) || Constants.getCurrentEmployee().getId().equals(selectedTask.getAssignee().getId()))
											 	&& selectedTask.getCurrentStatus().getId().equals(Constants.getStatusPaused().getId())){
										 color = MainDialogColorTheme.getHoverRedColor();
										        }
									 else if ((Constants.getCurrentEmployee().getId().equals(selectedTask.getCurrentAssignee().getId()) || Constants.getCurrentEmployee().getId().equals(selectedTask.getAssignee().getId()))
											 	&& selectedTask.getCurrentStatus().getId().equals(Constants.getStatusInDevGroup().getId())){
										 color = MainDialogColorTheme.getHoverWhiteColor();
										        }
								
									 else if ((Constants.getCurrentEmployee().getId().equals(selectedTask.getCurrentAssignee().getId()) || Constants.getCurrentEmployee().getId().equals(selectedTask.getAssignee().getId()))
											 	&& (selectedTask.getCurrentStatus().getId().equals(Constants.getStatusDeployed().getId()) 
											 	|| selectedTask.getCurrentStatus().getId().equals(Constants.getStatusDone().getId()))){
										 color = MainDialogColorTheme.getHoverGrayColor();
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
			if (btnHideDone.getSelection()&&(task.getCurrentStatus().getId().equals(Constants.getStatusDeployed().getId()) 
				 	|| task.getCurrentStatus().getId().equals(Constants.getStatusDone().getId())))
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
			 item.setText(9, Float.toString(task.getTimeTaken()));
			 item.setText(10, task.getExternalId());
			 item.setText(11, task.getAssignee().getName());
			 item.setText(12, task.getCurrentAssignee().getName());
			 
			 if ((Constants.getCurrentEmployee().getId().equals(task.getCurrentAssignee().getId()) || Constants.getCurrentEmployee().getId().equals(task.getAssignee().getId()))
			 	&& task.getCurrentStatus().getId().equals(Constants.getStatusInProgress().getId())){
				 item.setBackground(MainDialogColorTheme.getGreenColor());
		        }
			 else if ((Constants.getCurrentEmployee().getId().equals(task.getCurrentAssignee().getId()) || Constants.getCurrentEmployee().getId().equals(task.getAssignee().getId()))
					 	&& task.getCurrentStatus().getId().equals(Constants.getStatusPaused().getId())){
						 item.setBackground(MainDialogColorTheme.getRedColor());
				        }
			 else if ((Constants.getCurrentEmployee().getId().equals(task.getCurrentAssignee().getId()) || Constants.getCurrentEmployee().getId().equals(task.getAssignee().getId()))
					 	&& (task.getCurrentStatus().getId().equals(Constants.getStatusDeployed().getId()) 
					 	|| task.getCurrentStatus().getId().equals(Constants.getStatusDone().getId()))){
						 item.setBackground(MainDialogColorTheme.getGrayColor());
				        }
			 else item.setBackground(MainDialogColorTheme.getWhiteColor());
				 
			 
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
		TableColumn[] column = new TableColumn[13];
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
		  column[9].setText("TimeTaken");
		  column[10] = new TableColumn(tasksTable, SWT.NONE);
		  column[10].setText("ExternalId");
		  column[11] = new TableColumn(tasksTable, SWT.NONE);
		  column[11].setText("Assignee");
		  column[12] = new TableColumn(tasksTable, SWT.NONE);
		  column[12].setText("Current assignee");
		  
		  fillTable(tasksTable);
		  for (int i = 0, n = column.length; i < n; i++) {
		  column[i].pack();
		  }
		  tasksTable.setRedraw(true);
	}
}

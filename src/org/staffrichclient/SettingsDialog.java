package org.staffrichclient;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.staffrichclient.utils.ApplicationSettings;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;

public class SettingsDialog {

	protected Shell shell;
	private Text textUsername;
	private Text textPassword;
	
	private MainDialog parentDialog = null;

	public void setParentDialog(MainDialog parentDialog) {
		this.parentDialog = parentDialog;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			SettingsDialog window = new SettingsDialog();
			window.open();
			
			window.loadSettings();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadSettings() {
		ApplicationSettings as = new ApplicationSettings();
		textUsername.setText(as.getUsername());
		textPassword.setText(as.getPassword());
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		Image small = new Image(display, getClass().getResourceAsStream("logo.gif"));
		shell.setImage(small);
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellActivated(ShellEvent e) {
				loadSettings();
			}
		});
		shell.setSize(450, 140);
		shell.setText("Settings");
		
		textUsername = new Text(shell, SWT.BORDER);
		textUsername.setBounds(118, 10, 306, 21);
		
		textPassword = new Text(shell, SWT.BORDER | SWT.PASSWORD);
		textPassword.setBounds(118, 38, 306, 21);
		
		CLabel lblLogin = new CLabel(shell, SWT.NONE);
		lblLogin.setBounds(10, 10, 61, 21);
		lblLogin.setText("Login");
		
		CLabel lblPassword = new CLabel(shell, SWT.NONE);
		lblPassword.setText("Password");
		lblPassword.setBounds(10, 38, 61, 21);
		
		Button btnSave = new Button(shell, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ApplicationSettings as = new ApplicationSettings();
		        as.savePassword(textPassword.getText());
		        as.saveUsername(textUsername.getText());
		        shell.close();
		        if (!(parentDialog == null)) {
		        	parentDialog.reloadTable();
		        }
			}
		});
		btnSave.setBounds(10, 65, 414, 25);
		btnSave.setText("Save");

	}
}

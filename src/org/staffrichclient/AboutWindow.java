package org.staffrichclient;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;

public class AboutWindow {

	protected Shell shlAbout;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			AboutWindow window = new AboutWindow();
			window.open();
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
		shlAbout.setImage(small);
		
		shlAbout.open();
		shlAbout.layout();
		while (!shlAbout.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlAbout = new Shell();
		
		shlAbout.setSize(450, 300);
		shlAbout.setText("About");
		
		Label lblAssinoErpstaffRich = new Label(shlAbout, SWT.NONE);
		lblAssinoErpstaffRich.setFont(SWTResourceManager.getFont("Segoe UI", 24, SWT.NORMAL));
		lblAssinoErpstaffRich.setBounds(23, 48, 401, 45);
		lblAssinoErpstaffRich.setText("assino ERP-Staff Rich client");
		
		Label lblVersion = new Label(shlAbout, SWT.NONE);
		lblVersion.setText("version 0.2 by Invaa (c)");
		lblVersion.setBounds(148, 126, 134, 35);

	}
}

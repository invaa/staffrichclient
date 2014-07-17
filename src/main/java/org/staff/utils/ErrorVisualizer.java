package org.staff.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public final class ErrorVisualizer {

	public static void showMessage(Shell shell, Exception e) {
		String msg = e.getMessage();
		MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK); //SWT.YES | SWT.NO
		mb.setMessage(msg);
		mb.setText("Error");
		int mbResult = mb.open();
	}
	
}

package org.staff.settings;

import net.assino.app1msc.Employee;
import net.assino.app1msc.Status;

public final class Constants {
	private static Status StatusInProgress;
	private static Status StatusDone;
	private static Status StatusPaused;
	private static Status StatusDeployed;
	private static Status StatusInDevGroup;
	private static Employee CurrentEmployee;
	public static Status getStatusInProgress() {
		return StatusInProgress;
	}
	public static void setStatusInProgress(Status statusInProgress) {
		StatusInProgress = statusInProgress;
	}
	public static Status getStatusDone() {
		return StatusDone;
	}
	public static void setStatusDone(Status statusDone) {
		StatusDone = statusDone;
	}
	public static Status getStatusPaused() {
		return StatusPaused;
	}
	public static void setStatusPaused(Status statusPaused) {
		StatusPaused = statusPaused;
	}
	public static Status getStatusDeployed() {
		return StatusDeployed;
	}
	public static void setStatusDeployed(Status statusDeployed) {
		StatusDeployed = statusDeployed;
	}
	public static Status getStatusInDevGroup() {
		return StatusInDevGroup;
	}
	public static void setStatusInDevGroup(Status statusInDevGroup) {
		StatusInDevGroup = statusInDevGroup;
	}
	public static Employee getCurrentEmployee() {
		return CurrentEmployee;
	}
	public static void setCurrentEmployee(Employee currentEmployee) {
		CurrentEmployee = currentEmployee;
	}
	
	
}

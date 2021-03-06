package kip.tools.model;

import java.io.Serializable;

public class NextBestAction implements Serializable {

	private static final long serialVersionUID = 1L;

	private int period;
	private String action;
	private double benefit;
	private SimPeriod simPeriod;
	private String taskNameForAction;
	private String taskRefForAction;

	public SimPeriod getSimPeriod() {
		return simPeriod;
	}

	public void setSimPeriod(SimPeriod simPeriod) {
		this.simPeriod = simPeriod;
	}

	public NextBestAction() {
	}

	public NextBestAction(String action, double benefit) {
		this.action = action;
		this.benefit = benefit;
	}

	@Override
	public String toString() {
		return "NextBestAction [action=" + action + ", benefit=" + benefit + "]";
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public double getBenefit() {
		return benefit;
	}

	public void setBenefit(double benefit) {
		this.benefit = benefit;
	}

	public String getTaskNameForAction() {
		return taskNameForAction;
	}

	public void setTaskNameForAction(String taskNameForAction) {
		this.taskNameForAction = taskNameForAction;
	}

	public String getTaskRefForAction() {
		return taskRefForAction;
	}

	public void setTaskRefForAction(String taskRefForAction) {
		this.taskRefForAction = taskRefForAction;
	}

	// public SimAct getSimulatedValues() {
	// return simulatedValues;
	// }
	//
	// public void setSimulatedValues(SimAct simulatedValues) {
	// this.simulatedValues = simulatedValues;
	// }

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

}

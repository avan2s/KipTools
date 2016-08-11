package kip.tools.model;

import java.io.Serializable;

public class NextBestAction implements Serializable {

	private static final long serialVersionUID = 1L;

	private int period;
	private String action;
	private double benefit;
//	private SimAct simulatedValues;

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

//	public SimAct getSimulatedValues() {
//		return simulatedValues;
//	}
//
//	public void setSimulatedValues(SimAct simulatedValues) {
//		this.simulatedValues = simulatedValues;
//	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

}

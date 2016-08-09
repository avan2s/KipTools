package kip.tools.model;

import java.io.Serializable;

public class NextBestAction implements Serializable {

	private static final long serialVersionUID = 1L;

	private String action;
	private double benefit;
	private SimAct simulatedValues;

	public NextBestAction() {
		this.simulatedValues = new SimAct();
	}

	public NextBestAction(String action, double benefit) {
		this.action = action;
		this.benefit = benefit;
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

	public SimAct getSimulatedValues() {
		return simulatedValues;
	}

	public void setSimulatedValues(SimAct simulatedValues) {
		this.simulatedValues = simulatedValues;
	}

}

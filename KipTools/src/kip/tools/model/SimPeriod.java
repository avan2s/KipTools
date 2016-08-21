package kip.tools.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SimPeriod implements Serializable {
	private static final long serialVersionUID = 1L;

	private int period;
	private List<SimAct> simActValues;

	public SimPeriod() {
		this.simActValues = new ArrayList<>();
	}

	public boolean addSimAct(SimAct simAct) {
		return this.simActValues.add(simAct);
	}

	public SimAct getSimActByAction(String actionName) {
		for (SimAct simAct : this.simActValues) {
			if (simAct.getAction().contentEquals(actionName)) {
				return simAct;
			}
		}
		return null;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public List<SimAct> getSimActValues() {
		return simActValues;
	}

	public void setSimActValues(List<SimAct> simActValues) {
		this.simActValues = simActValues;
	}

}

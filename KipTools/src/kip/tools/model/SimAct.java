package kip.tools.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SimAct implements Serializable {

	private static final long serialVersionUID = 1L;

	private String action;
	private List<SimGoal> simGoalValues;

	public SimAct() {
		this.simGoalValues = new ArrayList<>();
	}

	public boolean addSimGoal(KipGoal goal, ExpectedValue expectedValue) {
		if (!this.simGoalValues.contains(goal)) {
			SimGoal simGoal = new SimGoal(goal, expectedValue);
			return this.simGoalValues.add(simGoal);
		}
		return false;
	}
	
	public SimGoal getSimGoalByGoalName(String goalName){
		for (SimGoal simGoal : simGoalValues) {
			String simGoalName = simGoal.getKipGoal().getGoalTarget();
			if(goalName.contentEquals(simGoalName))
				return simGoal;
		}
		return null;
	}

	public List<SimGoal> getSimGoalValues() {
		return simGoalValues;
	}

	public void setSimGoalValues(List<SimGoal> simGoalValues) {
		this.simGoalValues = simGoalValues;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}

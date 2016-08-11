package kip.tools.model;

import java.io.Serializable;

import kip.enums.GoalEffect;

public class KipGoal implements Serializable {

	private static final long serialVersionUID = 1L;

	private String goalTarget;
	private String abbreviation;
	private int goalstart_period;
	private int goalend_period;
	private double goalValue;
	private double goalWeight;
	private GoalEffect goalEffect;

	public KipGoal() {
	}

	public KipGoal(String goalTarget, int goalstart_period, int goalend_period, String abbreviation, double goalValue,
			GoalEffect effect) {
		this.goalTarget = goalTarget;
		this.goalstart_period = goalstart_period;
		this.goalend_period = goalend_period;
		this.abbreviation = abbreviation;
		this.goalValue = goalValue;
		this.goalEffect = effect;
	}

	@Override
	public String toString() {
		return "KipGoal [goalTarget=" + goalTarget + ", abbreviation=" + abbreviation + ", goalstart_period="
				+ goalstart_period + ", goalend_period=" + goalend_period + ", goalValue=" + goalValue + ", goalWeight="
				+ goalWeight + ", goalEffect=" + goalEffect + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((abbreviation == null) ? 0 : abbreviation.hashCode());
		result = prime * result + ((goalEffect == null) ? 0 : goalEffect.hashCode());
		result = prime * result + ((goalTarget == null) ? 0 : goalTarget.hashCode());
		long temp;
		temp = Double.doubleToLongBits(goalValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(goalWeight);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + goalend_period;
		result = prime * result + goalstart_period;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KipGoal other = (KipGoal) obj;
		if (abbreviation == null) {
			if (other.abbreviation != null)
				return false;
		} else if (!abbreviation.equals(other.abbreviation))
			return false;
		if (goalEffect != other.goalEffect)
			return false;
		if (goalTarget == null) {
			if (other.goalTarget != null)
				return false;
		} else if (!goalTarget.equals(other.goalTarget))
			return false;
		if (Double.doubleToLongBits(goalValue) != Double.doubleToLongBits(other.goalValue))
			return false;
		if (Double.doubleToLongBits(goalWeight) != Double.doubleToLongBits(other.goalWeight))
			return false;
		if (goalend_period != other.goalend_period)
			return false;
		if (goalstart_period != other.goalstart_period)
			return false;
		return true;
	}

	public String getGoalTarget() {
		return goalTarget;
	}

	public void setGoalTarget(String goalTarget) {
		this.goalTarget = goalTarget;
	}

	public int getGoalstart_period() {
		return goalstart_period;
	}

	public void setGoalstart_period(int goalstart_period) {
		this.goalstart_period = goalstart_period;
	}

	public int getGoalend_period() {
		return goalend_period;
	}

	public void setGoalend_period(int goalend_period) {
		this.goalend_period = goalend_period;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public double getGoalValue() {
		return goalValue;
	}

	public void setGoalValue(double goalValue) {
		this.goalValue = goalValue;
	}

	public GoalEffect getGoalEffect() {
		return goalEffect;
	}

	public void setGoalEffect(GoalEffect goalEffect) {
		this.goalEffect = goalEffect;
	}

	public double getGoalWeight() {
		return goalWeight;
	}

	public void setGoalWeight(double goalWeight) {
		this.goalWeight = goalWeight;
	}

}

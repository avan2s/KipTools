package kip.tools.model;

import java.io.Serializable;

import kip.enums.KipGoalEffect;
import kip.tools.UtilityTransformer;

public class ExpectedValue implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private double uniformUtility;
	public void setUniformUtility(double uniformUtility) {
		this.uniformUtility = uniformUtility;
	}

	public void setUnitValue(double unitValue) {
		this.unitValue = unitValue;
	}

	private double unitValue;

	public ExpectedValue() {

	}

	public double getUniformUtility() {
		return uniformUtility;
	}

	public void setUniformUtility(final double uniformUtility, final double goalValue, final KipGoalEffect effect) {
		this.uniformUtility = uniformUtility;
		this.unitValue = UtilityTransformer.calcUnitValue(uniformUtility, goalValue, effect);
	}

	public double getUnitValue() {
		return unitValue;
	}

	public void setUnitValue(final double unitValue, final double goalValue, final KipGoalEffect effect) {
		this.unitValue = unitValue;
		this.uniformUtility = UtilityTransformer.calcUniformUtility(unitValue, goalValue, effect);
	}

	@Override
	public String toString() {
		return "ExpectedValue [uniformUtility=" + uniformUtility + ", unitValue=" + unitValue + "]";
	}

}

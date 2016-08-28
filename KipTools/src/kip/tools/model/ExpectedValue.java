package kip.tools.model;

import java.io.Serializable;

public class ExpectedValue implements Serializable {

	private static final long serialVersionUID = 1L;

	private double uniformUtility;
	private double procentualDeviation;
	private double unitValue;

	public ExpectedValue() {
		this.uniformUtility = 0;
		this.procentualDeviation = 0;
		this.unitValue = 0;
	}

	public ExpectedValue(double uniformUtility, double procentualDeviation, double unitValue) {
		this.uniformUtility = uniformUtility;
		this.procentualDeviation = procentualDeviation;
		this.unitValue = unitValue;
	}

	public double getUniformUtility() {
		return uniformUtility;
	}

	public double getUnitValue() {
		return unitValue;
	}

	public double getProcentualDeviation() {
		return procentualDeviation;
	}

	public void setUniformUtility(double uniformUtility) {
		this.uniformUtility = uniformUtility;
	}

	public void setProcentualDeviation(double procentualDeviation) {
		this.procentualDeviation = procentualDeviation;
	}

	public void setUnitValue(double unitValue) {
		this.unitValue = unitValue;
	}

	@Override
	public String toString() {
		return "ExpectedValue [uniformUtility=" + uniformUtility + ", unitValue=" + unitValue + "]";
	}

}

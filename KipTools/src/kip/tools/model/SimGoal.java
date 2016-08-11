package kip.tools.model;

import java.io.Serializable;

public class SimGoal implements Serializable {

	private static final long serialVersionUID = 1L;

	private KipGoal kipGoal;
	private ExpectedValue expectedValue;

	public SimGoal() {

	}

	public SimGoal(KipGoal kipGoal, ExpectedValue expectedValue) {
		this.kipGoal = kipGoal;
		this.expectedValue = expectedValue;
	}

	@Override
	public String toString() {
		return "SimGoal [kipGoal=" + kipGoal + ", expectedValue=" + expectedValue + "]";
	}

	public KipGoal getKipGoal() {
		return kipGoal;
	}

	public void setKipGoal(KipGoal kipGoal) {
		this.kipGoal = kipGoal;
	}

	public ExpectedValue getExpectedValue() {
		return expectedValue;
	}

	public void setExpectedValue(ExpectedValue expectedValue) {
		this.expectedValue = expectedValue;
	}

}

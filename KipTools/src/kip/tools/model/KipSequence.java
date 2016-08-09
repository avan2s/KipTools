package kip.tools.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KipSequence implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<NextBestAction> sequence;
	private List<SimPeriod> simPeriods;

	public KipSequence() {
		this.sequence = new ArrayList<>();
		this.simPeriods = new ArrayList<>();
	}

	public KipSequence(List<NextBestAction> sequence, List<SimPeriod> simPeriodSet) {
		this.sequence = sequence;
		this.setSimPeriods(new ArrayList<>());
	}

	public List<NextBestAction> getSequence() {
		return sequence;
	}

	public void setSequence(List<NextBestAction> sequence) {
		this.sequence = sequence;
	}

	public List<SimPeriod> getSimPeriods() {
		return simPeriods;
	}

	public void setSimPeriods(List<SimPeriod> simPeriods) {
		this.simPeriods = simPeriods;
	}

}

package kip.tools;

import java.io.Serializable;

import smile.Network;

public class InfluenceDiagramNetwork extends Network implements Serializable {
	// http://stackoverflow.com/questions/95181/java-serialization-with-non-serializable-parts
	private static final long serialVersionUID = 1L;

	private String periodSeperator;
	private String decisionAbbreviation;
	private String instancePeriod;
	
	

	public InfluenceDiagramNetwork(String periodSeperator, String decisionAbbreviation, String instancePeriod) {
		super();
		this.periodSeperator = periodSeperator;
		this.decisionAbbreviation = decisionAbbreviation;
		this.instancePeriod = instancePeriod;
	}

	public String getInstancePeriod() {
		return instancePeriod;
	}

	public void setInstancePeriod(String instancePeriod) {
		this.instancePeriod = instancePeriod;
	}

	public InfluenceDiagramNetwork() {
		this.periodSeperator = "_";
	}

	public String getPeriodSeperator() {
		return periodSeperator;
	}

	public void setPeriodSeperator(String periodSeperator) {
		this.periodSeperator = periodSeperator;
	}

	public String getDecisionAbbreviation() {
		return decisionAbbreviation;
	}

	public void setDecisionAbbreviation(String decisionAbbreviation) {
		this.decisionAbbreviation = decisionAbbreviation;
	}

}

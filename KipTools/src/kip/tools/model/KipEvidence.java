package kip.tools.model;

import java.io.Serializable;

public class KipEvidence implements Serializable {
	private static final long serialVersionUID = 1L;

	private String nodeName;
	private String evidenceValue;

	public KipEvidence() {

	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getEvidenceValue() {
		return evidenceValue;
	}

	public void setEvidenceValue(String evidenceValue) {
		this.evidenceValue = evidenceValue;
	}

	@Override
	public String toString() {
		return "KipEvidence [nodeName=" + nodeName + ", evidenceValue=" + evidenceValue + "]";
	}

	public KipEvidence(String nodeName, String evidenceValue) {
		super();
		this.nodeName = nodeName;
		this.evidenceValue = evidenceValue;
	}

}

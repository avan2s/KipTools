package kip.tools.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KipSequence implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<NextBestAction> sequence;

	public KipSequence() {
		this.sequence = new ArrayList<>();
	}

	public KipSequence(List<NextBestAction> sequence) {
		this.sequence = sequence;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("KipSequence [");
		for (NextBestAction nextBestAction : sequence) {
			sb.append(" -> ").append(nextBestAction.getAction());
		}
		return sb.append(" ]").replace(0, 2, "").toString();
	}

	public List<NextBestAction> getSequence() {
		return sequence;
	}

	public void setSequence(List<NextBestAction> sequence) {
		this.sequence = sequence;
	}

}

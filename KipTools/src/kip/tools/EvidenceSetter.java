package kip.tools;

import java.util.Arrays;
import java.util.Map;

import smile.Network.NodeType;

public class EvidenceSetter {
	private InfluenceDiagramNetwork net;

	public EvidenceSetter(InfluenceDiagramNetwork net) {
		this.net = net;
	}

	public InfluenceDiagramNetwork getNet() {
		return net;
	}

	public void setNet(InfluenceDiagramNetwork net) {
		this.net = net;
	}

	public InfluenceDiagramNetwork setEvidences(Map<String, String> nodeNameToEvidence, boolean updateBeliefs) {
		for (Map.Entry<String, String> entry : nodeNameToEvidence.entrySet()) {
			String nodeId = entry.getKey();
			String evidenceValue = entry.getValue();
			this.setEvidence(nodeId, evidenceValue, false);
		}
		if (updateBeliefs) {
			net.updateBeliefs();
		}
		return net;
	}

	public InfluenceDiagramNetwork setEvidence(String nodeId, String evidenceValue, boolean updateBeliefs) {
		if (this.checkEvidenceSetPossible(nodeId, evidenceValue)) {
			net.setEvidence(nodeId, evidenceValue);
		}
		if (updateBeliefs) {
			net.updateBeliefs();
		}
		return net;
	}

	public boolean checkEvidenceSetPossible(String nodeId, String evidenceValue) {
		if (this.net.getNodeType(nodeId) == NodeType.Cpt) {
			if (this.net.isPropagatedEvidence(nodeId)) {
				return false;
			}
			int outcomeIndex = Arrays.asList(this.net.getOutcomeIds(nodeId)).indexOf(evidenceValue);
			if (outcomeIndex < 0) {
				return false;
			}

			double probability = this.net.getNodeValue(nodeId)[outcomeIndex];
			return probability > 0;
		}
		return false;
	}

}

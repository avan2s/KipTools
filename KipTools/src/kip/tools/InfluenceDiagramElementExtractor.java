package kip.tools;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import smile.Network;
import smile.Network.NodeType;

public class InfluenceDiagramElementExtractor {

	private InfluenceDiagramNetwork solvedNetwork;

	public InfluenceDiagramElementExtractor(InfluenceDiagramNetwork solvedNetwork) {
		this.solvedNetwork = solvedNetwork;
	}

	public TreeMap<String, Double> extractProbabilityDistribution(String nodeId) {
		TreeMap<String, Double> outcomeIdToProbability = new TreeMap<>();
		if (this.solvedNetwork.getNodeType(nodeId) == NodeType.Cpt) {
			double[] probabilities = this.solvedNetwork.getNodeValue(nodeId);
			String[] outcomeIds = this.solvedNetwork.getOutcomeIds(nodeId);
			int numberOfOutcomes = this.solvedNetwork.getOutcomeCount(nodeId);
			for(int i=0; i<numberOfOutcomes;i++){
				outcomeIdToProbability.put(outcomeIds[i], probabilities[i]);
			}
		}
		return outcomeIdToProbability;
	}
	
	public List<String> extractOutcomes(String nodeId) {
		if (this.solvedNetwork.getNodeType(nodeId) == NodeType.Cpt) {
			return Arrays.asList(this.solvedNetwork.getOutcomeIds(nodeId));
		}
		return null;
	}
	
	public String generateNodeId(int perdiodForRecommendation) {
		StringBuilder nodeId = new StringBuilder(this.solvedNetwork.getDecisionAbbreviation());
		nodeId.append(this.solvedNetwork.getPeriodSeperator()).append(perdiodForRecommendation);
		return nodeId.toString();
	}

	public Network getSolvedNetwork() {
		return solvedNetwork;
	}

	public void setSolvedNetwork(InfluenceDiagramNetwork solvedNetwork) {
		this.solvedNetwork = solvedNetwork;
	}

}

package kip.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import kip.tools.model.KipGoal;
import smile.Network.NodeType;

public class InfluenceDiagramElementExtractor {

	private InfluenceDiagramNetwork network;

	public InfluenceDiagramElementExtractor(InfluenceDiagramNetwork network) {
		this.network = network;
	}

	public TreeMap<String, Double> extractProbabilityDistribution(String nodeId) {
		TreeMap<String, Double> outcomeIdToProbability = new TreeMap<>();
		if (this.network.getNodeType(nodeId) == NodeType.Cpt) {
			double[] probabilities = this.network.getNodeValue(nodeId);
			String[] outcomeIds = this.network.getOutcomeIds(nodeId);
			int numberOfOutcomes = this.network.getOutcomeCount(nodeId);
			for (int i = 0; i < numberOfOutcomes; i++) {
				outcomeIdToProbability.put(outcomeIds[i], probabilities[i]);
			}
		}
		return outcomeIdToProbability;
	}

	public List<String> extractOutcomes(String nodeId) {
		List<String> outcomes = new ArrayList<>();
		if (this.network.getNodeType(nodeId) == NodeType.Cpt) {
			return Arrays.asList(this.network.getOutcomeIds(nodeId));
		}
		return outcomes;
	}

	public List<String> extractPossibleOutcomes(String nodeId) {
		List<String> possibleOutcomes = new ArrayList<>();
		if (this.network.getNodeType(nodeId) == NodeType.Cpt) {
			double[] probabilities = this.network.getNodeValue(nodeId);
			String[] outcomeIds = this.network.getOutcomeIds(nodeId);
			int numberOfOutcomes = this.network.getOutcomeCount(nodeId);
			for (int i = 0; i < numberOfOutcomes; i++) {
				if (probabilities[i] > 0) {
					possibleOutcomes.add(outcomeIds[i]);
				}
			}
		}
		return possibleOutcomes;
	}

	public List<String> getAllNodeIdsByPeriod(int period, KipGoal goal) {
		if (period == 0) {
			return new ArrayList<String>();
		}
		List<String> goalNodes = new LinkedList<String>(Arrays.asList(this.network.getAllNodeIds()));
		String nodeAbbreviation = goal.getAbbreviation();
		String seperator = this.network.getPeriodSeperator();

		for (Iterator<String> iterator = goalNodes.iterator(); iterator.hasNext();) {
			String nodeName = iterator.next();
			StringBuilder sbPattern = new StringBuilder(nodeAbbreviation);
			sbPattern.append(seperator).append("(.*)").append(seperator).append(period);

			if (!nodeName.matches(sbPattern.toString())) {
				iterator.remove();
			}
		}
		return goalNodes;
	}

	public String generateNodeId(String nodeAbbreviation, int period, boolean nullPeriodWithSeperator) {
		StringBuilder nodeId = new StringBuilder(nodeAbbreviation);
		if (period > 0 || (period == 0 && nullPeriodWithSeperator)) {
			nodeId.append(this.network.getPeriodSeperator()).append(period);
		}
		return nodeId.toString();
	}

	public InfluenceDiagramNetwork getNetwork() {
		return network;
	}

	public void setNetwork(InfluenceDiagramNetwork network) {
		this.network = network;
	}

}

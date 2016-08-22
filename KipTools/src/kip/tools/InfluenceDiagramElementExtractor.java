package kip.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import kip.tools.model.KipGoal;
import smile.Network.NodeType;
import smile.UserProperty;

public class InfluenceDiagramElementExtractor {

	private InfluenceDiagramNetwork network;

	public InfluenceDiagramElementExtractor(InfluenceDiagramNetwork network) {
		this.network = network;
	}

	public List<String> extractAllNodeIds() {
		return new LinkedList<String>(Arrays.asList(this.network.getAllNodeIds()));
	}

	public int extractPeriodFromNodeId(String nodeId, boolean nullPeriodWithSeperator) {
		String seperator = this.network.getPeriodSeperator();

		String[] nodeIdParts = nodeId.split(seperator);
		String possiblePeriod = nodeIdParts[nodeIdParts.length - 1];
		boolean isInstancePeriod = possiblePeriod.contentEquals(this.network.getInstancePeriod());
		if (possiblePeriod.matches("\\d+")) {
			return Integer.parseInt(possiblePeriod);
		} else if (!nullPeriodWithSeperator && !isInstancePeriod) {
			return 0;
		} else {
			return -1;
		}
	}

	public String extractNodeUserProperty(String nodeId, String userProperty) {
		UserProperty[] properties = this.network.getNodeUserProperties(nodeId);
		for (UserProperty property : properties) {
			if (property.name.contentEquals(userProperty)) {
				return property.value;
			}
		}
		return null;
	}

	public String extractNetworkUserProperty(String userProperty) {
		UserProperty[] properties = this.network.getUserProperties();
		for (UserProperty property : properties) {
			if (property.name.contentEquals(userProperty)) {
				return property.value;
			}
		}
		return null;
	}

	public String extractAbbreviation(String nodeId) {
		// String[] nodes = nodeId.split(this.network.getPeriodSeperator());
		// int lastIndex = nodes.length -1;
		// String lastPart = nodes[lastIndex];
		// int lastIndexBeforePeriodPart = lastIndex;
		//
		// boolean lastIsPeriod = lastPart.matches("\\d+") ||
		// lastPart.equals(this.network.getInstancePeriod());
		//
		// if(lastIsPeriod && nodes.length > 1){
		// lastIndexBeforePeriodPart = lastIndexBeforePeriodPart-1;
		// }
		// StringBuilder sb = new StringBuilder(nodes[0]);
		// for (int i = 1; i <= lastIndexBeforePeriodPart; i++) {
		// sb.append(this.network.getPeriodSeperator()).append(nodes[i]);
		// }
		// String s = sb.toString();
		// return s;
		String[] nodes = nodeId.split(this.network.getPeriodSeperator());
		return nodes[0];
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

	public List<String> getAllNodeIdsByPeriodAndGoal(int period, KipGoal goal) {
		if (period == 0) {
			return new ArrayList<String>();
		}
		List<String> goalNodeIds = this.extractAllNodeIds();
		String goalNodeAbbreviation = goal.getAbbreviation();
		String seperator = this.network.getPeriodSeperator();
		
		for (Iterator<String> iterator = goalNodeIds.iterator(); iterator.hasNext();) {
			String nodeId = iterator.next();
			String nodeAbbreviation = this.extractAbbreviation(nodeId);
			boolean hasAbbreviation = nodeAbbreviation.equals(goalNodeAbbreviation);
			boolean hasPeriod = nodeId.endsWith(seperator+String.valueOf(period));
			
			if (!hasAbbreviation || !hasPeriod) {
				iterator.remove();
			}
		}
		return goalNodeIds;
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

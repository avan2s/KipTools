package kip.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import kip.tools.model.ExpectedValue;
import kip.tools.model.KipGoal;
import smile.Network;

public class EffectExtractor {

	private InfluenceDiagramNetwork solvedNetwork;
	
	public EffectExtractor() {
		
	}

	public EffectExtractor(InfluenceDiagramNetwork solvedNetwork) {
		this.solvedNetwork = solvedNetwork;
	}

	public final ExpectedValue extract(KipGoal goal) throws Exception {
		ExpectedValue expectedValue = new ExpectedValue();
		expectedValue.setUnitValue(this.extractExpectedEffect(goal), goal.getGoalValue(), goal.getGoalEffect());
		return expectedValue;
	}

	private double extractExpectedEffect(KipGoal goal) throws Exception {
		int tVon = goal.getGoalstart_period();
		int tBis = goal.getGoalend_period();
		if ((tBis < 0 || tVon < 0) || tBis < tVon) {
			throw new Exception("Periods not valid!");
		}

		double expectedEffect = 0;

		for (int period = tVon; period <= tBis; period++) {
			List<String> goalNodes = this.getAllNodeIdsByPeriod(period,goal);
			for (String goalNode : goalNodes) {
				double effectInPeriod = this.solvedNetwork.getNodeValue(goalNode)[0];
				expectedEffect = expectedEffect + effectInPeriod;
			}
		}
		return expectedEffect;
	}

	private List<String> getAllNodeIdsByPeriod(int period, KipGoal goal) {
		if (period == 0) {
			return new ArrayList<String>();
		}
		List<String> goalNodes = new LinkedList<String>(Arrays.asList(this.solvedNetwork.getAllNodeIds()));
		String nodeAbbreviation = goal.getAbbreviation();
		String seperator = this.solvedNetwork.getPeriodSeperator();

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

	public Network getNetwork() {
		return solvedNetwork;
	}

	public void setNetwork(InfluenceDiagramNetwork network) {
		this.solvedNetwork = network;
	}

}

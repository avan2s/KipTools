package kip.tools;

import java.util.List;
import java.util.TreeMap;

import kip.tools.model.KipGoal;
import kip.tools.model.KipSequence;
import kip.tools.model.NextBestAction;
import kip.tools.model.SimAct;
import kip.tools.model.SimPeriod;

public class MaxProbabilitySequenceCalculator extends SequenceCalculator {
	private BenefitCalculator benefitCalculator;

	public MaxProbabilitySequenceCalculator(InfluenceDiagramNetwork network) {
		super(network);
		this.benefitCalculator = new BenefitCalculator(network);
	}

	@Override
	public KipSequence calculate(int currentPeriod, int lastPeriod, List<KipGoal> goals) throws Exception {
		NextBestAction nextBestAction = this.nextActionCalculator.calculateNextBestAction(currentPeriod, currentPeriod,
				goals);
		this.kipSequence.getSequence().add(nextBestAction);
		this.kipSequence.getSimPeriods().add(this.nextActionCalculator.getSimPeriod());

		for (int period = currentPeriod + 1; period <= lastPeriod; period++) {
			// Entscheidung in der Periode als Evidenz setzen
			String nodeId = this.influenceDiagramExtractor.generateNodeId(period);
			double maxProbability = 0;
			String maxProbabilityAction = null;
			int maxBenefit = 0;

			SimPeriod simPeriod = new SimPeriod();
			simPeriod.setPeriod(period);
			this.influenceDiagramExtractor.generateNodeId(period);
			List<String> actions = this.influenceDiagramExtractor.extractOutcomes(nodeId);
			TreeMap<String, Double> probabilityDistributions = this.influenceDiagramExtractor
					.extractProbabilityDistribution(nodeId);
			for (String action : actions) {
				double probability = probabilityDistributions.get(action);
				double benefit = this.benefitCalculator.calculateBenefit(currentPeriod, period, action, goals);
				SimAct simAct = this.benefitCalculator.getSimAct();
				simPeriod.addSimAct(simAct);

				if (probability > maxProbability) {
					maxProbabilityAction = action;
					maxProbability = probability;
					continue;
				}
				if (probability == maxProbability) {
					if (benefit > maxBenefit) {
						maxProbabilityAction = action;
						maxProbability = probability;
						continue;
					}
				}
			}
			nextBestAction.setAction(maxProbabilityAction);
			nextBestAction.setBenefit(maxBenefit);
			nextBestAction.setSimulatedValues(simPeriod.getSimActByAction(maxProbabilityAction));
			this.kipSequence.getSequence().add(nextBestAction);
			this.kipSequence.getSimPeriods().add(simPeriod);
			this.evidenceSetter.setEvidence(nodeId, maxProbabilityAction, true, true);
		}
		return this.kipSequence;
	}

}

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

	public MaxProbabilitySequenceCalculator(NextActionCalculator nextActionCalculator, EvidenceSetter evidenceSetter,
			InfluenceDiagramElementExtractor influenceDiagramExtractor, InfluenceDiagramNetwork network,
			BenefitCalculator benefitCalculator) {
		super(nextActionCalculator, evidenceSetter, influenceDiagramExtractor, network);
		this.benefitCalculator = benefitCalculator;
	}

	@Override
	public KipSequence calculate(int currentPeriod, int lastPeriod, List<KipGoal> goals) throws Exception {
		NextBestAction nextBestAction = this.nextActionCalculator.calculateNextBestAction(currentPeriod, currentPeriod,
				goals);
		this.kipSequence.getSequence().add(nextBestAction);
		this.kipSequence.getSimPeriods().add(this.nextActionCalculator.getSimPeriod());

		String nodeAbbreviation = this.network.getDecisionAbbreviation();
		String nodeId = this.influenceDiagramExtractor.generateNodeId(nodeAbbreviation, currentPeriod, false);
		this.evidenceSetter.setEvidence(nodeId, nextBestAction.getAction(), false, true);

		for (int period = currentPeriod + 1; period <= lastPeriod; period++) {
			// Entscheidung in der Periode als Evidenz setzen

			nodeId = this.influenceDiagramExtractor.generateNodeId(nodeAbbreviation, period, false);
			double maxProbability = 0;
			String maxProbabilityAction = null;
			double maxBenefit = 0;

			// Simulationswerte der Periode aufbereiten
			SimPeriod simPeriod = new SimPeriod();
			simPeriod.setPeriod(period);

			List<String> actions = this.influenceDiagramExtractor.extractPossibleOutcomes(nodeId);
			if (!nothingActionAllowed) {
				actions.remove("nichts");
			}

			TreeMap<String, Double> probabilityDistributions = this.influenceDiagramExtractor
					.extractProbabilityDistribution(nodeId);

			nextBestAction = new NextBestAction();

			// Iteriere durch jede Entscheidung am Entscheidungsknoten
			for (String action : actions) {
				double probability = probabilityDistributions.get(action);
				double benefit = this.benefitCalculator.calculateBenefit(currentPeriod, period, action, goals);
				SimAct simAct = this.benefitCalculator.getSimAct();
				simPeriod.addSimAct(simAct);

				if (probability > maxProbability) {
					maxProbabilityAction = action;
					maxProbability = probability;
					maxBenefit = benefit;
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
			
			if (maxProbabilityAction != null) {
				nextBestAction.setAction(maxProbabilityAction);
				nextBestAction.setBenefit(maxBenefit);
				this.kipSequence.getSimPeriods().add(simPeriod);
				this.evidenceSetter.setEvidence(nodeId, maxProbabilityAction, false, true);
			}
			else{
				maxProbabilityAction = "!No Action available for period " + period + "!";
				nextBestAction.setAction(maxProbabilityAction);
				nextBestAction.setBenefit(maxBenefit);
			
			}
			this.kipSequence.getSequence().add(nextBestAction);
		}
		return this.kipSequence;
	}

	public BenefitCalculator getBenefitCalculator() {
		return benefitCalculator;
	}

	public void setBenefitCalculator(BenefitCalculator benefitCalculator) {
		this.benefitCalculator = benefitCalculator;
	}

}

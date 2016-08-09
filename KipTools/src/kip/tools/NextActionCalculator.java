package kip.tools;

import java.util.List;

import kip.tools.model.KipGoal;
import kip.tools.model.NextBestAction;
import kip.tools.model.SimAct;
import kip.tools.model.SimPeriod;

public class NextActionCalculator {

	private InfluenceDiagramNetwork network;
	private BenefitCalculator benefitCalculator;
	private InfluenceDiagramElementExtractor extractor;
	private SimPeriod simPeriod;
	private NextBestAction nextBestAction;

	public NextActionCalculator(InfluenceDiagramNetwork network) {
		this.network = network;
		this.benefitCalculator = new BenefitCalculator(network);
	}

	// Abbildung: 60
	public NextBestAction calculateNextBestAction(int currentPeriod, int periodForRecommendation, List<KipGoal> goals)
			throws Exception {
		if (currentPeriod > periodForRecommendation) {
			throw new Exception("invalid current Period: Can't recommend a decision, which is already taken");
		}
		this.nextBestAction = new NextBestAction();
		this.nextBestAction.setBenefit(Double.MIN_VALUE);

		this.simPeriod = new SimPeriod();

		String nodeId = extractor.generateNodeId(periodForRecommendation);
		List<String> actions = extractor.extractOutcomes(nodeId);

		// Für jede Aktion
		for (String action : actions) {
			double benefit = this.benefitCalculator.calculateBenefit(currentPeriod, periodForRecommendation, action,
					goals);
			SimAct simAct = this.benefitCalculator.getSimAct();
			this.simPeriod.addSimAct(simAct);

			// Wenn Aktion einen höheren Nutzen erzielt,...
			if (benefit > this.nextBestAction.getBenefit()) {
				this.nextBestAction.setAction(action);
				this.nextBestAction.setBenefit(benefit);
			}
		}
		return this.nextBestAction;
	}

	public InfluenceDiagramNetwork getNetwork() {
		return network;
	}

	public void setNetwork(InfluenceDiagramNetwork network) {
		this.network = network;
	}

	public BenefitCalculator getBenefitCalculator() {
		return benefitCalculator;
	}

	public void setBenefitCalculator(BenefitCalculator benefitCalculator) {
		this.benefitCalculator = benefitCalculator;
	}

	public SimPeriod getSimPeriod() {
		return simPeriod;
	}

	public void setSimPeriod(SimPeriod simPeriod) {
		this.simPeriod = simPeriod;
	}

	public NextBestAction getNextBestAction() {
		return nextBestAction;
	}

	public void setNextBestAction(NextBestAction nextBestAction) {
		this.nextBestAction = nextBestAction;
	}

}

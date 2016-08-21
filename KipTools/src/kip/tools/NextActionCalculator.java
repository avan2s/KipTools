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
	private double benefit;
	private boolean nothingActionAllowed;

	public NextActionCalculator(InfluenceDiagramNetwork network) {
		this.network = network;
		this.benefitCalculator = new BenefitCalculator(network);
		this.extractor = new InfluenceDiagramElementExtractor(network);
	}

	public NextActionCalculator(InfluenceDiagramNetwork network, BenefitCalculator benefitCalculator,
			InfluenceDiagramElementExtractor extractor) {
		this.network = network;
		this.benefitCalculator = benefitCalculator;
		this.extractor = extractor;
	}

	// Abbildung: 60
	public NextBestAction calculateNextBestAction(int currentPeriod, int periodForRecommendation, List<KipGoal> goals)
			throws Exception {
		this.reset();
		if (currentPeriod > periodForRecommendation) {
			throw new Exception("invalid current Period: Can't recommend a decision, which is already taken");
		}
		this.nextBestAction = new NextBestAction();
		this.nextBestAction.setBenefit(-Double.MAX_VALUE);

		this.simPeriod.setPeriod(periodForRecommendation);

		String nodeId = extractor.generateNodeId(this.network.getDecisionAbbreviation(), periodForRecommendation,
				false);

		List<String> actions = extractor.extractPossibleOutcomes(nodeId);
		if (!nothingActionAllowed) {
			actions.remove("nichts");
		}

		// Für jede Aktion
		for (String action : actions) {
			this.benefit = this.benefitCalculator.calculateBenefit(currentPeriod, periodForRecommendation, action,
					goals);
			SimAct simAct = this.benefitCalculator.getSimAct();
			this.simPeriod.addSimAct(simAct);

			// Wenn Aktion einen höheren Nutzen erzielt,...
			if (this.benefit > this.nextBestAction.getBenefit()) {
				this.nextBestAction.setAction(action);
				this.nextBestAction.setBenefit(this.benefit);
			}
		}
		this.nextBestAction.setPeriod(periodForRecommendation);
		this.nextBestAction.setSimPeriod(this.simPeriod);
		return this.nextBestAction;
	}

	private void reset() {
		this.simPeriod = new SimPeriod();
		this.benefit = 0;
		this.nextBestAction = new NextBestAction();
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

	public InfluenceDiagramElementExtractor getExtractor() {
		return extractor;
	}

	public void setExtractor(InfluenceDiagramElementExtractor extractor) {
		this.extractor = extractor;
	}

	public double getBenefit() {
		return benefit;
	}

	public void setBenefit(double benefit) {
		this.benefit = benefit;
	}

	public boolean isNothingActionAllowed() {
		return nothingActionAllowed;
	}

	public void setNothingActionAllowed(boolean nothingActionAllowed) {
		this.nothingActionAllowed = nothingActionAllowed;
	}

}

package kip.tools;

import java.util.List;

import kip.tools.model.KipGoal;
import kip.tools.model.KipSequence;
import kip.tools.model.SimPeriod;

public abstract class SequenceCalculator {
	protected NextActionCalculator nextActionCalculator;
	protected EvidenceSetter evidenceSetter;
	protected InfluenceDiagramElementExtractor influenceDiagramExtractor;
	protected KipSequence kipSequence;
	protected InfluenceDiagramNetwork network;
	protected List<SimPeriod> simPeriods;
	protected boolean nothingActionAllowed;

	// InfluenceDiagramInteractor
	public SequenceCalculator(InfluenceDiagramNetwork network) {
		this.network = network;
		this.nothingActionAllowed = false;
		this.nextActionCalculator = new NextActionCalculator(network);
		this.nextActionCalculator.setNothingActionAllowed(this.nothingActionAllowed);
		this.evidenceSetter = new EvidenceSetter(network);
		this.influenceDiagramExtractor = new InfluenceDiagramElementExtractor(network);
		this.kipSequence = new KipSequence();
	}

	public SequenceCalculator(NextActionCalculator nextActionCalculator, EvidenceSetter evidenceSetter,
			InfluenceDiagramElementExtractor influenceDiagramExtractor, InfluenceDiagramNetwork network) {
		super();
		this.nextActionCalculator = nextActionCalculator;
		this.evidenceSetter = evidenceSetter;
		this.influenceDiagramExtractor = influenceDiagramExtractor;
		this.network = network;
	}



	public abstract KipSequence calculate(int currentPeriod, int lastPeriod, List<KipGoal> goals) throws Exception;

	protected void reset() {
		this.kipSequence.getSequence().clear();
		this.kipSequence.getSimPeriods().clear();
	}

	public NextActionCalculator getNextActionCalculator() {
		return nextActionCalculator;
	}

	public void setNextActionCalculator(NextActionCalculator nextActionCalculator) {
		this.nextActionCalculator = nextActionCalculator;
		this.nothingActionAllowed = nextActionCalculator.isNothingActionAllowed();
	}

	public KipSequence getKipSequence() {
		return kipSequence;
	}

	public void setKipSequence(KipSequence kipSequence) {
		this.kipSequence = kipSequence;
	}

	public List<SimPeriod> getSimPeriods() {
		return simPeriods;
	}

	public void setSimPeriods(List<SimPeriod> simPeriods) {
		this.simPeriods = simPeriods;
	}

	public EvidenceSetter getEvidenceSetter() {
		return evidenceSetter;
	}

	public void setEvidenceSetter(EvidenceSetter evidenceSetter) {
		this.evidenceSetter = evidenceSetter;
	}

	public InfluenceDiagramElementExtractor getInfluenceDiagramExtractor() {
		return influenceDiagramExtractor;
	}

	public void setInfluenceDiagramExtractor(InfluenceDiagramElementExtractor influenceDiagramExtractor) {
		this.influenceDiagramExtractor = influenceDiagramExtractor;
	}

	public InfluenceDiagramNetwork getNetwork() {
		return network;
	}

	public void setNetwork(InfluenceDiagramNetwork network) {
		this.network = network;
	}

	public boolean isNothingActionAllowed() {
		return nothingActionAllowed;
	}

	public void setNothingActionAllowed(boolean nothingActionAllowed) {
		this.nothingActionAllowed = nothingActionAllowed;
		this.nextActionCalculator.setNothingActionAllowed(nothingActionAllowed);
	}

}

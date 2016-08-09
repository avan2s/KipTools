package kip.tools;

import java.util.List;

import kip.tools.model.KipGoal;
import kip.tools.model.KipSequence;

public abstract class SequenceCalculator {
	protected NextActionCalculator nextActionCalculator;
	protected EvidenceSetter evidenceSetter;
	protected InfluenceDiagramElementExtractor influenceDiagramExtractor;
	protected KipSequence kipSequence;

	// InfluenceDiagramInteractor
	public SequenceCalculator(InfluenceDiagramNetwork network) {
		this.nextActionCalculator = new NextActionCalculator(network);
		this.evidenceSetter = new EvidenceSetter(network);
		this.influenceDiagramExtractor = new InfluenceDiagramElementExtractor(network);
		this.kipSequence = new KipSequence();
	}

	public abstract KipSequence calculate(int currentPeriod, int lastPeriod, List<KipGoal> goals) throws Exception;

	protected void reset(){
		this.kipSequence.getSequence().clear();
		this.kipSequence.getSimPeriods().clear();
	}
	
	public NextActionCalculator getNextActionCalculator() {
		return nextActionCalculator;
	}

	public void setNextActionCalculator(NextActionCalculator nextActionCalculator) {
		this.nextActionCalculator = nextActionCalculator;
	}

	public KipSequence getKipSequence() {
		return kipSequence;
	}

	public void setKipSequence(KipSequence kipSequence) {
		this.kipSequence = kipSequence;
	}

}

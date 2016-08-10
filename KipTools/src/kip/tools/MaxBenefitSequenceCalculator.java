package kip.tools;

import java.util.List;

import kip.tools.model.KipGoal;
import kip.tools.model.KipSequence;
import kip.tools.model.NextBestAction;

public class MaxBenefitSequenceCalculator extends SequenceCalculator {

	public MaxBenefitSequenceCalculator(InfluenceDiagramNetwork network) {
		super(network);
	}

	@Override
	public KipSequence calculate(int currentPeriod, int lastPeriod, List<KipGoal> goals) throws Exception {
		this.reset();
		for (int period = currentPeriod; period <= lastPeriod; period++) {
			// Nutzenerbringenste Aktion für die Periode ermitteln
			NextBestAction nextBestAction = this.nextActionCalculator.calculateNextBestAction(currentPeriod, period,
					goals);
			this.kipSequence.getSequence().add(nextBestAction);
			// Daraus einhergehende Simulationswerte bestimmen
			this.kipSequence.getSimPeriods().add(this.nextActionCalculator.getSimPeriod());

			// Entscheidung in der Periode als Evidenz setzen
			String nodeId = this.influenceDiagramExtractor.generateNodeId(period);
			this.evidenceSetter.setEvidence(nodeId, nextBestAction.getAction(), true, true);
		}
		return this.kipSequence;
	}

}

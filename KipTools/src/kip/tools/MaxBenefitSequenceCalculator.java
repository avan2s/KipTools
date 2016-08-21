package kip.tools;

import java.util.List;

import kip.tools.model.KipGoal;
import kip.tools.model.KipSequence;
import kip.tools.model.NextBestAction;
import kip.tools.model.SimPeriod;

public class MaxBenefitSequenceCalculator extends SequenceCalculator {

	public MaxBenefitSequenceCalculator(InfluenceDiagramNetwork network) {
		super(network);
	}

	public MaxBenefitSequenceCalculator(NextActionCalculator nextActionCalculator, EvidenceSetter evidenceSetter,
			InfluenceDiagramElementExtractor influenceDiagramExtractor, InfluenceDiagramNetwork network) {
		super(nextActionCalculator,evidenceSetter,influenceDiagramExtractor,network);
	}

	@Override
	public KipSequence calculate(int currentPeriod, int lastPeriod, List<KipGoal> goals) throws Exception {
		this.reset();
		for (int period = currentPeriod; period <= lastPeriod; period++) {
			// Nutzenerbringenste Aktion für die Periode ermitteln
			NextBestAction nextBestAction = this.nextActionCalculator.calculateNextBestAction(currentPeriod, period,
					goals);
			SimPeriod simPeriod = this.nextActionCalculator.getSimPeriod();
			nextBestAction.setSimPeriod(simPeriod);
			this.kipSequence.getSequence().add(nextBestAction);
			// Daraus einhergehende Simulationswerte bestimmen
			this.simPeriods.add(simPeriod);

			// Entscheidung in der Periode als Evidenz setzen
			String nodeAbbreviation = this.network.getDecisionAbbreviation();
			String nodeId = this.influenceDiagramExtractor.generateNodeId(nodeAbbreviation, period, false);
			this.evidenceSetter.setEvidence(nodeId, nextBestAction.getAction(), false, true);
		}
		return this.kipSequence;
	}

}

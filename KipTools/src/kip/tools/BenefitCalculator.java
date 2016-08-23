
package kip.tools;

import java.util.List;

import kip.tools.model.ExpectedValue;
import kip.tools.model.KipGoal;
import kip.tools.model.SimAct;

public class BenefitCalculator {

	private EffectExtractor effectExtractor;
	private EvidenceSetter evidenceSetter;
	private InfluenceDiagramElementExtractor elementExtractor;
	private InfluenceDiagramNetwork network;
	private SimAct simAct;

	public BenefitCalculator(InfluenceDiagramNetwork network) {
		this.initialize(network);
	}

	public BenefitCalculator(EffectExtractor effectExtractor, EvidenceSetter evidenceSetter,
			InfluenceDiagramElementExtractor elementExtractor, InfluenceDiagramNetwork network) {
		this.initialize(network);
		this.effectExtractor = effectExtractor;
		this.evidenceSetter = evidenceSetter;
		this.elementExtractor = elementExtractor;
	}

	private void initialize(InfluenceDiagramNetwork network) {
		this.network = network;
		this.effectExtractor = new EffectExtractor(network);
		this.elementExtractor = new InfluenceDiagramElementExtractor(network);
		this.evidenceSetter = new EvidenceSetter(network);

		this.simAct = new SimAct();
	}

	// Abbildung 59
	public double calculateBenefit(int currentPeriod, int periodForRecommendation, String action, List<KipGoal> goals)
			throws Exception {
		// Optimierungsmodell zurücksetzen
		this.reset();

		// Erhalte die NodeId (den Zufallsentscheidungsknoten)
		String nodeAbbreviation = this.network.getDecisionAbbreviation();
		String nodeId = this.elementExtractor.generateNodeId(nodeAbbreviation, periodForRecommendation, false);

		// Prüfe, ob die Periode gültig ist
		if (periodForRecommendation < currentPeriod)
			throw new Exception("invalid current Period: Can't recommend a decision, which is already taken");

		// Setze Evidenz und löse das Einflussdiagramm (updateBeliefs=true)
		// Einflussdiagramm wird verändert, da es als Referenz übergeben wurde
		this.evidenceSetter.setEvidence(nodeId, action, false, true);

		this.simAct.setAction(action);

		double benefit = 0;
		for (KipGoal kipGoal : goals) {
			// Berechne zukünftige Auswirkungen auf die Zielgröße
			ExpectedValue expectedValue = this.effectExtractor.extract(kipGoal);
			double weightedGoalUniformUtility = kipGoal.getGoalWeight() * expectedValue.getUniformUtility();
			benefit = benefit + weightedGoalUniformUtility;

			// Füge Zuordnung ExpectedValue dem Simulationsobjekt hinzu
			this.simAct.addSimGoal(kipGoal, expectedValue);
		}
		this.simAct.setBenefit(benefit);
		return benefit;
	}

	private void reset() {
		this.initialize(this.network);
	}

	// Abbildung 39 entfernen

	public EffectExtractor getEffectExtractor() {
		return effectExtractor;
	}

	public void setEffectExtractor(EffectExtractor effectExtractor) {
		this.effectExtractor = effectExtractor;
	}

	public EvidenceSetter getEvidenceSetter() {
		return evidenceSetter;
	}

	public void setEvidenceSetter(EvidenceSetter evidenceSetter) {
		this.evidenceSetter = evidenceSetter;
	}

	public InfluenceDiagramElementExtractor getElementExtractor() {
		return elementExtractor;
	}

	public void setElementExtractor(InfluenceDiagramElementExtractor elementExtractor) {
		this.elementExtractor = elementExtractor;
	}

	public InfluenceDiagramNetwork getNetwork() {
		return network;
	}

	public void setNetwork(InfluenceDiagramNetwork network) {
		this.network = network;
	}

	public SimAct getSimAct() {
		return simAct;
	}

	public void setSimAct(SimAct simAct) {
		this.simAct = simAct;
	}

}

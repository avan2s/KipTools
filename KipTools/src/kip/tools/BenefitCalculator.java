
package kip.tools;

import java.util.List;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import kip.enums.GoalEffect;
import kip.tools.model.ExpectedValue;
import kip.tools.model.KipGoal;
import kip.tools.model.SimAct;

public class BenefitCalculator {

	private IloCplex optimizationModel;
	private IloLinearNumExpr objectiveExpression;
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
		try {
			this.optimizationModel = new IloCplex();
			this.objectiveExpression = this.optimizationModel.linearNumExpr();
		} catch (IloException e) {
			e.printStackTrace();
		}
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

		for (KipGoal kipGoal : goals) {
			// Berechne zukünftige Auswirkungen auf die Zielgröße
			ExpectedValue expectedValue = this.effectExtractor.extract(kipGoal);
			this.addGoalToOptimizationModel(kipGoal, expectedValue.getUniformUtility());

			// Füge Zuordnung ExpectedValue dem Simulationsobjekt hinzu
			this.simAct.addSimGoal(kipGoal, expectedValue);
		}

		// Löse das Optimierungsmodell
		this.optimizationModel.addMaximize(this.objectiveExpression);
		this.optimizationModel.exportModel(action + ".lp");
		if (this.optimizationModel.solve()) {
			return this.optimizationModel.getObjValue();
		}
		throw new Exception("No Benefit could be calculated - invalid model!");
	}

	private void reset() {
		this.optimizationModel = null;
		this.objectiveExpression = null;
		this.initialize(network);
	}

	// Abbildung 39:
	private void addGoalToOptimizationModel(final KipGoal goal, final double expectedValue) throws IloException {
		double pUp;
		double pDown = -goal.getGoalWeight();
		if (goal.getGoalEffect().equals(GoalEffect.NEUTRAL)) {
			pUp = -goal.getGoalWeight();
		} else {
			pUp = goal.getGoalWeight();
		}

		double utilityOpt = UtilityTransformer.NORM_FACTOR;
		if (goal.getGoalEffect().equals(GoalEffect.NEGATIVE)) {
			utilityOpt = -utilityOpt;
		}

		// Nebenbedingung hinzufügen
		IloLinearNumExpr constraint = optimizationModel.linearNumExpr();
		IloNumVar deviationUp = optimizationModel.numVar(0, Double.MAX_VALUE);
		IloNumVar deviationDown = optimizationModel.numVar(0, Double.MAX_VALUE);
		constraint.addTerm(-1, deviationUp);
		constraint.addTerm(1, deviationDown);
		constraint.setConstant(expectedValue);
		optimizationModel.addEq(constraint, utilityOpt);

		// Zielfunktion um Term ergänzen
		objectiveExpression.addTerm(pUp, deviationUp);
		objectiveExpression.addTerm(pDown, deviationDown);
	}

	public IloCplex getOptimizationModel() {
		return optimizationModel;
	}

	public void setOptimizationModel(IloCplex optimizationModel) {
		this.optimizationModel = optimizationModel;
	}

	public IloLinearNumExpr getObjectiveExpression() {
		return objectiveExpression;
	}

	public void setObjectiveExpression(IloLinearNumExpr objectiveExpression) {
		this.objectiveExpression = objectiveExpression;
	}

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

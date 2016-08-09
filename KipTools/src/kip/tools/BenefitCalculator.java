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
		this.network = network;
		this.effectExtractor = new EffectExtractor();
		this.elementExtractor = new InfluenceDiagramElementExtractor(network);
		this.evidenceSetter = new EvidenceSetter(network);
		this.simAct = new SimAct();
		try {
			this.optimizationModel = new IloCplex();
		} catch (IloException e) {
			e.printStackTrace();
		}
	}

	// Abbildung 59
	public double calculateBenefit(int currentPeriod, int periodForRecommendation, String action, List<KipGoal> goals)
			throws Exception {
		// Optimierungsmodell zur�cksetzen
		this.resetModel();

		// Erhalte die NodeId (den Zufallsentscheidungsknoten)
		String nodeId = this.elementExtractor.generateNodeId(periodForRecommendation);

		// Pr�fe ob Evidenz gesetzt werden kann
		if (!this.evidenceSetter.checkEvidenceSetPossible(nodeId, action))
			throw new Exception("invalid action for decisionnode: " + nodeId);

		// Pr�fe, ob die Periode g�ltig ist
		if (periodForRecommendation < currentPeriod)
			throw new Exception("invalid current Period: Can't recommend a decision, which is already taken");

		// Setze Evidenz und l�se das Einflussdiagramm (updateBeliefs=true)
		// Einflussdiagramm wird ver�ndert, da es als Referenz �bergeben wurde
		this.evidenceSetter.setEvidence(nodeId, action, true);

		this.simAct = new SimAct();
		this.simAct.setAction(action);

		for (KipGoal kipGoal : goals) {
			// Berechne zuk�nftige Auswirkungen auf die Zielgr��e
			ExpectedValue expectedValue = this.effectExtractor.extract(kipGoal);
			this.addGoalToOptimizationModel(kipGoal, expectedValue.getUniformUtility());

			// F�ge Zuordnung ExpectedValue dem Simulationsobjekt hinzu
			this.simAct.addSimGoal(kipGoal, expectedValue);
		}

		// L�se das Optimierungsmodell
		if (this.optimizationModel.solve()) {
			return this.optimizationModel.getObjValue();
		}
		throw new Exception("No Benefit could be calculated - invalid model!");
	}

	private void resetModel() {
		try {
			this.optimizationModel = null;
			this.optimizationModel = new IloCplex();
		} catch (IloException e) {
			e.printStackTrace();
		}
	}

	// Abbildung 39:
	private void addGoalToOptimizationModel(final KipGoal goal, final double expectedValue) throws IloException {
		double pUp = goal.getGoalWeight();
		double pDown = -goal.getGoalWeight();

		double utilityOpt = UtilityTransformer.NORM_FACTOR;
		if (goal.getGoalEffect().equals(GoalEffect.NEGATIVE)) {
			utilityOpt = -utilityOpt;
		}

		// Nebenbedingung hinzuf�gen
		IloLinearNumExpr constraint = optimizationModel.linearNumExpr();
		IloNumVar deviationUp = optimizationModel.numVar(0, Double.MAX_VALUE);
		IloNumVar deviationDown = optimizationModel.numVar(0, Double.MAX_VALUE);
		constraint.addTerm(-1, deviationUp);
		constraint.addTerm(1, deviationDown);
		constraint.setConstant(expectedValue);
		optimizationModel.addEq(constraint, utilityOpt);

		// Zielfunktion um Term erg�nzen
		objectiveExpression.addTerm(pUp, deviationUp);
		objectiveExpression.addTerm(pDown, deviationDown);
	}

	// public void reset(){
	// this.clear();
	// this.;
	// }
	//
	// public void clear(){
	// this.effectExtractor = null;
	// this.evidenceSetter = null;
	// this.elementExtractor = null;
	// this.optimizationModel = null;
	// }

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

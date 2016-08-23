package kip.tools;

import java.util.List;

import kip.enums.KipGoalEffect;
import kip.tools.exception.PeriodNotValidException;
import kip.tools.exception.ValueNotReadableException;
import kip.tools.model.ExpectedValue;
import kip.tools.model.KipGoal;
import smile.Network;
import smile.SMILEException;

public class EffectExtractor {

	private InfluenceDiagramNetwork network;
	private InfluenceDiagramElementExtractor extractor;

	public EffectExtractor(InfluenceDiagramNetwork network) {
		this.extractor = new InfluenceDiagramElementExtractor(network);
		this.network = network;
	}

	public EffectExtractor(InfluenceDiagramNetwork network, InfluenceDiagramElementExtractor extractor) {
		this.network = network;
		this.extractor = extractor;
	}

	public final ExpectedValue extract(KipGoal goal) throws Exception {
		double unitValue = this.extractExpectedEffect(goal);
		double goalValue = goal.getGoalValue();
		KipGoalEffect goalEffect = goal.getGoalEffect();
		double uniformUtility = UtilityTransformer.calcUniformUtility(unitValue, goalValue, goalEffect);
		double procentualDeviation = UtilityTransformer.calcProcentualDeviation(unitValue, goalValue);
		ExpectedValue expectedValue = new ExpectedValue(uniformUtility,procentualDeviation,unitValue);
		return expectedValue;
	}

	// Extrahiere den erwarteten Effekt bzgl. einer Zielgröße
	private double extractExpectedEffect(KipGoal goal) throws PeriodNotValidException, ValueNotReadableException {
		double expectedEffect = 0;
		try {
			int tVon = goal.getGoalstart_period();
			int tBis = goal.getGoalend_period();
			if ((tBis < 0 || tVon < 0) || tBis < tVon) {
				throw new PeriodNotValidException("Periods not valid. Endperiod can't be lower than startperiod!");
			}

			expectedEffect = 0;

			for (int period = tVon; period <= tBis; period++) {
				List<String> goalNodes = this.extractor.getAllNodeIdsByPeriodAndGoal(period, goal);
				for (String goalNode : goalNodes) {
					double effectInPeriod = this.network.getNodeValue(goalNode)[0];
					expectedEffect = expectedEffect + effectInPeriod;
				}
			}
		} catch (SMILEException e) {
			throw new ValueNotReadableException();
		}
		return expectedEffect;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(InfluenceDiagramNetwork network) {
		this.network = network;
	}

	public InfluenceDiagramElementExtractor getExtractor() {
		return extractor;
	}

	public void setExtractor(InfluenceDiagramElementExtractor extractor) {
		this.extractor = extractor;
	}

}

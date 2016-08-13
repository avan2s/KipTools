package kip.tools;

import kip.enums.KipGoalEffect;

public class UtilityTransformer {
	public final static double NORM_FACTOR = 100;

	public static double calcUniformUtility(final double reachedValue, final double goalValue,
			final KipGoalEffect effect) {
		if (effect.equals(KipGoalEffect.NEGATIVE)) {
			return -1 * (reachedValue / goalValue * NORM_FACTOR);
		} else {
			return reachedValue / goalValue * NORM_FACTOR;
		}
	}

	public static double calcUnitValue(final double uniformUtility, final double goalValue, final KipGoalEffect effect) {
		if (effect.equals(KipGoalEffect.NEGATIVE)) {
			return -1 * ((uniformUtility * goalValue) / NORM_FACTOR);
		} else {
			return (uniformUtility * goalValue) / NORM_FACTOR;
		}
	}

}

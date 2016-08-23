package kip.tools;

import kip.enums.KipGoalEffect;

public class UtilityTransformer {
	public final static double NORM_FACTOR = 100;

	public static double calcProcentualDeviation(final double reachedValue, double goalValue) {
		// Vor 0 Teilung sch�tzen
		if(goalValue==0){
			goalValue = Double.MIN_VALUE;
		}
		double difference = reachedValue - goalValue;
		double procentualDeviation = Math.abs(difference) / Math.abs(goalValue) * NORM_FACTOR;
		if (difference < 0) {
			procentualDeviation = procentualDeviation * -1;
		}
		return procentualDeviation;
	}

	public static double calcUnitValueByProcentualDeviation(final double procentualDeviation, final double goalValue) {
		double unitValue = 0;
		double difference = Math.abs(procentualDeviation) * Math.abs(goalValue) / NORM_FACTOR;
		if (procentualDeviation < 0) {
			difference = difference * -1;
		}
		unitValue = difference + goalValue;
		return unitValue;
	}

	public static double calcUniformUtility(final double reachedValue, final double goalValue,
			final KipGoalEffect effect) {
		double utility = 0;
		double procentualDeviation = UtilityTransformer.calcProcentualDeviation(reachedValue, goalValue);

		// Bei positiven Zielgr��en entspricht die prozentuale Abweichung der
		// Utility
		// positive prozentuale Abweichung -> Abweichung nach oben -> erw�nscht
		// -> positive Utility (prozentuale Abweichung belassen)
		// negative prozentuale Abweichung -> Abweichung nach unten ->
		// unerw�nscht -> negative Utility (prozentuale Abweichung belassen)
		// d.h. Prozentuale Abweichung = utility
		if (effect == KipGoalEffect.POSITIVE) {
			utility = procentualDeviation;
		}

		// positive prozentuale Abweichung -> Abweichung nach oben ->
		// unerw�nscht -> negative Utility (prozentuale Abweichung umkehren)
		// negative prozentuale Abweichung -> Abweichung nach unten -> erw�nscht
		// -> positive Utiliy (prozentuale Abweichung umkehren)
		else if (effect == KipGoalEffect.NEGATIVE) {
			utility = procentualDeviation * -1;
		}
		// positive prozentuale Abweichung -> Abweichung nach oben ->
		// unerw�nscht -> negative Utility (prozentuale Abweichung umkehren)
		// negative prozentuale Abweichung -> Abweichung nach unten ->
		// unerw�nscht -> negative Utiliy
		else {
			// Abweichungen nach oben werden auch bestraft und sind negativ als
			// Utility auszudr�cken
			if (procentualDeviation > 0) {
				utility = procentualDeviation * -1;
			}
		}
		return utility;
	}

}

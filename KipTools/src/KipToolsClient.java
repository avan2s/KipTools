import java.util.ArrayList;
import java.util.List;

import kip.enums.KipGoalEffect;
import kip.tools.BenefitCalculator;
import kip.tools.EffectExtractor;
import kip.tools.EvidenceSetter;
import kip.tools.InfluenceDiagramElementExtractor;
import kip.tools.InfluenceDiagramNetwork;
import kip.tools.MaxBenefitSequenceCalculator;
import kip.tools.MaxProbabilitySequenceCalculator;
import kip.tools.NextActionCalculator;
import kip.tools.SequenceCalculator;
import kip.tools.UtilityTransformer;
import kip.tools.model.KipEvidence;
import kip.tools.model.KipGoal;
import kip.tools.model.KipSequence;
import kip.tools.model.NextBestAction;
import kip.tools.model.SimPeriod;

public class KipToolsClient {

	public static void main(String[] args) throws Exception {

		double pD1 = UtilityTransformer.calcProcentualDeviation(-20, 100);
		double U1 = UtilityTransformer.calcUniformUtility(-20, 100, KipGoalEffect.NEGATIVE);

		double pD2 = UtilityTransformer.calcProcentualDeviation(20, -100);
		double U2 = UtilityTransformer.calcUniformUtility(20, -100, KipGoalEffect.NEGATIVE);

		double pD3 = UtilityTransformer.calcProcentualDeviation(-120, -100);
		double U3 = UtilityTransformer.calcUniformUtility(-120, -100, KipGoalEffect.NEGATIVE);

		double pD4 = UtilityTransformer.calcProcentualDeviation(-20, 100);
		double U4 = UtilityTransformer.calcUniformUtility(-20, 100, KipGoalEffect.POSITIVE);

		double pD5 = UtilityTransformer.calcProcentualDeviation(20, -100);
		double U5 = UtilityTransformer.calcUniformUtility(20, -100, KipGoalEffect.POSITIVE);

		double pD6 = UtilityTransformer.calcProcentualDeviation(-120, -100);
		double U6 = UtilityTransformer.calcUniformUtility(-120, -100, KipGoalEffect.POSITIVE);

		double kWeight = 100;
		double kzWeight = 1;
		double zaWeight = 1;
		double spWeight = 1;
		double igWeight = 1;
		double sum = kWeight + kzWeight + zaWeight + spWeight + igWeight;
		// Ziele definieren
		KipGoal gtK = new KipGoal("Kosten", 0, 4, "K", 10000, KipGoalEffect.NEGATIVE);
		gtK.setGoalWeight(kWeight / sum);
		KipGoal gtZa = new KipGoal("Zeitaufwand", 0, 4, "ZA", 270, KipGoalEffect.NEGATIVE);
		gtZa.setGoalWeight(zaWeight / sum);
		KipGoal gtKz = new KipGoal("Kundenzufriedenheit", 0, 4, "KZ", 20, KipGoalEffect.POSITIVE);
		gtKz.setGoalWeight(kzWeight / sum);
		KipGoal gtSP = new KipGoal("Stakeholder-Power", 0, 4, "SP", -40, KipGoalEffect.NEGATIVE);
		gtSP.setGoalWeight(spWeight / sum);
		KipGoal gtIg = new KipGoal("Informationsgewinn", 0, 4, "IG", 1, KipGoalEffect.POSITIVE);
		gtIg.setGoalWeight(igWeight / sum);

		List<KipGoal> goals = new ArrayList<>();
		goals.add(gtK);
		goals.add(gtZa);
		goals.add(gtKz);
		goals.add(gtSP);
		goals.add(gtIg);

		// Einflussdiagram initialisieren und Wahrscheinlichkeiten berechnen
		InfluenceDiagramNetwork net = new InfluenceDiagramNetwork("_", "E", "I");
		net.readFile("shitstorm-shitstorm-instance-3.xdsl");
		net.updateBeliefs();

		// Kalkulatoren initialisieren
		EvidenceSetter evidenceSetter = new EvidenceSetter(net);
		InfluenceDiagramElementExtractor influenceDiagramExtractor = new InfluenceDiagramElementExtractor(net);
		EffectExtractor effectExtractor = new EffectExtractor(net);
		effectExtractor.setExtractor(influenceDiagramExtractor);

		String s = influenceDiagramExtractor.extractAbbreviation("Smtb_12");

		BenefitCalculator benefitCalculator = new BenefitCalculator(net);
		benefitCalculator.setEffectExtractor(effectExtractor);
		benefitCalculator.setElementExtractor(influenceDiagramExtractor);
		benefitCalculator.setEvidenceSetter(evidenceSetter);

		NextActionCalculator nextActionCalc = new NextActionCalculator(net);
		nextActionCalc.setExtractor(influenceDiagramExtractor);
		nextActionCalc.setBenefitCalculator(benefitCalculator);
		nextActionCalc.setNothingActionAllowed(true);

		SequenceCalculator sequenceCalculatorB = new MaxBenefitSequenceCalculator(net);
		sequenceCalculatorB.setEvidenceSetter(evidenceSetter);
		sequenceCalculatorB.setInfluenceDiagramExtractor(influenceDiagramExtractor);
		sequenceCalculatorB.setNothingActionAllowed(false);
		SequenceCalculator sequenceCalculatorP = new MaxProbabilitySequenceCalculator(net);
		sequenceCalculatorP.setEvidenceSetter(evidenceSetter);
		sequenceCalculatorP.setInfluenceDiagramExtractor(influenceDiagramExtractor);
		sequenceCalculatorP.setNothingActionAllowed(true);

		// Bisherige Evidenzen anlegen und setzen
		List<KipEvidence> evidences = new ArrayList<>();
		// evidences.add(new KipEvidence("SMTb", "enabled"));
		// evidences.add(new KipEvidence("Uf", "enabled"));
		// evidences.add(new KipEvidence("Ve", "available"));
		// evidences.add(new KipEvidence("Ev", "enabled"));
		// evidences.add(new KipEvidence("varU", "unbekannt"));
		// evidences.add(new KipEvidence("varSP", "hoch"));
		// evidences.add(new KipEvidence("varKZ", "niedrig"));
		// evidences.add(new KipEvidence("E", "Uf"));
		evidences.add(new KipEvidence("varU", "unbekannt"));
		evidences.add(new KipEvidence("varKZ", "niedrig"));
		evidences.add(new KipEvidence("varSP", "hoch"));
		evidences.add(new KipEvidence("SMTb", "enabled"));
		evidences.add(new KipEvidence("Uf", "enabled"));
		evidences.add(new KipEvidence("Ve", "available"));
		evidences.add(new KipEvidence("Ev", "enabled"));
		// evidences.add(new KipEvidence("E", "Uf"));
		// evidences.add(new KipEvidence("varU_1", "sachlich"));
		// evidences.add(new KipEvidence("varKZ_1", "niedrig"));
		// evidences.add(new KipEvidence("varSP_1", "hoch"));
		// evidences.add(new KipEvidence("SMTb_1", "enabled"));
		// evidences.add(new KipEvidence("Uf_1", "completed"));
		// evidences.add(new KipEvidence("Ve_1", "enabled"));
		// evidences.add(new KipEvidence("Ev_1", "enabled"));
		// evidences.add(new KipEvidence("Ev", "enabled"));
		evidenceSetter.setEvidences(evidences, false, true);

		nextActionCalc.calculateNextBestAction(0, 0, goals);
		NextBestAction nextBestAction = nextActionCalc.getNextBestAction();
		SimPeriod simPeriod = nextActionCalc.getSimPeriod();
		System.out.println(nextBestAction);

		// Sequenz berechnen
		sequenceCalculatorB.calculate(0, 3, goals);

		KipSequence kipSequence = sequenceCalculatorB.getKipSequence();
		List<SimPeriod> simPeriods = sequenceCalculatorB.getSimPeriods();

		System.out.println(kipSequence);

		// EffectExtractor effExtractor = new EffectExtractor(net);
		// ExpectedValue evK = effExtractor.extract(gtK);
		// ExpectedValue evZa = effExtractor.extract(gtZa);
		// ExpectedValue evKz = effExtractor.extract(gtKz);
		// ExpectedValue evSP = effExtractor.extract(gtSP);

	}

}

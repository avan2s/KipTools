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
import kip.tools.model.KipEvidence;
import kip.tools.model.KipGoal;
import kip.tools.model.KipSequence;
import kip.tools.model.NextBestAction;
import kip.tools.model.SimPeriod;

public class KipToolsClient {

	public static void main(String[] args) throws Exception {
		// Ziele definieren
		KipGoal gtK = new KipGoal("Kosten", 0, 4, "K", 10000, KipGoalEffect.NEGATIVE);
		gtK.setGoalWeight(0.05);
		KipGoal gtZa = new KipGoal("Zeitaufwand", 0, 4, "ZA", 130, KipGoalEffect.NEGATIVE);
		gtZa.setGoalWeight(0.15);
		KipGoal gtKz = new KipGoal("Kundenzufriedenheit", 0, 4, "KZ", 300, KipGoalEffect.POSITIVE);
		gtKz.setGoalWeight(0.4);
		KipGoal gtSP = new KipGoal("Stakeholder-Power", 0, 4, "SP", 50, KipGoalEffect.NEGATIVE);
		gtSP.setGoalWeight(0.1);
		KipGoal gtIg = new KipGoal("Informationsgewinn", 0, 4, "IG", 1, KipGoalEffect.POSITIVE);
		gtIg.setGoalWeight(0.3);

		List<KipGoal> goals = new ArrayList<>();
		goals.add(gtK);
		goals.add(gtZa);
		goals.add(gtKz);
		goals.add(gtSP);
		goals.add(gtIg);

		// Einflussdiagram initialisieren und Wahrscheinlichkeiten berechnen
		InfluenceDiagramNetwork net = new InfluenceDiagramNetwork("_", "E");
		net.readFile("shitstorm.xdsl");
		net.updateBeliefs();

		// Kalkulatoren initialisieren
		EvidenceSetter evidenceSetter = new EvidenceSetter(net);
		InfluenceDiagramElementExtractor influenceDiagramExtractor = new InfluenceDiagramElementExtractor(net);
		EffectExtractor effectExtractor = new EffectExtractor(net);
		effectExtractor.setExtractor(influenceDiagramExtractor);

		BenefitCalculator benefitCalculator = new BenefitCalculator(net);
		benefitCalculator.setEffectExtractor(effectExtractor);
		benefitCalculator.setElementExtractor(influenceDiagramExtractor);
		benefitCalculator.setEvidenceSetter(evidenceSetter);

		NextActionCalculator nextActionCalc = new NextActionCalculator(net);
		nextActionCalc.setExtractor(influenceDiagramExtractor);
		nextActionCalc.setBenefitCalculator(benefitCalculator);
		nextActionCalc.setNothingActionAllowed(false);
		SequenceCalculator sequenceCalculatorB = new MaxBenefitSequenceCalculator(net);
		sequenceCalculatorB.setEvidenceSetter(evidenceSetter);
		sequenceCalculatorB.setInfluenceDiagramExtractor(influenceDiagramExtractor);
		sequenceCalculatorB.setNothingActionAllowed(false);
		SequenceCalculator sequenceCalculatorP = new MaxProbabilitySequenceCalculator(net);
		sequenceCalculatorP.setEvidenceSetter(evidenceSetter);
		sequenceCalculatorP.setInfluenceDiagramExtractor(influenceDiagramExtractor);
		sequenceCalculatorP.setNothingActionAllowed(false);

		// Bisherige Evidenzen anlegen und setzen
		List<KipEvidence> evidences = new ArrayList<>();
		evidences.add(new KipEvidence("SMTb", "enabled"));
		evidences.add(new KipEvidence("Uf", "enabled"));
		evidences.add(new KipEvidence("Ve", "available"));
		evidences.add(new KipEvidence("Ev", "enabled"));
		evidences.add(new KipEvidence("varU", "unbekannt"));
		evidences.add(new KipEvidence("varSP", "hoch"));
		evidences.add(new KipEvidence("varKZ", "niedrig"));
		// evidences.add(new KipEvidence("E", "Uf"));
		evidenceSetter.setEvidences(evidences, false, true);

		// Sequenz berechnen
		sequenceCalculatorB.calculate(0, 3, goals);

		KipSequence kipSequence = sequenceCalculatorB.getKipSequence();
		List<SimPeriod> simPeriods = sequenceCalculatorB.getSimPeriods();

		System.out.println(kipSequence);

		nextActionCalc.calculateNextBestAction(0, 0, goals);
		NextBestAction nextBestAction = nextActionCalc.getNextBestAction();
		SimPeriod simPeriod = nextActionCalc.getSimPeriod();

		// EffectExtractor effExtractor = new EffectExtractor(net);
		// ExpectedValue evK = effExtractor.extract(gtK);
		// ExpectedValue evZa = effExtractor.extract(gtZa);
		// ExpectedValue evKz = effExtractor.extract(gtKz);
		// ExpectedValue evSP = effExtractor.extract(gtSP);

		System.out.println(nextBestAction);
	}

}

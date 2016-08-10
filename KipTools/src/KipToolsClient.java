import java.util.ArrayList;
import java.util.List;

import kip.enums.GoalEffect;
import kip.tools.EffectExtractor;
import kip.tools.EvidenceSetter;
import kip.tools.InfluenceDiagramNetwork;
import kip.tools.model.ExpectedValue;
import kip.tools.model.KipEvidence;
import kip.tools.model.KipGoal;

public class KipToolsClient {

	public static void main(String[] args) throws Exception {
//
		List<KipEvidence> evidences = new ArrayList<>();
		evidences.add(new KipEvidence("SMTb", "enabled"));
		evidences.add(new KipEvidence("Uf", "enabled"));
		evidences.add(new KipEvidence("Ve", "available"));
		evidences.add(new KipEvidence("Ev", "enabled"));
		evidences.add(new KipEvidence("varU", "unbekannt"));
		evidences.add(new KipEvidence("varSP", "hoch"));
		evidences.add(new KipEvidence("varKZ", "niedrig"));
		evidences.add(new KipEvidence("E", "Uf"));

		InfluenceDiagramNetwork net = new InfluenceDiagramNetwork("_", "E");
		// new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(net);

		System.out.println("Hello World!");

		net.readFile("shitstorm.xdsl");

		net.updateBeliefs();
		EvidenceSetter evidenceSetter = new EvidenceSetter(net);
		evidenceSetter.setEvidences(evidences, false, true);
		net = evidenceSetter.getNet();

		KipGoal gt = new KipGoal("Kosten", 1, 4, "K", 10000, GoalEffect.NEGATIVE);
		EffectExtractor effExtractor = new EffectExtractor(net);
		ExpectedValue expectedEffect = effExtractor.extract(gt);
		System.out.print(expectedEffect + " for " + gt.getGoalTarget());
	}

}

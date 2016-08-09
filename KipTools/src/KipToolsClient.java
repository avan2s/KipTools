import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import kip.enums.GoalEffect;
import kip.tools.EffectExtractor;
import kip.tools.EvidenceSetter;
import kip.tools.InfluenceDiagramNetwork;
import kip.tools.model.ExpectedValue;
import kip.tools.model.KipGoal;

public class KipToolsClient {

	public static void main(String[] args) throws Exception {

		InfluenceDiagramNetwork net = new InfluenceDiagramNetwork("_","E");
		new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(net);

		System.out.println("Hello World!");
		// Network net = new Network();

		net.readFile("shitstorm.xdsl");
		net.updateBeliefs();
		EvidenceSetter evidenceSetter = new EvidenceSetter(net);
		net = evidenceSetter.setEvidence("SMTb", "active", false);

		KipGoal gt = new KipGoal("Kosten", 0, 2, "K", 3000, GoalEffect.NEGATIVE);
		EffectExtractor effExtractor = new EffectExtractor(net);
		ExpectedValue expectedEffect = effExtractor.extract(gt);
		System.out.print(expectedEffect + " for " + gt.getGoalTarget());
		new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(net);
	}

}

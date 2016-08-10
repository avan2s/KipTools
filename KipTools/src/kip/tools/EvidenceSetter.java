package kip.tools;

import java.util.Arrays;
import java.util.List;

import kip.tools.exception.ValueNotReadableException;
import kip.tools.model.KipEvidence;
import smile.SMILEException;
import smile.Network.NodeType;

public class EvidenceSetter {

	private InfluenceDiagramNetwork net;

	public EvidenceSetter(InfluenceDiagramNetwork net) {
		this.net = net;
	}

	public InfluenceDiagramNetwork getNet() {
		return net;
	}

	public void setNet(InfluenceDiagramNetwork net) {
		this.net = net;
	}

	/**
	 * @param evidences
	 *            Evidenzen, die zu setzen sind
	 * @param checkEvidences
	 *            Bei true wird bei jeder Evidenz eine Validitätsprüfung anhand
	 *            des vorliegenden Influence Diagrams durchgeführt.
	 * @param afterUpdate
	 *            Bei true, werden die Wahrscheinlichkeitsverteilungen nach dem
	 *            Lösen des Influence Diagrams aktualisiert.
	 * @return
	 * @throws ValueNotReadableException
	 */
	public boolean setEvidences(List<KipEvidence> evidences, boolean checkEvidences, boolean afterUpdate)
			throws ValueNotReadableException {
		for (KipEvidence evidence : evidences) {
			String nodeId = evidence.getNodeName();
			String evidenceValue = evidence.getEvidenceValue();
			if (!this.setEvidence(nodeId, evidenceValue, checkEvidences, false)) {
				return false;
			}
		}
		if (afterUpdate) {
			this.net.updateBeliefs();
		}
		return true;
	}

	/**
	 * @param nodeId
	 *            Knoten-Id
	 * @param evidenceValue
	 *            Zustand, der dem Knoten als Evidenz zugewiesen werden soll
	 * @param checkEvidence
	 *            Bei true wird die Evidenz vor dem Setzen hinsichtlich ihrer
	 *            Gültigkeit im aktuellen Influence Diagram geprüft.
	 * @param afterUpdate
	 *            Bei true werden die Wahrscheinlichkeitsverteilungen des
	 *            InfluenceDiagrams aktualisiert. aktualisiert
	 * @return true, wenn die Evidenz erfolgreich gesetzt wurde
	 * @throws ValueNotReadableException
	 *             Wenn der Wert bei der Validitätsprüfung der Evidenz nicht
	 *             ausgelesen werden konnte.
	 */
	public boolean setEvidence(String nodeId, String evidenceValue, boolean checkEvidence, boolean afterUpdate)
			throws ValueNotReadableException {
		if (checkEvidence) {
			boolean isValidEvidence = this.isValidEvidence(nodeId, evidenceValue);
			if (!isValidEvidence) {
				return false;
			}
		}
		this.net.setEvidence(nodeId, evidenceValue);
		if (afterUpdate) {
			this.net.updateBeliefs();
		}
		return true;
	}

	public boolean isValidEvidence(String nodeId, String evidenceValue) throws ValueNotReadableException {
		int outcomeIndex = -1;
		if (this.net.getNodeType(nodeId) == NodeType.Cpt) {
			// Wenn es sich um eine propagierte Evidenz handelt, braucht sie
			// nicht gesetzt werden, da sie schon existiert.
			if (this.net.isPropagatedEvidence(nodeId)) {
				return false;
			}
			// Wenn indexOf -1 zurückgibt, existiert dieser Zustand nicht. Eine
			// Evidenz
			// für einen nicht existierenden Zustand kann nicht gesetzt werden.
			outcomeIndex = Arrays.asList(this.net.getOutcomeIds(nodeId)).indexOf(evidenceValue);
			if (outcomeIndex == -1) {
				return false;
			}
			try {
				return this.net.getNodeValue(nodeId)[outcomeIndex] > 0;
			} catch (SMILEException ex) {
				throw new ValueNotReadableException("Value could not be read: Network update is necessary");
			}
		}
		return false;
	}

}

package univie.cs.psa.msg;

import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.transport.Transport;

public class BroadcastWeights implements Control
{
	private static final String PAR_PROT = "protocol";
	private static final String PAR_TYPE = "type";

	private final int protocolID;
	private final boolean sum;

	public BroadcastWeights(String prefix)
	{
		protocolID = Configuration.getPid(prefix + "." + PAR_PROT);
		sum = Configuration.getString(prefix + "." + PAR_TYPE, "avg").equals("sum");
	}

	@Override
	public boolean execute()
	{
		Node root = Network.get(0);

		Transport transport = (Transport) root.getProtocol(FastConfig.getTransport(protocolID));
		transport.send(root, root, new InitializationMessage(1.0), protocolID);

		double nodeWeight = sum ? 0.0 : 1.0;

		for (int i = 1; i < Network.size(); i++)
		{
			transport = (Transport) Network.get(i).getProtocol(FastConfig.getTransport(protocolID));
			transport.send(root, Network.get(i), new InitializationMessage(nodeWeight), protocolID);
		}

		return false;
	}
}
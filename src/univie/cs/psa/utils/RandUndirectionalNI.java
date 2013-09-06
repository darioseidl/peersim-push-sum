package univie.cs.psa.utils;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Linkable;
import peersim.core.Network;
import peersim.core.Node;
import peersim.dynamics.NodeInitializer;

public class RandUndirectionalNI implements NodeInitializer
{
	private static final String PAR_PROT = "protocol";
	private static final String PAR_DEGREE = "k";
	private static final String PAR_PACK = "pack";

	private final int pid;
	private final int k;
	private final boolean pack;

	public RandUndirectionalNI(String prefix)
	{
		pid = Configuration.getPid(prefix + "." + PAR_PROT);
		k = Configuration.getInt(prefix + "." + PAR_DEGREE);
		pack = Configuration.contains(prefix + "." + PAR_PACK);
	}

	@Override
	public void initialize(Node n)
	{
		if (Network.size() == 0)
			return;

		Linkable linkable = (Linkable) n.getProtocol(pid);
		for (int j = 0; j < k; ++j)
		{
			int r = CommonState.r.nextInt(Network.size());
			linkable.addNeighbor(Network.get(r));
			//add undirectional edge to neighbor
			((Linkable) Network.get(r).getProtocol(pid)).addNeighbor(n);
		}

		if (pack)
		{
			linkable.pack();
		}
	}
}

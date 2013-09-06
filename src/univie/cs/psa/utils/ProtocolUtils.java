package univie.cs.psa.utils;

import peersim.config.FastConfig;
import peersim.core.CommonState;
import peersim.core.Linkable;
import peersim.core.Node;

public class ProtocolUtils
{
	public static Node getRandomNeighbor(Node self, int protocolID)
	{
		Linkable linkable = (Linkable) self.getProtocol(FastConfig.getLinkable(protocolID));

		if (linkable.degree() > 0)
		{
			Node neighbor = linkable.getNeighbor(CommonState.r.nextInt(linkable.degree()));

			if (neighbor.isUp())
			{
				return neighbor;
			}
		}

		return null;
	}

	private ProtocolUtils()
	{}
}

package univie.cs.psps.utils;

import peersim.config.FastConfig;
import peersim.core.CommonState;
import peersim.core.Linkable;
import peersim.core.Node;

/**
 * A utility class for commonly used functions in protocols.
 * 
 * @author Dario Seidl
 * 
 */
public class ProtocolUtils
{
	/**
	 * Returns a randomly selected neighbor of {@code self}.
	 * 
	 * @param self
	 *            the calling node.
	 * @param protocolID
	 *            the ID of the {@link Linkable} protocol to use.
	 * @return a randomly selected node from the neighbors of {@code self}, or
	 *         {@code null} if {@code self} has no neighbors or the chosen
	 *         neighbor is down.
	 */
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

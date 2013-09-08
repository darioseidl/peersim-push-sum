package univie.cs.psa.utils;

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
	 * Returns a randomly selected neighbor of <code>self</code>.
	 * 
	 * @param self
	 *            The calling node.
	 * @param protocolID
	 *            The ID of the {@link peersim.core.Linkable} protocol to use.
	 * @return A randomly selected node from the neighbors of <code>self</code>,
	 *         or <code>null</code> if <code>self</code> has no neighbors or the
	 *         chosen neighbor is down.
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

package univie.cs.psa.utils;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Linkable;
import peersim.core.Network;
import peersim.core.Node;
import peersim.dynamics.NodeInitializer;
import peersim.dynamics.RandNI;

/**
 * A {@link NodeInitializer} creating a given number of random undirectional
 * links for a node. This class does exactly the same as {@link RandNI}, except
 * that the created links are undirectional.
 * <p>
 * This control expects the following parameters in the configuration file:
 * <p>
 * <blockquote><code>protocol</code> - the name of the protocol to wire<br/>
 * <code>k</code> - the number of links to create per node<br/>
 * <code>pack</code> - if this paramter is defined, call {@link Linkable#pack()}
 * after initializing the links<br/>
 * </blockquote>
 * 
 * @author Dario Seidl
 * 
 */
public class RandUndirectionalNI implements NodeInitializer
{
	private static final String PAR_PROTOCOL = "protocol";
	private static final String PAR_DEGREE = "k";
	private static final String PAR_PACK = "pack";

	private final int pid;
	private final int k;
	private final boolean pack;

	/**
	 * The standard constructor called by the simulator, reading parameters from
	 * the configuration file.
	 * 
	 * @param prefix
	 *            the prefix for this control in the configuration file.
	 */
	public RandUndirectionalNI(String prefix)
	{
		pid = Configuration.getPid(prefix + "." + PAR_PROTOCOL);
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
			((Linkable) Network.get(r).getProtocol(pid)).addNeighbor(n);
		}

		if (pack)
			linkable.pack();
	}
}

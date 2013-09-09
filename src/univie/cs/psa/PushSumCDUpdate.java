package univie.cs.psa;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

/**
 * Control class needed for the cycle-driven Push-Sum to call
 * {@link PushSumCD#update()} in all nodes after each cycle.<br/>
 * <br/>
 * This control expects the following parameters in the configuration file:
 * <blockquote><code>protocol</code> - the name of {@link PushSumCD} protocol<br/>
 * </blockquote>
 * 
 * @author Dario Seidl
 * 
 */
public class PushSumCDUpdate implements Control
{
	private static final String PAR_PROT = "protocol";

	private final int protocolID;

	/**
	 * The standard constructor called by the simulator, reading parameters from
	 * the configuration file.
	 * 
	 * @param prefix
	 *            The prefix for this control in the configuration file.
	 */
	public PushSumCDUpdate(String prefix)
	{
		protocolID = Configuration.getPid(prefix + "." + PAR_PROT);
	}

	/**
	 * Calls {@link PushSumCD#update()} in all nodes in the network.
	 */
	@Override
	public boolean execute()
	{
		for (int i = 0; i < Network.size(); i++)
		{
			((PushSumCD) Network.get(i).getProtocol(protocolID)).update();
		}

		return false;
	}
}

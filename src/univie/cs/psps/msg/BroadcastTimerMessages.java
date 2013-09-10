package univie.cs.psps.msg;

import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.transport.Transport;

/**
 * A control class to initialize the weights of all nodes in event-driven
 * simulations.
 * <p>
 * This control expects the following parameters in the configuration file:
 * <p>
 * <blockquote>{@code protocol} - the name of the protocol to use for
 * sending the messages<br/>
 * </blockquote>
 * 
 * @author Dario Seidl
 * 
 */
public class BroadcastTimerMessages implements Control
{
	private static final String PAR_PROT = "protocol";

	private final int protocolID;

	/**
	 * The standard constructor called by the simulator, reading parameters from
	 * the configuration file.
	 * 
	 * @param prefix
	 *            the prefix for this control in the configuration file.
	 */
	public BroadcastTimerMessages(String prefix)
	{
		protocolID = Configuration.getPid(prefix + "." + PAR_PROT);
	}

	/**
	 * Sends an {@link TimerMessage} to each node in the network.
	 */
	@Override
	public boolean execute()
	{
		Node root = Network.get(0);

		for (int i = 0; i < Network.size(); i++)
		{
			Transport transport = (Transport) Network.get(i).getProtocol(FastConfig.getTransport(protocolID));
			transport.send(root, Network.get(i), new TimerMessage(), protocolID);
		}

		return false;
	}
}
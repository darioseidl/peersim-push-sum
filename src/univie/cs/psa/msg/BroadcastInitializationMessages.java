package univie.cs.psa.msg;

import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.transport.Transport;

/**
 * A control class to initialize the weights of all nodes in event-driven
 * simulations.<br/>
 * <br/>
 * This control expects the following parameters in the configuration file:
 * <blockquote><code>protocol</code> - the name of the protocol to use for
 * sending the messages<br/>
 * </blockquote>
 * 
 * @author Dario Seidl
 * 
 */
public class BroadcastInitializationMessages implements Control
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
	public BroadcastInitializationMessages(String prefix)
	{
		protocolID = Configuration.getPid(prefix + "." + PAR_PROT);
	}

	/**
	 * Sends an {@link InitializationMessage} to each node in the network.
	 */
	@Override
	public boolean execute()
	{
		Node root = Network.get(0);

		for (int i = 0; i < Network.size(); i++)
		{
			Transport transport = (Transport) Network.get(i).getProtocol(FastConfig.getTransport(protocolID));
			transport.send(root, Network.get(i), new InitializationMessage(1.0), protocolID);
		}

		return false;
	}
}
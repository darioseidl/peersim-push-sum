/*
 * Copyright (c) 2013 Faculty of Computer Science, University of Vienna
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
 * <blockquote>{@code protocol} - the name of the protocol to use for sending
 * the messages<br/>
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
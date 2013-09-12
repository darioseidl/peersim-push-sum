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
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.edsim.EDSimulator;

/**
 * A control class to send {@link TimerMessage} messages to all nodes to
 * initiate the first step in event-driven simulations. Each messages will be
 * send after a random delay to avoid an unrealistic synchronization between the
 * nodes.
 * <p>
 * This control expects the following parameters in the configuration file:
 * <p>
 * <blockquote>{@code protocol} - the name of the protocol to use for sending
 * the messages<br/>
 * {@code step} - each message will be sent after a delay drawn at random from 0
 * (inclusive) to {@code step} (exclusive)<br/>
 * </blockquote>
 * 
 * @author Dario Seidl
 * 
 */
public class BroadcastTimerMessages implements Control
{
	private static final String PAR_PROT = "protocol";
	private static final String PAR_STEP = "step";

	private final int protocolID;
	private final int stepSize;

	/**
	 * The standard constructor called by the simulator, reading parameters from
	 * the configuration file.
	 */
	public BroadcastTimerMessages(String prefix)
	{
		protocolID = Configuration.getPid(prefix + "." + PAR_PROT);
		stepSize = Configuration.getInt(prefix + "." + PAR_STEP);
	}

	/**
	 * Sends an {@link TimerMessage} to each node in the network.
	 */
	@Override
	public boolean execute()
	{
		for (int i = 0; i < Network.size(); i++)
		{
			EDSimulator.add(CommonState.r.nextInt(stepSize), new TimerMessage(), Network.get(i), protocolID);
		}

		return false;
	}
}
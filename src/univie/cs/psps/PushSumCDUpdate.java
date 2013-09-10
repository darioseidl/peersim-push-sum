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

package univie.cs.psps;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

/**
 * Control class needed for the cycle-driven Push-Sum to call
 * {@link PushSumCD#update()} in all nodes after each cycle.
 * <p>
 * This control expects the following parameters in the configuration file:
 * <p>
 * <blockquote>{@code protocol} - the name of {@link PushSumCD} protocol<br/>
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
	 *            the prefix for this control in the configuration file.
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

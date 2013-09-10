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

package univie.cs.psps.utils;

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
 * <blockquote>{@code protocol} - the name of the protocol to wire<br/>
 * {@code k} - the number of links to create per node<br/>
 * {@code pack} - if this paramter is defined, call {@link Linkable#pack()}
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

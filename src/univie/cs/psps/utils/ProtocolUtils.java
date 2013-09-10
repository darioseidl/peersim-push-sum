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

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

import peersim.cdsim.CDProtocol;
import peersim.core.Node;
import peersim.vector.VectControl;
import univie.cs.psps.utils.AggregationProtocol;
import univie.cs.psps.utils.ProtocolUtils;

/**
 * A cycle-driven implementation of the Push-Pull protocol.
 * <p>
 * In each cycle, each node selects a random neighbor and sets the estimate of
 * the neighbor and of itself to the mean of their current estimates.
 * <p>
 * This implementation is based on a very simplified model in which we take
 * advantage of two kinds of synchronization: cycles and synchronous
 * communication. Instead of sending a message and waiting for a response, we
 * update the estimates of the communicating nodes directly and simultaneously.
 * 
 * @author Dario Seidl
 * 
 */
public class PushPullCD implements AggregationProtocol, CDProtocol
{
	private double trueValue;
	private double estimate;

	/**
	 * The standard constructor called by the simulator, reading parameters from
	 * the configuration file.
	 */
	public PushPullCD(String prefix)
	{}

	/**
	 * Called once for each node before advancing to the next cycle.
	 * <p>
	 * In each cycle, each node selects a random neighbor and sets the estimate
	 * of the neighbor and of itself to the mean of their current estimates.
	 */
	@Override
	public void nextCycle(Node self, int protocolID)
	{
		Node neighbor = ProtocolUtils.getRandomNeighbor(self, protocolID);

		if (neighbor != null)
		{
			PushPullCD neighborProtocol = (PushPullCD) neighbor.getProtocol(protocolID);

			// set the estimate of the neighbor and of itself to the mean of
			// their current estimates
			double mean = (this.estimate + neighborProtocol.estimate) / 2;
			this.estimate = mean;
			neighborProtocol.estimate = mean;
		}
	}

	/**
	 * Returns the unmodified value of the node.
	 */
	@Override
	public double getTrueValue()
	{
		return trueValue;
	}

	/**
	 * Returns the local estimate of the mean value of all nodes in the network.
	 */
	@Override
	public double getEstimate()
	{
		return estimate;
	}

	/**
	 * Sets the value of this node. Should be called by subclasses of
	 * {@link VectControl} to initialize all nodes in the network.
	 */
	public void initializeValue(double value)
	{
		this.trueValue = value;
		this.estimate = value;
	}

	@Override
	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new RuntimeException(e);
		}
	}
}

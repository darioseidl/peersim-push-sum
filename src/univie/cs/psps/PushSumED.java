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
import peersim.config.FastConfig;
import peersim.core.CommonState;
import peersim.core.Node;
import peersim.edsim.EDProtocol;
import peersim.edsim.EDSimulator;
import peersim.transport.Transport;
import peersim.vector.VectControl;
import univie.cs.psps.msg.TimerMessage;
import univie.cs.psps.utils.AggregationProtocol;
import univie.cs.psps.utils.ProtocolUtils;

/**
 * An event-driven implementation of the Push-Sum protocol.
 * <p>
 * In each step, each node sums up the received values and weights of the
 * previous step and then sends half of its value and half of its weight to a
 * randomly selected neighbor and to itself.
 * <p>
 * This protocol expects the following additional parameters in the
 * configuration file:
 * <p>
 * <blockquote>{@code step} - the time between timer messages signaling the
 * start of the next step<br/>
 * </blockquote>
 * 
 * @author Dario Seidl
 * 
 */
public class PushSumED implements AggregationProtocol, EDProtocol
{
	private static final String PAR_STEP = "step";

	private static int stepSize;

	private double trueValue;
	private double value;
	private double weight;

	private double valueBuffer;
	private double weightBuffer;

	/**
	 * The standard constructor called by the simulator, reading parameters from
	 * the configuration file.
	 */
	public PushSumED(String prefix)
	{
		stepSize = Configuration.getInt(prefix + "." + PAR_STEP);
	}

	@Override
	public void processEvent(Node self, int protocolID, Object event)
	{
		// a timer message signaling the start of a new step
		if (event instanceof TimerMessage)
		{
			Node neighbor = ProtocolUtils.getRandomNeighbor(self, protocolID);

			if (neighbor != null)
			{
				// sum up
				value = valueBuffer;
				weight = weightBuffer;

				// send half of value and weight to self
				valueBuffer = value / 2;
				weightBuffer = weight / 2;

				// send half of value and weight to a random neighbor
				Transport transport = (Transport) self.getProtocol(FastConfig.getTransport(protocolID));
				transport.send(self, neighbor, new PushSumMessage(value / 2, weight / 2), protocolID);

			}

			// schedule a timer message for the next step
			EDSimulator.add(CommonState.r.nextInt(stepSize), new TimerMessage(), self, protocolID);

		}
		// a message from a neighbor
		else if (event instanceof PushSumMessage)
		{
			PushSumMessage message = (PushSumMessage) event;

			valueBuffer += message.getValue();
			weightBuffer += message.getWeight();
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
	 * The estimate is obtained by dividing value by weight.
	 */
	@Override
	public double getEstimate()
	{
		return value / weight;
	}

	/**
	 * Sets the value of this node. Should be called by subclasses of
	 * {@link VectControl} to initialize all nodes in the network.
	 */
	public void initializeValue(double value)
	{
		trueValue = value;
		this.value = value;
		valueBuffer = value;
	}

	/**
	 * Sets the weight of this node. Should be called by subclasses of
	 * {@link VectControl} to initialize all nodes in the network.
	 */
	public void initializeWeight(double weight)
	{
		this.weight = weight;
		weightBuffer = weight;
	}

	@Override
	public String toString()
	{
		return String.format("(%e, %e)", value, weight);
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

class PushSumMessage
{
	private final double value;
	private final double weight;

	public PushSumMessage(double value, double weight)
	{
		this.value = value;
		this.weight = weight;
	}

	public double getValue()
	{
		return value;
	}

	public double getWeight()
	{
		return weight;
	}
}

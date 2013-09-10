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
import peersim.core.Node;
import peersim.edsim.EDProtocol;
import peersim.edsim.EDSimulator;
import peersim.transport.Transport;
import peersim.vector.VectControl;
import univie.cs.psps.msg.TimerMessage;
import univie.cs.psps.utils.AggregationProtocol;
import univie.cs.psps.utils.ProtocolUtils;

/**
 * An event-driven implementation of the Push-Pull protocol.
 * <p>
 * In each step, each node selects a random neighbor and sets the estimate of
 * the neighbor and of itself to the mean of their current estimates.
 * 
 * @author Dario Seidl
 * 
 */
public class PushPullED implements AggregationProtocol, EDProtocol
{
	private static final String PAR_STEP = "step";

	private final int stepSize;
	private double trueValue;
	private double estimate;
	private boolean wait;

	public PushPullED(String prefix)
	{
		stepSize = Configuration.getInt(prefix + "." + PAR_STEP);
	}

	@Override
	public void processEvent(Node self, int protocolID, Object event)
	{
		if (event instanceof ContinueMessage)
		{
			wait = false;
			return;
		}

		// a timer message signalling the start of a new step
		else if (event instanceof TimerMessage)
		{
			Node neighbor = ProtocolUtils.getRandomNeighbor(self, protocolID);

			if (neighbor != null)
			{
				PushPullMessage push = new PushPullMessage(self, estimate);
				Transport transport = (Transport) self.getProtocol(FastConfig.getTransport(protocolID));
				transport.send(self, neighbor, push, protocolID);

				// after sending a push message, the node has to wait for a
				// short time, during which incoming push messages are ignored
				wait = true;
				EDSimulator.add(1, new ContinueMessage(), self, protocolID);
			}

			// schedule a timer message for the next step
			EDSimulator.add(stepSize, event, self, protocolID);
		}

		// a message from a neighbor
		else if (event instanceof PushPullMessage)
		{
			PushPullMessage msg = (PushPullMessage) event;

			// if sender is not null it is a push message that has to be
			// answered
			if (msg.getSender() != null)
			{
				// but if this node just sent out a push message, we ignore this
				// message
				if (wait)
				{
					return;
				}

				// send an answer
				PushPullMessage pull = new PushPullMessage(null, estimate);
				Transport transport = (Transport) self.getProtocol(FastConfig.getTransport(protocolID));
				transport.send(self, msg.getSender(), pull, protocolID);
			}

			// set the estimate to the mean of its own estimate and the estimate
			// of the sender of the message
			estimate = (msg.getValue() + estimate) / 2;
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
			throw new RuntimeException();
		}
	}
}

class PushPullMessage
{
	private final Node sender;
	private final double value;

	PushPullMessage(Node sender, double value)
	{
		this.sender = sender;
		this.value = value;
	}

	public Node getSender()
	{
		return sender;
	}

	public double getValue()
	{
		return value;
	}
}

class ContinueMessage
{}
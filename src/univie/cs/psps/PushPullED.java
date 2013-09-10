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

public class PushPullED implements AggregationProtocol, EDProtocol
{
	private static final String PAR_STEP = "step";

	private final int stepSize;
	private double trueValue;
	private double estimate;
	private boolean initiated = false;

	public PushPullED(String prefix)
	{
		stepSize = Configuration.getInt(prefix + "." + PAR_STEP);

		// TODO get rid of this restriction
		if (stepSize % 4 != 0)
		{
			throw new IllegalArgumentException("step must be divisble by 4.");
		}
	}

	@Override
	public void processEvent(Node self, int protocolID, Object event)
	{
		// TODO what is the deal with initiated???
		if (event == null)
		{
			initiated = false;
			return;
		}

		// a timer message signalling the start of a new step
		else if (event instanceof TimerMessage)
		{
			Node neighbor = ProtocolUtils.getRandomNeighbor(self, protocolID);

			if (neighbor != null)
			{
				EDSimulator.add(stepSize / 4, null, self, protocolID);

				initiated = true;
				ValueSenderMessage request = new ValueSenderMessage(initiated, self, estimate);
				Transport transport = (Transport) self.getProtocol(FastConfig.getTransport(protocolID));
				transport.send(self, neighbor, request, protocolID);
			}

			// schedule a timer message for the next step
			EDSimulator.add(stepSize, event, self, protocolID);
		}

		// a message from a neighbor
		else if (event instanceof ValueSenderMessage)
		{
			ValueSenderMessage msg = (ValueSenderMessage) event;

			if (msg.isInitiated())
			{
				if (initiated)
				{
					return;
				}

				double temp = msg.getValue();

				ValueSenderMessage request = new ValueSenderMessage(initiated, self, estimate);
				Transport transport = (Transport) self.getProtocol(FastConfig.getTransport(protocolID));
				transport.send(self, msg.getSender(), request, protocolID);

				estimate = (temp + estimate) / 2;
			}
			else
			{
				estimate = (msg.getValue() + estimate) / 2;
				initiated = false;
			}
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

// TODO combine with ValueWeightMessage
class ValueSenderMessage
{
	private final boolean initiated;
	private final Node sender;
	private final double value;

	ValueSenderMessage(boolean initiated, Node sender, double value)
	{
		this.initiated = initiated;
		this.sender = sender;
		this.value = value;
	}

	public boolean isInitiated()
	{
		return initiated;
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
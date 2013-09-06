package univie.cs.psa;

import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.CommonState;
import peersim.core.Node;
import peersim.edsim.EDProtocol;
import peersim.edsim.EDSimulator;
import peersim.transport.Transport;
import univie.cs.psa.edutils.TimerMessage;
import univie.cs.psa.edutils.WeightMessage;
import univie.cs.psa.utils.ProtocolUtils;
import univie.cs.psa.utils.ValueWeight;

public class PushPullED implements ValueWeight, EDProtocol
{
	private static final String PAR_STEP = "step";

	private final int step;
	private double trueValue;
	private double value;
	private boolean initiated = false;

	public PushPullED(String prefix)
	{
		step = Configuration.getInt(prefix + "." + PAR_STEP);

		if (step % 2 != 0)
		{
			throw new IllegalArgumentException("step must be divisble by 2.");
		}
	}

	@Override
	public void processEvent(Node self, int protocolID, Object event)
	{
		if (event == null)
		{
			initiated = false;
			return;
		}
		else if (event instanceof WeightMessage)
		{
			int delay = CommonState.r.nextInt(step / 2);
			EDSimulator.add(delay, new TimerMessage(), self, protocolID);
		}
		else if (event instanceof TimerMessage)
		{
			Node neighbor = ProtocolUtils.getRandomNeighbor(self, protocolID);

			if (neighbor != null)
			{
				EDSimulator.add(step / 4, null, self, protocolID);

				initiated = true;
				ValueSenderMessage request = new ValueSenderMessage(initiated, self, value);
				Transport transport = (Transport) self.getProtocol(FastConfig.getTransport(protocolID));
				transport.send(self, neighbor, request, protocolID);

				EDSimulator.add(step, event, self, protocolID);
			}
		}
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

				ValueSenderMessage request = new ValueSenderMessage(initiated, self, value);
				Transport transport = (Transport) self.getProtocol(FastConfig.getTransport(protocolID));
				transport.send(self, msg.getSender(), request, protocolID);

				value = (temp + value) / 2;
			}
			else
			{
				value = (msg.getValue() + value) / 2;
				initiated = false;
			}
		}
	}

	@Override
	public double getTrueValue()
	{
		return trueValue;
	}

	@Override
	public double getValue()
	{
		return value;
	}

	@Override
	public void setValue(double value)
	{
		this.trueValue = value;
		this.value = value;
	}

	@Override
	public void setWeight(double weight)
	{}

	@Override
	public double getWeight()
	{
		return Double.NaN;
	}

	@Override
	public double getEstimate()
	{
		return value;
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
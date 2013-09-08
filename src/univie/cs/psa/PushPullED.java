package univie.cs.psa;

import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.CommonState;
import peersim.core.Node;
import peersim.edsim.EDProtocol;
import peersim.edsim.EDSimulator;
import peersim.transport.Transport;
import univie.cs.psa.msg.InitializationMessage;
import univie.cs.psa.msg.TimerMessage;
import univie.cs.psa.utils.AggregationProtocol;
import univie.cs.psa.utils.ProtocolUtils;

public class PushPullED implements AggregationProtocol, EDProtocol
{
	private static final String PAR_STEP = "step";

	private final int step;
	private double trueValue;
	private double estimate;
	private boolean initiated = false;

	public PushPullED(String prefix)
	{
		step = Configuration.getInt(prefix + "." + PAR_STEP);

		if (step % 4 != 0)
		{
			throw new IllegalArgumentException("step must be divisble by 4.");
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

		else if (event instanceof InitializationMessage)
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
				ValueSenderMessage request = new ValueSenderMessage(initiated, self, estimate);
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

	@Override
	public double getTrueValue()
	{
		return trueValue;
	}

	@Override
	public double getEstimate()
	{
		return estimate;
	}

	public void initialize(double value)
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
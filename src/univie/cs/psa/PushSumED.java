package univie.cs.psa;

import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.Node;
import peersim.edsim.EDProtocol;
import peersim.edsim.EDSimulator;
import peersim.transport.Transport;
import univie.cs.psa.msg.TimerMessage;
import univie.cs.psa.msg.ValueWeightMessage;
import univie.cs.psa.utils.AggregationProtocol;
import univie.cs.psa.utils.ProtocolUtils;

public class PushSumED implements AggregationProtocol, EDProtocol
{
	private static final String PAR_STEP = "step";

	private static int step;

	private double trueValue;
	private double value;
	private double weight;

	private double valueBuffer;
	private double weightBuffer;

	public PushSumED(String prefix)
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
		//a timer message signalling the start of a new cycle
		if (event instanceof TimerMessage)
		{
			Node neighbor = ProtocolUtils.getRandomNeighbor(self, protocolID);

			if (neighbor != null)
			{
				//sum up
				value = valueBuffer;
				weight = weightBuffer;

				//send to self
				valueBuffer = value / 2;
				weightBuffer = weight / 2;

				//send to neighbor
				Transport transport = (Transport) self.getProtocol(FastConfig.getTransport(protocolID));
				transport.send(self, neighbor, new ValueWeightMessage(value / 2, weight / 2), protocolID);

			}

			EDSimulator.add(step, new TimerMessage(), self, protocolID);

		}
		//a message from a neighbor
		else if (event instanceof ValueWeightMessage)
		{
			ValueWeightMessage message = (ValueWeightMessage) event;

			valueBuffer += message.getValue();
			weightBuffer += message.getWeight();
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
		return value / weight;
	}

	public void initializeValue(double value)
	{
		trueValue = value;
		this.value = value;
		valueBuffer = value;
	}

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

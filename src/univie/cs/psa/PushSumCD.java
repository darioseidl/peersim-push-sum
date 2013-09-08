package univie.cs.psa;

import peersim.cdsim.CDProtocol;
import peersim.core.Node;
import univie.cs.psa.utils.AggregationProtocol;
import univie.cs.psa.utils.ProtocolUtils;

public class PushSumCD implements AggregationProtocol, CDProtocol
{
	private double trueValue;
	private double value;
	private double weight;

	private double valueBuffer;
	private double weightBuffer;

	public PushSumCD(String prefix)
	{}

	@Override
	public void nextCycle(Node self, int protocolID)
	{
		Node neighbor = ProtocolUtils.getRandomNeighbor(self, protocolID);

		if (neighbor != null)
		{
			PushSumCD neighborProtocol = (PushSumCD) neighbor.getProtocol(protocolID);

			//send half of value and weight to self
			valueBuffer += value / 2;
			weightBuffer += weight / 2;

			//send half of value and weight to a random neighbor
			neighborProtocol.valueBuffer += value / 2;
			neighborProtocol.weightBuffer += weight / 2;
		}
	}

	public void update()
	{
		//sum up
		value = valueBuffer;
		weight = weightBuffer;
		valueBuffer = 0.;
		weightBuffer = 0.;
	}

	@Override
	public double getTrueValue()
	{
		return trueValue;
	}

	@Override
	public double getEstimate()
	{
		return (value == 0.) ? 0. : value / weight;
	}

	public void initializeValue(double value)
	{
		this.trueValue = value;
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
		Object o = null;
		try
		{
			o = super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new RuntimeException(e);
		}
		return o;
	}
}

package univie.cs.psa;

import peersim.cdsim.CDProtocol;
import peersim.core.Node;
import univie.cs.psa.utils.ProtocolUtils;
import univie.cs.psa.utils.ValueWeight;

public class PushSumCD implements ValueWeight, CDProtocol
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

			//send to self
			valueBuffer += value / 2;
			weightBuffer += weight / 2;

			//send to neighbor
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
	public double getValue()
	{
		return value;
	}

	@Override
	public void setValue(double value)
	{
		this.trueValue = value;
		this.value = value;
		valueBuffer = value;
	}

	@Override
	public double getWeight()
	{
		return weight;
	}

	@Override
	public void setWeight(double weight)
	{
		this.weight = weight;
		weightBuffer = weight;
	}

	@Override
	public double getEstimate()
	{
		return (value == 0.) ? 0. : value / weight;
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

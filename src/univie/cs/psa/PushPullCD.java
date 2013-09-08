package univie.cs.psa;

import peersim.cdsim.CDProtocol;
import peersim.core.Node;
import univie.cs.psa.utils.AggregationProtocol;
import univie.cs.psa.utils.ProtocolUtils;

public class PushPullCD implements AggregationProtocol, CDProtocol
{
	private double trueValue;
	private double value;

	public PushPullCD(String prefix)
	{}

	@Override
	public void nextCycle(Node self, int protocolID)
	{
		Node neighbor = ProtocolUtils.getRandomNeighbor(self, protocolID);

		if (neighbor != null)
		{
			PushPullCD neighborProtocol = (PushPullCD) neighbor.getProtocol(protocolID);

			double mean = (this.value + neighborProtocol.value) / 2;
			this.value = mean;
			neighborProtocol.value = mean;
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
	public double getWeight()
	{
		return Double.NaN;
	}

	@Override
	public void setWeight(double weight)
	{}

	@Override
	public double getEstimate()
	{
		return value;
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

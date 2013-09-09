package univie.cs.psa;

import peersim.cdsim.CDProtocol;
import peersim.core.Node;
import univie.cs.psa.utils.AggregationProtocol;
import univie.cs.psa.utils.ProtocolUtils;

public class PushPullCD implements AggregationProtocol, CDProtocol
{
	private double trueValue;
	private double estimate;

	public PushPullCD(String prefix)
	{}

	@Override
	public void nextCycle(Node self, int protocolID)
	{
		Node neighbor = ProtocolUtils.getRandomNeighbor(self, protocolID);

		if (neighbor != null)
		{
			PushPullCD neighborProtocol = (PushPullCD) neighbor.getProtocol(protocolID);

			//set the estimate of self and neighbor to the mean of the current estimates
			double mean = (this.estimate + neighborProtocol.estimate) / 2;
			this.estimate = mean;
			neighborProtocol.estimate = mean;
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
			throw new RuntimeException(e);
		}
	}
}

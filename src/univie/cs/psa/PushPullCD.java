package univie.cs.psa;

import peersim.cdsim.CDProtocol;
import peersim.core.Node;
import peersim.vector.VectControl;
import univie.cs.psa.utils.AggregationProtocol;
import univie.cs.psa.utils.ProtocolUtils;

/**
 * A cycle-driven implementation of the Push-Pull protocol.
 * <p>
 * In each cycle a node sends half of it's value and half of it's weight to a
 * randomly selected neighbor and to itself. In the cycle driven implementation,
 * instead of sending a message we write directly into the buffers of the
 * receiving node.
 * <p>
 * Afterwards all nodes sum up the received values. Since this has to happen
 * after all the values have been exchanged, we implement this as a seperate
 * control class {@link PushSumCDUpdate}, that is executed after each cycle.
 * 
 * @author Dario Seidl
 * 
 */
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

			//set the estimate of self and neighbor to the mean of their current estimates
			double mean = (this.estimate + neighborProtocol.estimate) / 2;
			this.estimate = mean;
			neighborProtocol.estimate = mean;
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
	 * Setter to initialize the value of this node. Called by subclasses of
	 * {@link VectControl}.
	 */
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

package univie.cs.psps;

import peersim.cdsim.CDProtocol;
import peersim.core.Node;
import peersim.vector.VectControl;
import univie.cs.psps.utils.AggregationProtocol;
import univie.cs.psps.utils.ProtocolUtils;

/**
 * A cycle-driven implementation of the Push-Pull protocol.
 * <p>
 * In each cycle, each node selects a random neighbor and sets the estimate of
 * the neighbor and of itself to the mean of their current estimates.
 * 
 * @author Dario Seidl
 * 
 */
public class PushPullCD implements AggregationProtocol, CDProtocol
{
	private double trueValue;
	private double estimate;

	/**
	 * The standard constructor called by the simulator, reading parameters from
	 * the configuration file.
	 * 
	 * @param prefix
	 *            the prefix for this control in the configuration file.
	 */
	public PushPullCD(String prefix)
	{}

	@Override
	public void nextCycle(Node self, int protocolID)
	{
		Node neighbor = ProtocolUtils.getRandomNeighbor(self, protocolID);

		if (neighbor != null)
		{
			PushPullCD neighborProtocol = (PushPullCD) neighbor.getProtocol(protocolID);

			// set the estimate the neighbor and of itself to the mean of their
			// current estimates
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
			throw new RuntimeException(e);
		}
	}
}
